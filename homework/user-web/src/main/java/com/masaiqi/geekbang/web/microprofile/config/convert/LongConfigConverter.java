package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:23 PM
 */
public class LongConfigConverter implements Converter<Long> {
    @Override
    public Long convert(String value) throws IllegalArgumentException, NullPointerException {
        return Long.valueOf(value);
    }
}
