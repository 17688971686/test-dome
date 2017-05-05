package cs.common.cache;

import org.apache.log4j.Logger;

import cs.service.sys.UserServiceImpl;

/**
 * @author lqs
 * cache manager,use to get a cache implemnts
 * make sure the cache is singleton
 * */
public class CacheManager {

	private static Logger logger = Logger.getLogger(CacheManager.class);
	
	private static ICache cache;
	
	private static CacheFactory cacheFactory;
	
	//use default cache factory
	private static String cacheFactoryDefault = "cs.common.cache.DefaultCacheFactory";
	
	static{
		//search app config for cacheFactory
		//TODO:
		
		//new the CacheFactory from the cacheFactoryDefault
		//cacheFactory class
		Class<?> cacheFactoryClass = null;
		try {
			logger.info("use cache " + cacheFactoryDefault);
			cacheFactoryClass = Class.forName(cacheFactoryDefault);
			cacheFactory = (CacheFactory)cacheFactoryClass.newInstance();
			logger.info("Init CacheFactory success");
		} catch (ClassNotFoundException e) {
			logger.warn("Init cache factory failed", e);
		} catch (InstantiationException e) {
			logger.warn("Init cache factory failed", e);
		} catch (IllegalAccessException e) {
			logger.warn("Init cache factory failed", e);
		}
		//get the cache
		cache = cacheFactory.getCache();
		
	}
	public static ICache getCache(){
		return cache;
	}
}
