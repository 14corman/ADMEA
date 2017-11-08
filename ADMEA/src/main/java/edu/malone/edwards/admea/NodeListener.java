/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea;

import static edu.malone.edwards.admea.ASystem.nodeList;
import static edu.malone.edwards.admea.ASystem.qLearningBuffer;
import edu.malone.edwards.admea.nodeUtils.Node;
import edu.malone.edwards.admea.nodeUtils.Nodes;
import java.util.LinkedList;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.openide.util.Exceptions;

/**
 *
 * @author Cory Edwards
 */
public class NodeListener implements CacheEventListener<String, Node> {

    @Override
    public void onEvent(CacheEvent<? extends String, ? extends Node> ce) {
        new Thread()
        {
            @Override
            public void run()
            {
                Node node = ce.getNewValue();
        //        System.out.println("Event fired: " + ce.getType());
                switch(ce.getType().toString())
                {
                    case "EVICTED":
                    case "EXPIRED":
                        try {
                            if(node != null)
                                nodeList.delete(node.getNodeId());
                        } catch (Exception ex) {
                            Exceptions.printStackTrace(ex);
                        }
                        break;
                    case "CREATED":
        //                System.out.println("IN CREATED");
                        nodeList.n++;
                        break;
                    case "REMOVED":
                        System.out.println("IN REMOVED");
                        nodeList.n--;
                        break;
                    case "UPDATED":
                        if(!qLearningBuffer.contains(node.getNodeId()))
                        {
                            System.out.println("IN UPDATED");
                            for(String parent : node.parents)
                                Nodes.getNode(parent).update();
                        }
                        break;
                }
            }
        }.start();
    }
}
