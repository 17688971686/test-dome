package com.sn.framework.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sn.framework.core.common.StrUtils;

import java.io.IOException;

/**
 * Created by ldm on 2018/11/26 0026.
 */
public class StringXssDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String source = p.getText().trim();
        // 把字符串做XSS过滤
        //return StringEscapeUtils.escapeHtml3(source);
        return StrUtils.cleanXSS(source);
    }
}

