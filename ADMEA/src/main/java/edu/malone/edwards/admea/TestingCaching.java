/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea;

import java.io.File;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

/**
 *
 * @author Cory Edwards
 */
public class TestingCaching {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File jarPath = new File(TestingCaching.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath = jarPath.getParentFile().getAbsolutePath();
        try(PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .with(CacheManagerBuilder.persistence(new File(propertiesPath, "NodeStore"))) 
            .withCache("threeTieredCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .heap(10, EntryUnit.ENTRIES) 
                        .offheap(1, MemoryUnit.MB) 
                        .disk(20, MemoryUnit.MB, true) 
                    )
            ).build(true))
        {

            Cache<Long, String> threeTieredCache = persistentCacheManager.getCache("threeTieredCache", Long.class, String.class);
            threeTieredCache.put(1L, "stillAvailableAfterRestart"); 
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
