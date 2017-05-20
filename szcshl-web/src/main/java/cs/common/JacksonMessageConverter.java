package cs.common;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Deprecated
public class JacksonMessageConverter extends FastJsonHttpMessageConverter{

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		JSON.DEFFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
		super.writeInternal(obj, outputMessage);
	}

}
