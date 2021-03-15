package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:47 PM
 */
public class StringConfigConverter implements Converter<String> {
    @Override
    public String convert(String value) throws IllegalArgumentException, NullPointerException {
        return value;
    }
}
