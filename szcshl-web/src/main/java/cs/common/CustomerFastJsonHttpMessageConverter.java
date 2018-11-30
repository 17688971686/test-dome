package cs.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import cs.common.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ldm on 2017/5/22 0022.
 */
public class CustomerFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    /**
     * 日志记录器
     **/
    private static Logger LOGGER = Logger.getLogger(CustomerFastJsonHttpMessageConverter.class);

    public static SerializeConfig mapping = new SerializeConfig();

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        String text = JSON.toJSONString(obj, mapping, super.getFeatures());
        // 对文本做HTML特殊字符转义
        String result = convertJson(text);
        byte[] bytes = result.getBytes(getCharset());
        out.write(bytes);
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        mapping.put(java.util.Date.class, new SimpleDateFormatSerializer(defaultDateFormat));
    }

    /**
     * JSON参数转义
     * <功能详细描述>
     * @param json
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String convertJson(String json){
        try{
            // 判断是否是JSON对象
            if (json.startsWith("{")){
                // 将参数转换成JSONObject
                JSONObject jsonObj = JSONObject.parseObject(json);
                // 处理参数
                JSONObject myobj = jsonObj(jsonObj);
                return myobj.toString();
            }
            // 判断是否是JSON数组
            else if (json.startsWith("[")){
                // 将参数转换成JSONArray
                JSONArray jsonArray = JSONArray.parseArray(json);
                //处理参数
                JSONArray array = parseArray(jsonArray);
                return array.toString();
            }
            else
            {
                return json;
            }
        }
        catch (JSONException e){
            LOGGER.error("Json数据解析处理失败！");
            return "{}";
        }
    }

    /**
     * JSON参数Map（对象）转义
     * <功能详细描述>
     * @param json
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    private JSONObject jsonObj(JSONObject json){
        Set<String> keySets = json.keySet();
        for (String key : keySets){
            // 获取对象的值
            Object obj = json.get(key);
            // 判断对象类型
            if (obj instanceof List){
                json.put(key, parseArray((JSONArray)obj));
            }
            // 判断是否是对象结构
            else if (obj instanceof Map){
                // 处理参数
                json.put(key, jsonObj((JSONObject)obj));
            }
            else if (obj instanceof String){
                // 处理参数
                json.put(key, convertStr((String)obj));
            }

        }
        return json;
    }

    /**
     * JSON参数List（数组）转义
     * <功能详细描述>
     * @param jsonArray
     * @return
     * @see [类、类#方法、类#成员]
     */
    private JSONArray parseArray(JSONArray jsonArray){
        // 判空
        if (null == jsonArray || jsonArray.isEmpty() || jsonArray.size() == 0){
            return jsonArray;
        }
        //
        for (int i = 0, l = jsonArray.size(); i < l; i++){
            Object obj = jsonArray.get(i);
            // 判断是否是数据结构
            if (obj instanceof List){
                // 处理数组对象
                parseArray((JSONArray)obj);
            }
            // 判断是否是对象结构
            else if (obj instanceof Map){
                // 处理参数
                jsonObj((JSONObject)obj);
            }
            // 判断是否是String结构
            else if (obj instanceof String){
                jsonArray.set(i, convertStr((String)obj));
            }
        }

        return jsonArray;
    }

    /**
     * HTML脚本转义
     * <功能详细描述>
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String convertStr(String str) {
        return StringUtil.cleanXSS(str);
    }
}
