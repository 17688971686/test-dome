package com.sn.framework.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sn.framework.common.StringUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Description: JSON工具类（jackson的实现）
 *
 * @author: tzg
 * @date: 2017/12/20 19:58
 */
public final class JacksonUtils {

    public final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String df = AppYmlUtils.getProperty("spring.jackson.date-format");
        if (StringUtil.isNotBlank(df)) {
            SimpleDateFormat sdf = new SimpleDateFormat(df);
            objectMapper.setDateFormat(sdf);
        }
        String tz = AppYmlUtils.getProperty("spring.jackson.time-zone");
        if (StringUtil.isNotBlank(tz)) {
            TimeZone timeZone = TimeZone.getTimeZone(tz);
            objectMapper.setTimeZone(timeZone);
        }
    }

    /**
     * 对象转为json字符串
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String objectToString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json字符串转为对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T stringToEntity(String json, Class<T> cls) throws IOException {
        return objectMapper.readValue(json, cls);
    }

    /**
     * json字符串转为数组集合
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> stringToList(String json, Class<T> cls) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, cls);
        return objectMapper.readValue(json, javaType);
    }

    /**
     * json字符串转为Map
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static Map stringToMap(String json) throws IOException {
        return stringToEntity(json, Map.class);
    }

    /**
     * 读取json字节转换为json对象
     * @param bytes
     * @return
     * @throws IOException
     */
    public static JsonNode readTree(byte[] bytes) throws IOException {
        return objectMapper.readTree(bytes);
    }

}