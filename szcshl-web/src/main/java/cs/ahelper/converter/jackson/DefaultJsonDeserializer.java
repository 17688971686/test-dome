package cs.ahelper.converter.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import cs.xss.XssShieldUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * String类型的json反序列化处理器
 */
public class DefaultJsonDeserializer extends StdDeserializer<String> {

    public DefaultJsonDeserializer() {
        this(null);
    }

    public DefaultJsonDeserializer(Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (StringUtils.isEmpty(value)) {
            return value;
        } else {
            return XssShieldUtil.getInstance().stripXss(value);
        }
    }
}
