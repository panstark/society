package com.pan.society.Common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义的JSON对象转换类。
 *
 * @author wangruiv
 * @date 2017-08-21 14:49:20
 */
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        super();

        // 反序列化时，允许单引号
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 反序列化时，允许字段名没有双引号
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 反序列化时，忽略未知的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 允许控制字符
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
}
