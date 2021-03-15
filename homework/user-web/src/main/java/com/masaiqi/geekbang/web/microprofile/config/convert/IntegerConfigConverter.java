package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:17 PM
 */
public class IntegerConfigConverter implements Converter<Integer> {
    @Override
    public Integer convert(String value) throws IllegalArgumentException, NullPointerException {
        return Integer.valueOf(value);
    }
}
