package cs.common.cache;

import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author lqs
 * the default cache factory to marke ehcache
 * */
public class DefaultCacheFactory implements CacheFactory{

	
	public DefaultCacheFactory(){
		
	}
	/**
	 * 
	 * return a ehcache object
	 * */
	@Override
	public ICache getCache() {
		
		org.ehcache.CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
		/*.withCache("preConfigured",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
		        ResourcePoolsBuilder.heap(100)).build()).build(true);*/

		//Cache<String, Object> preConfigured= cacheManager.getCache("preConfigured", String.class, Object.class);

		Cache<String, Object> myCache = cacheManager.createCache("myCache",
		          CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
		                                        ResourcePoolsBuilder.heap(100)).build());
		      
		
		DefaultCache defaultCache = new DefaultCache(myCache);
		
		return defaultCache;
	}


}
