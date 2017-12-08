/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.nodeUtils;

import gnu.trove.map.hash.THashMap;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.xml.XmlConfiguration;
import org.openide.util.Utilities;

/**
 * Holds all Nodes in the application, and can create, get, and work with all
 * of them.
 * 
 * @author Cory Edwards
 */
public class Nodes implements CacheLoaderWriter<String, Node>{
    
    private PersistentCacheManager persistentCacheManager;
    private static Cache<String, Node> nodes;
    private THashMap<String, Node> cache = new THashMap();
    private static Map<String, Node> nodeBuffer = new THashMap();
    public int n = 0;
    
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
    public Node getNode(State state)
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
        return nodes.get(id);
    }
    
    /**
     * Create a new Node with a unique Id and the given state.
     * @param state The state to give the new Node.
     */
    private Node createNode(State state)
    {
        //Give the Node a new incremented Id.
        String newId = DigestUtils.shaHex(state.toString());
        Node node = new Node(state, newId);
        
        //Create the new Node and put it into the list.
//        nodes.put(newId, node);
        nodeBuffer.put(newId, node);
        
        //Return the new Node.
        return node;
    }
    
    public static void saveNode(Node node)
    {
        nodes.put(node.getNodeId(), node);
    }
    
    public static void flushBuffer()
    {
        nodes.putAll(nodeBuffer);
        nodeBuffer.clear();
    }
    
    /**
     * Take all of the possible children for the node and get rid of any children that would cause a loop.
     * @param children A list of children from the parent.
     * @param parent The parent node ID.
     * @return A list of all good children.
     */
    public Set<String> deleteLoops(Set<String> children, String parent)
    {
        Set<String> tree = new HashSet();
        Set<String> goodEdges = new HashSet();
        
        for(String child : children)
        {
            //Add the child to the temp tree and list of good edges.
            tree.add(child);
            goodEdges.add(child);
            
            String[] treeNodes = tree.toArray(new String[0]);
            for(String treeNode : treeNodes)
            {
                //If n == parent, then we hit a loop and are right back where we started.
                if(treeNode.equals(parent))
                {
                    //That means adding this child as an edge would form a loop, so delete the edge.
                    goodEdges.remove(child);
                    break;
                }
                
                //Add all of the current nodes children to tree to be able to continue down the path.
                if(!nodes.get(treeNode).children.isEmpty())
                    tree.addAll(nodes.get(treeNode).children);
                
                tree.remove(treeNode);
            }
            
            //Because break could be called and we need a clean tree in the next loop.
            tree.clear();
        }
        
        return goodEdges;
    }
    
    public void init() throws MalformedURLException
    {
//        CachingProvider cachingProvider = Caching.getCachingProvider();
//        persistentCacheManager = cachingProvider.getCacheManager( 
//            Utilities.toURI(new File("C:\\NSF\\GameOfLifeWebsite\\cache.xml")), 
//            getClass().getClassLoader()); 
        URL myUrl = Utilities.toURI(new File("C:\\NSF\\ADMEA\\ADMEA\\cache.xml")).toURL(); 
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        persistentCacheManager = (PersistentCacheManager) CacheManagerBuilder.newCacheManager(xmlConfig);
        persistentCacheManager.init();
        nodes = persistentCacheManager.getCache("default", String.class, Node.class);
    }
    
    public void close()
    {
        if(persistentCacheManager != null)
            persistentCacheManager.close();
    }

    @Override
    public  Node load(String k) throws Exception {
        return cache.get(k);
    }

    @Override
    public Map<String, Node> loadAll(Iterable<? extends String> itrbl) throws BulkCacheLoadingException, Exception {
        Map<String, Node> map = new THashMap();
        Iterator<? extends String> it = itrbl.iterator();
        while(it.hasNext())
        {
            String key = it.next();
            map.put(key, cache.get(key));
        }
        return map;
    }

    @Override
    public void write(String k, Node v) throws Exception {
        cache.put(k, v);
    }

    @Override
    public void writeAll(Iterable<? extends Map.Entry<? extends String, ? extends Node>> itrbl) throws BulkCacheWritingException, Exception {
        for (Map.Entry<? extends String, ? extends Node> entry: itrbl)
            cache.put(entry.getKey(), entry.getValue());
    }

    @Override
    public void delete(String k) throws Exception {
        cache.remove(k);
    }

    @Override
    public void deleteAll(Iterable<? extends String> itrbl) throws BulkCacheWritingException, Exception {
        Iterator<? extends String> it = itrbl.iterator();
        while(it.hasNext())
            cache.remove(it.next());
    }
}
