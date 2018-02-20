package edu.malone.edwards.admea.ehache.qlearning;

import static edu.malone.edwards.admea.ehache.ASystem.qLearningBuffer;
import edu.malone.edwards.admea.ehache.nodeUtils.Node;
import edu.malone.edwards.admea.ehache.nodeUtils.Nodes;
import edu.malone.edwards.admea.utils.Debugging;
import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
/**
 * created by: Kunuk Nykjaer &nbsp;&nbsp;&nbsp; website:
 * <a href="https://kunuk.wordpress.com/2010/09/24/q-learning/">Q-learning example with Java</a>
 * <br>
 * edited by: Cory Edwards <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Made all of the variables work with the algorithm <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Added applyPolicy() <br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Got rid of main and debugging methods. <br>
 * <br>
 * references: <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://en.wikipedia.org/wiki/Q-learning">wikipedia Q-learning</a> <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://people.revoledu.com/kardi/tutorial/ReinforcementLearning/Q-Learning.htm">Q-learning tutorial</a>
 */
public class QLearning implements Runnable {
 
    // path finding
    private final double alpha = 0.1;
    private final double gamma = 0.9;
    
    //Rewards will come from the parent score - child score. Those scores are calculated when a Node is initialized.
    //If a Node is not a valid child of of the parent then that value is set to 0.
    private THashMap<String, Integer> R;
    
    //Map of every child Node's Q value coming from every parent.
    private THashMap<String, Double> Q;
    
    private final Debugging debugger = new Debugging();
    
    private final Node node;
    
    public QLearning(Node input)
    {
        node = input;
    }
    
    /**
     * Start the learning process.
     */
    @Override
    public void run() 
    {   
        if(!node.children.isEmpty() && !qLearningBuffer.contains(node.getNodeId()))
        {
            qLearningBuffer.add(node.getNodeId());
            debugger.println("Starting Q learning for node " + node.getNodeId() + ".");
            R = new THashMap(node.children.size(), .9f);
            Q = new THashMap(node.children.size(), .9f);
            Map<String, Node> children = new HashMap();

            //Now go through and reset all of the possible children to an actual value.
            for(String childId : node.children) 
            {
                Node childNode = Nodes.getNode(childId);
                children.put(childId, childNode);
                R.put(childId, node.getScore() - childNode.getScore());
                Q.put(childId, 0.0);
            };
            
            for(int i = 0; i < 300; i++)
            {
                node.children.forEach((childId) -> 
                {
                    //Get all of the variables needed.
                    double q = Q(childId);
                    double maxQ = maxQ(children.get(childId));
                    int r = R(childId);

                    // Q(s,a)= Q(s,a) + alpha * (R(s,a) + gamma * Max(next id, all actions) - Q(s,a))
                    double value = q + alpha * (r + gamma * maxQ - q);
                    setQ(childId, value);
                });
            }

            node.givePolicy(Q);
            Nodes.saveNode(node);
            debugger.println("Q learning done for node " + node.getNodeId() + ".");
        }
    }
 
    //s is a Node. You will get and iterate over all its children to find the one
    //with the highest value.
    private double maxQ(Node actionsFromState) 
    {
        double maxValue = Double.MIN_VALUE;
        for(Double value : actionsFromState.getPolicy().values()) 
        {
            if(value != null)
                if (value > maxValue)
                    maxValue = value;
        }
        return maxValue;
    }
    
    //a = child Id
    private double Q(String a)
    {
        return Q.get(a);
    }
 
    //s = parent Id
    //a = child Id
    //value = setting value
    private void setQ(String a, double value)
    {
        Q.put(a, value);
    }
 
    //s = parent Id
    //a = child Id
    private int R(String a) 
    {
        return R.get(a);
    }
}