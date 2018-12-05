package cs.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import cs.common.utils.StringUtil;
import cs.xss.XssShieldUtil;
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
        byte[] bytes = text.getBytes(getCharset());
        out.write(bytes);
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        mapping.put(java.util.Date.class, new SimpleDateFormatSerializer(defaultDateFormat));
    }
}
