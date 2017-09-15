package cs.service.rtx;

import cs.common.utils.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯通发送消息对象缓冲池
 * Created by ldm on 2017/9/12 0012.
 */
public class RTXSendMsgPool {

    private static final RTXSendMsgPool INSTANCE = new RTXSendMsgPool();

    private RTXSendMsgPool() { }

    public static RTXSendMsgPool getInstance() {
        return INSTANCE;
    }

    private Map<String,Object> cacheMap = new HashMap<String, Object>();

    /**
     * 设置发送对象缓冲池
     * @param key
     * @param variables
     */
    public void sendReceiverPool(String key,Map<String,Object> variables){
        if(Validate.isMap(variables) && variables.get(key) != null){
            this.cacheMap.put(key,variables.get(key));
        }
    }

    /**
     * 设置发送对象缓冲池
     * @param key
     * @param receivers
     */
    public void sendReceiverIdPool(String key,String receivers){
        if(Validate.isString(receivers)){
            this.cacheMap.put(key,receivers);
        }
    }

    /**
     * 获取缓冲池的接收者
     * @param key
     * @return
     */
    public Object getReceiver(String key){
        return this.cacheMap.get(key);
    }

    /**
     * 删除缓冲池数据
     * @param key
     */
    public void removeCache(String key){
        this.cacheMap.remove(key);
    }
}
