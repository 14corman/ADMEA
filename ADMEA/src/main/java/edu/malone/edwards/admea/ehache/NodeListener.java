/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache;

import static edu.malone.edwards.admea.ehache.ASystem.nodeList;
import static edu.malone.edwards.admea.ehache.ASystem.qLearningBuffer;
import edu.malone.edwards.admea.ehache.nodeUtils.Node;
import edu.malone.edwards.admea.ehache.nodeUtils.Nodes;
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
        //        System.out.println("Event fired: " + ce.getType());
                switch(ce.getType().toString())
                {
                    case "EVICTED":
                    case "EXPIRED":
                        try {
                            String key = ce.getKey();
                            nodeList.delete(key);
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
                        Node node = ce.getNewValue();
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
