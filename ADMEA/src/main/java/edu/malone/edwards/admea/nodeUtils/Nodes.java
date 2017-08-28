/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.nodeUtils;

import gnu.trove.map.hash.THashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Holds all Nodes in the application, and can create, get, and work with all
 * of them.
 * 
 * @author Cory Edwards
 */
public class Nodes extends THashMap<Long, Node> implements Serializable {
    
    //To keep track of which state has which Id.
    private final THashMap<State, Long> stateToId = new THashMap();
    
    /**
     * @return The number of Nodes in the system.
     */
    public int numberOfNodes()
    {
        return size();
    }
    
    /**
     * Get the node with the given state. If a Node does not exist with that
     * state, then a new node will be made.
     * @param state The state to check against.
     * @param states The method created by the process to see if 2 states are equal.
     * @return The Node with that state.
     */
    public Node getNode(State<?> state)
    {
        if(stateToId.containsKey(state))
        {
            return get(stateToId.get(state));
        }
        else
        {
            //If the Node is not in the list then create it and return it.
            return get(createNode(state));
        }
    }
    
    /**
     * Will get the Node with the given Id.
     * @param id The id of the wanted Node. Cannot be null.
     * @return The Node with the given Id.
     */
    public Node getNode(long id)
    {
        return get(id);
    }
    
    /**
     * Create a new Node with a unique Id and the given state.
     * @param state The state to give the new Node.
     */
    private synchronized long createNode(State<?> state)
    {
        //Give the Node a new incremented Id.
        long newId = size();
        
        //Create the new Node and put it into the list.
        put(newId, new Node(state, newId));
        
        //Add the state Id pair.
        stateToId.put(state, newId);
        
        //Return the new Node's Id.
        return newId;
    }
    
    /**
     * Take all of the possible children for the node and get rid of any children that would cause a loop.
     * @param children A list of children from the parent.
     * @param parent The parent node ID.
     * @return A list of all good children.
     */
    public long[] deleteLoops(long[] children, long parent)
    {
        ArrayList<Long> tree = new ArrayList();
        ArrayList<Long> goodEdges = new ArrayList();
        
        for(long child : children)
        {
            //Add the child to the temp tree and list of good edges.
            tree.add(child);
            goodEdges.add(child);
            
            for(Long n : tree)
            {
                //If n == parent, then we hit a loop and are right back where we started.
                if(n == parent)
                {
                    //That means adding this child as an edge would form a loop, so delete the edge.
                    goodEdges.remove(child);
                    break;
                }
                
                //Add all of the current nodes children to tree to be able to continue down the path.
                if(get(n).children.length != 0)
                    tree.addAll(Arrays.stream(get(n).children).boxed().collect(Collectors.toSet()));
                
                tree.remove(n);
            }
            
            //Because break could be called and we need a clean tree in the next loop.
            tree.clear();
        }
        
        long[] goodChildren = new long[goodEdges.size()];
        for(int i = 0; i < goodChildren.length; i++)
            goodChildren[i] = goodEdges.get(i);
        
        return goodChildren;
    }
}
