/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.qlearning;

import edu.malone.edwards.admea.nodeUtils.Node;
import gnu.trove.map.hash.THashMap;

/**
 *
 * @author Cory Edwards
 */
public class QLearningList {
    
    private static final THashMap<Node, Object> Q_LIST = new THashMap();
    
    private static final Object DUMMY = new Object();
    
    protected static synchronized boolean checkList(Node node)
    {
        if(Q_LIST.contains(node))
            return true;
        else
        {
            Q_LIST.put(node, DUMMY);
            return false;
        }
    }
    
    protected static synchronized void remove(Node node)
    {
        Q_LIST.remove(node);
    }
    
    public static synchronized void clear()
    {
        Q_LIST.clear();
    }
    
}
