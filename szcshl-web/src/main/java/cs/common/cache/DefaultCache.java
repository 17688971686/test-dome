package cs.common.cache;

import org.ehcache.Cache;

/**
 * @author lqs
 * the default chace product,use ehcache implements
 * */
public class DefaultCache implements ICache{

	
	private Cache<String, Object> cache;
	
	private DefaultCache(){}
	
	public DefaultCache(Cache<String, Object> cache){
		this.cache = cache;
	}
	
	@Override
	public void put(String key, Object value) {
		cache.put(key, value);
		
	}

	@Override
	public Object get(String key) {
		
		return cache.get(key);
	}

	@Override
	public void clear(String key) {
		cache.remove(key);
		
	}

	@Override
	public void clearAll() {
		cache.clear();
		
	}

}
