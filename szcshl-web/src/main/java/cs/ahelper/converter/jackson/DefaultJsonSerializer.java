package cs.ahelper.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import cs.xss.XssShieldUtil;
import java.io.IOException;

/**
 * String类型的json序列化处理器
 */
public class DefaultJsonSerializer extends StdSerializer<String> {

  public DefaultJsonSerializer() {
    this(null);
  }

  public DefaultJsonSerializer(Class<String> t) {
    super(t);
  }

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    // xss策略再此执行
    String safe = XssShieldUtil.getInstance().stripXss(value);
    gen.writeString(safe);
  }
}
