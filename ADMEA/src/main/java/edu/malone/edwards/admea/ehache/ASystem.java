/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache;

import edu.malone.edwards.admea.ehache.nodeUtils.Node;
import edu.malone.edwards.admea.ehache.nodeUtils.Nodes;
import edu.malone.edwards.admea.ehache.nodeUtils.State;
import edu.malone.edwards.admea.ehache.qlearning.QLearning;
import edu.malone.edwards.admea.ehache.qlearning.QlearningQueue;
import edu.malone.edwards.admea.utils.Debugging;
import edu.malone.edwards.admea.utils.Stopwatch;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The main class for the algorithm.
 * 
 * VERSION 2 IDEA:
 *      Turn performAction into submit action and have it submit a runnable to
 *          an exutorservice that can then be cancelled and have it checking periodically if
 *          if the current thread is being interuppted by Thread.currentThread.isInterreupted().
 *          This way the programmer does not have to worry about that. Just have a shutdown method called.
 * 
 * @author Cory Edwards
 */
public abstract class ASystem {
    
    /**
     *A always existing list of all of the Nodes in the algorithm.
     *Analogous to a jar holding every Node, and the Nodes are free of each
     *other in the jar. 
     */
    protected static Nodes nodeList = new Nodes();
    
    /**
     * Used to keep track of the last root Node that was used to update
     * its binomial data in the next iteration.
     */
    private String lastNodeId = null;
    
    /**
     * Used to keep track of the last winning child Node that was used to update
     * its binomial data in the next iteration.
     */
    private String lastStepId = null; //used child Node
    
    /**
     * Used to determine if debugging is needed.
     */
    public static boolean DEBUG = false;
    
    private final Debugging debugger = new Debugging();
    
    public static QlearningQueue qLearningBuffer = new QlearningQueue(2);
    public static ExecutorService qLearningQueue = Executors.newCachedThreadPool();
    
    private double oldTime = 0.0;
    private double newTime = 0.0;
    
    /**
     * Determine which child to go to next from the root.
     * @param children all of the children from the root Node.
     * @return The winning child's state.
     */
    private State getWinningPath(Set<String> children)
    {
        THashMap<String, Double> finalScores = new THashMap(); //Will be the list of children and their best scores.
        String winningChild = "";
        for(String child : children)
        {
            winningChild = child;
            //Get the child's scores and probabilities per path.
            ArrayList<double[]> pathScores = getScoreProbabilties(child, new THashSet());
            double[] results = new double[pathScores.size()];
            
            //Each path's final score (binomial probabilities * sum of scores).
            for(int i = 0; i < results.length; i++)
                results[i] = pathScores.get(i)[0] * pathScores.get(i)[1];
            
            //Find the winning path.
            double max = -Double.MAX_VALUE;
            for(double item : results)
                if(item > max)
                    max = item;
            
            //Put the child's Id into the list with its winning path's score.
            finalScores.put(child, max);
        }
        
        //Find the child with the highest score.
        double max = -Double.MAX_VALUE;
        for(String child : finalScores.keySet())
        {
            double item = finalScores.get(child);
            if(item >= max)
            {
                winningChild = child;
                max = item;
            }
        }
        
        //Return the winning child's state.
        return Nodes.getNode(winningChild).getState();
    }
    
    /**
     * Recursively go over each child's tree to find all score, probability combinations
     * for all possible paths.
     * @param childId The current Node's child.
     * @return An ArrayList of all possible score, probability combinations for all current paths.
     */
    private ArrayList<double[]> getScoreProbabilties(String childId, THashSet oldSet)
    {
        Node node = Nodes.getNode(childId);
        
        if(oldSet.contains(childId))
        {
            debugger.println("Loop found for Node: " + childId);
            
            //We have hit a loop so treat as leaf and return.
            ArrayList<double[]> temp = new ArrayList();
            temp.add(new double[]{node.calculateProbability(), node.getMaxQScore()});
            return temp;
        }
        else
        {
            THashSet<String> set = new THashSet();
            set.addAll(oldSet);
            set.add(childId);
            
            //If there are no children for the current Node then we have hit a leaf Node.
            if(node.children.isEmpty())
            {
                //Get the leaf Node's score and binomial probability.
                ArrayList<double[]> temp = new ArrayList();
                temp.add(new double[]{node.calculateProbability(), node.getMaxQScore()});
                return temp;
            }
            else //There are possibly branches so we need to go over them.
            {
                //Get an ArrayList ready to hold all paths.
                ArrayList<double[]> results = new ArrayList();

                //Go over current Node's children.
                for(String child : node.children)
                {
                    //Get all of the child's paths.
                    ArrayList<double[]> tempResults = getScoreProbabilties(child, set);

                    //Go over every path and combine the current Node's binomial probability
                    //and score.
                    for(double[] list : tempResults.subList(0, tempResults.size()))
                    {
                        list[0] = list[0] * node.calculateProbability();
                        list[1] = list[1] + node.getMaxQScore();
                        results.add(list);
                    }
                }
                //We are starting to go back to the root (which is a child of the actual root).
                return results;
            }
        }
    }
    
