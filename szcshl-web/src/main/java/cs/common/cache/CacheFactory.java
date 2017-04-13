package cs.common.cache;

/**
 * @author lqs
 * 缓存工厂类，用于产生缓存对象
 * 
 * */
public class CacheFactory {

	private static ICache cache = new DefaultCacheImpl();
	
	public static ICache getCache(){
		return cache;
	}
}
