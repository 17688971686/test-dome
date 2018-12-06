package cs.ahelper.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import cs.ahelper.converter.jackson.DefaultJsonDeserializer;
import cs.ahelper.converter.jackson.DefaultJsonSerializer;
import cs.common.utils.DateUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

/**
 * ObjectMapper工厂类，添加自定义序列化转化器
 */
public class ObjectMapperFactory{

  public static ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

  static {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(String.class, new DefaultJsonDeserializer());
    module.addSerializer(String.class, new DefaultJsonSerializer());
    mapper.registerModule(module);
    mapper.registerModule(new Hibernate5Module());
    SimpleDateFormat smt = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
    mapper.setDateFormat(smt);
  }

  public static ObjectMapper getMapper() {
    return mapper;
  }
}
