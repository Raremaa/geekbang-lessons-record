package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:16 PM
 */
public class ShortConfigConverter implements Converter<Short> {
    @Override
    public Short convert(String value) throws IllegalArgumentException, NullPointerException {
        return Short.valueOf(value);
    }
}
