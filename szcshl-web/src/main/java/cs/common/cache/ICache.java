package cs.common.cache;

/**
 * @author lqs
 * 系统缓存接口
 * 
 * */
public interface ICache {

	/**
	 * 增加缓存
	 * @param key 键
	 * @param value 值
	 * */
	public void put(String key,Object value);
	
	
	/**
	 * 取得缓存数据
	 * @param key 键
	 * */
	public Object get(String key);
	
	/**
	 * 删除缓存数据
	 * @param key 键
	 * */
	public void clear(String key);
	
	/**
	 * 删除全部缓存
	 * @param key 键
	 * */
	public void clearAll();
}
