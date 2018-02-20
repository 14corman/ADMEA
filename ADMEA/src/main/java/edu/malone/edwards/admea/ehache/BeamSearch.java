/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache;

import edu.malone.edwards.admea.ehache.nodeUtils.Node;
import edu.malone.edwards.admea.ehache.nodeUtils.Nodes;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Cory
 */
public class BeamSearch {
    
    private final int B;
    private final ArrayList<Node>[] list;
    private boolean doIteration = true;
    
    public BeamSearch(int b)
    {
        B = b;
        list = new ArrayList[B];
    }
    
    public void buildList(Node root)
    {
        Set<String> children = root.children;
        ArrayList<Node> nodes = new ArrayList();
        for(String child : children)
            nodes.add(Nodes.getNode(child));
        
        list[0] = nodes;
                
        while(doIteration)
        {
            for(int i = 0; i < B; i++)
            {
                
            }
        }
    }
    
    private void getIteration()
    {
        //Get all children
        
        //
    }
    
}

class Beam
{
    
}