    /**
     * Main method to the algorithm. Call this on every iteration and the algorithm
     * does the rest.
     * @param state The current state in the process the algorithm is running in.
     * @return The next step to take in the process.
     */
    public State performAction(State state)
    {
        Stopwatch time = new Stopwatch();
        oldTime = 0.0;
        newTime = 0.0;
        debugger.println("Starting process.");
        
        //If it is null, the algorithm is just starting.
        //Otherwise we need to see how the last root did.
        if(lastNodeId != null)
        {
            //If the proccess thinks the past Node pair was a success.
            if(isSuccess(Nodes.getNode(lastStepId), state))
            {
                Nodes.getNode(lastNodeId).addSuccess();
                Nodes.getNode(lastStepId).addSuccess();
            }
        }
        
        debugger.addTiming(getElapsed(time), false);
        
        //This will get the Node with the given state, or make one and return it.
        Node root = nodeList.getNode(state);
        
        debugger.println("Root Node Id: " + root.getNodeId());
        
        //When each Node is made there is a boolean that is set to true.
        //When isNew is called it gets that boolean then sets it to false.
        if(root.isNew())
        {
            debugger.println("Creating children.");
            
            //Create the new Node's children.
            root.children = setChildren(state, root);
//            Nodes.flushBuffer();
            debugger.addTiming(getElapsed(time), false);
            
            //Check for loops and get rid of children that create the loop.
//            root.children = nodeList.deleteLoops(root.children, root.getNodeId());
//            System.out.println("Loops deleted: " + getElapsed(time));
            
            //Add the root Node as a parent to its children.
//            for(String child : root.children)
//            {
//                Nodes.getNode(child).addParent(root);
//            }
//            System.out.println("Parents added : " + getElapsed(time));
            
            debugger.println("Children created.");
            debugger.println("Calculating score.");
            
            //Set the new Node's score.
            root.setScore(calculateScore(state));
            debugger.addTiming(getElapsed(time), false);
            
            debugger.println("Score calculated.");
            
            //Create policy for all Nodes and apply them.
            new QLearning(root).run();
            debugger.addTiming(getElapsed(time), false);
            
            debugger.println("Policies given.");
            
            debugger.println("Root has " + root.parents.length + " number of parents.");
            debugger.println("Root has " + root.children.size() + " number of children.");
        }
        
        //Use the policy for the Node given the state along with combined 
        //binomial distributions to get the correct next Node's state to take.
        State nextState = getWinningPath(root.children);
        debugger.addTiming(getElapsed(time), false);
        
        debugger.println("Next path found.");
        
        //Get the root Node's Id for later.
        lastNodeId = root.getNodeId();
        
        //Add an occurence to both Nodes being used. (IE n++ in a binomial distribution)
        root.addOccurrence();
        Node nextNode = nodeList.getNode(nextState);
        nextNode.addOccurrence();
        
        debugger.addTiming(getElapsed(time), false);
        
        lastStepId = nextNode.getNodeId();
        
        debugger.println("Next Node Id: " + lastStepId);
        debugger.println("Total number of Nodes: " + nodeList.numberOfNodes());
        debugger.println("Returning next state.");
        debugger.println("");
        
        //Update the Nodes' probabilities.
        updateProbabilities();
        debugger.addTiming(getElapsed(time), false);
        
        Nodes.saveNode(root);
        Nodes.saveNode(nextNode);
        
        debugger.addTiming(getElapsed(time), false);
        
        
        debugger.addTiming(time.elapsedTime(), true);
        System.out.println();
        System.out.println();
        //Return the next state to take.
        return nextState;
    }
    
    private double getElapsed(Stopwatch watch)
    {
        oldTime = newTime;
        newTime = watch.elapsedTime();
        return newTime - oldTime;
    }
    
    /**
     * Use lastNodeId and lastStepId to call: nodeList.getNode(lastNodeId).recalcProb();
     * Alternatively, someone could override this method and give a probability
     * rather than calculating it. This could be done by calling .giveProb(new probability)
     * instead of .recalcProb().
     */
    public void updateProbabilities()
    {
        Nodes.getNode(lastNodeId).recalcProb();
        Nodes.getNode(lastStepId).recalcProb(); 
    }
    
    public Node getNode(State state)
    {
        return nodeList.getNode(state);
    }
    
    public Node getNode(State state, Node parent)
    {
        return nodeList.getNode(state, parent);
    }
    
    /**
     * Start up the algorithm so it can load Nodes from storage.
     * @throws java.net.MalformedURLException
     */
    public void init() throws MalformedURLException
    {
        nodeList.init();
    }
    
    /**
     * Sha-1 hash the state to get its id.
     * @param state The state to hash
     * @return 
     */
    public String hashState(State state)
    {
        return DigestUtils.shaHex(state.toString());
    }
    
    /**
     * Close the algorithm safely so that Nodes can be safely written to storage.
     */
    public void close()
    {
        nodeList.close();
        qLearningQueue.shutdown();
    }
    
    /**
     * When a path's action is performed, it will try and see if it was successful or not.
     * @param lastNode The winning child Node from the last iteration.
     * @param newNodeState The new state in the current iteration.
     * @return True if and only if the child Node taken was successful.
     */
    public abstract boolean isSuccess(Node lastNode, State newNodeState);
    
    /**
     * How the program will calculate the score to give to Q learning when a policy is being made.
     * @param state The state to get the score from.
     * @return The score that will go along with this state.
     */
    public abstract int calculateScore(State state);
    
    /**
     * 
     * @param stateA A Node's state.
     * @param stateB The new state.
     * @return True if and only if stateA = stateB
     */
    public abstract boolean areStatesEqual(State stateA, State stateB);
    
    /**
     * Use the given state and NODE_LIST to create all of the children from 
     * the state, and return a list of their Ids.
     * @param state The parent's state
     * @return A String[] of all of the newly created child Ids.
     */
    public abstract Set<String> setChildren(State state, Node parent);
}
