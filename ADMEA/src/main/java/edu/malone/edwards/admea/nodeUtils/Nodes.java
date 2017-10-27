/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.nodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Holds all Nodes in the application, and can create, get, and work with all
 * of them.
 * 
 * @author Cory Edwards
 */
public class Nodes<K extends State> {
    
    private int n = 0;
    
    /**
     * @return The number of Nodes in the system.
     */
    public int numberOfNodes()
    {
        return n;
    }
    
    /**
     * Get the node with the given state. If a Node does not exist with that
     * state, then a new node will be made.
     * @param state The state to check against.
     * @param states The method created by the process to see if 2 states are equal.
     * @return The Node with that state.
     */
    public Node getNode(K state)
    {
        Node node = getNode(DigestUtils.shaHex(state.toString()));
        if(node == null)
            node = createNode(state);
        return node;
    }
    
    /**
     * Will get the Node with the given Id.
     * @param id The id of the wanted Node. Cannot be null.
     * @return The Node with the given Id.
     */
    public static Node getNode(String id)
    {
        return get(id);
    }
    
    /**
     * Create a new Node with a unique Id and the given state.
     * @param state The state to give the new Node.
     */
    private synchronized Node createNode(K state)
    {
        //Give the Node a new incremented Id.
        String newId = DigestUtils.shaHex(state.toString());
        Node node = new Node(state, newId);
        
        //Create the new Node and put it into the list.
        put(newId, node);
        
        n++;
        
        //Return the new Node.
        return node;
    }
    
    /**
     * Take all of the possible children for the node and get rid of any children that would cause a loop.
     * @param children A list of children from the parent.
     * @param parent The parent node ID.
     * @return A list of all good children.
     */
    public String[] deleteLoops(String[] children, String parent)
    {
        ArrayList<String> tree = new ArrayList();
        ArrayList<String> goodEdges = new ArrayList();
        
        for(String child : children)
        {
            //Add the child to the temp tree and list of good edges.
            tree.add(child);
            goodEdges.add(child);
            
            String[] treeNodes = tree.toArray(new String[0]);
            for(String n : treeNodes)
            {
                //If n == parent, then we hit a loop and are right back where we started.
                if(n.equals(parent))
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
        
        String[] goodChildren = new String[goodEdges.size()];
        for(int i = 0; i < goodChildren.length; i++)
            goodChildren[i] = goodEdges.get(i);
        
        return goodChildren;
    }
    
    public void init()
    {
        
    }
    
    public void close()
    {
        
    }
}
