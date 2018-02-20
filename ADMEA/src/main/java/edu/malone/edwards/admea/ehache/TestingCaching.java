/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache;

import java.io.File;
import java.net.URL;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.xml.XmlConfiguration;

/**
 *
 * @author Cory Edwards
 */
public class TestingCaching {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        URL myUrl = TestingCaching.class.getClassLoader().getResource("cache.xml"); 
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        try(PersistentCacheManager persistentCacheManager = (PersistentCacheManager) CacheManagerBuilder.newCacheManager(xmlConfig))
        {
            persistentCacheManager.init();
            Cache<Long, String> threeTieredCache = persistentCacheManager.getCache("default", Long.class, String.class);
            threeTieredCache.put(1L, "stillAvailableAfterRestart"); 
//            System.out.println(threeTieredCache.get(1L));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
