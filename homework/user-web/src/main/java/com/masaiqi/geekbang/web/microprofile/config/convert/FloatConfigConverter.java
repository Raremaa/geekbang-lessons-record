package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:17 PM
 */
public class FloatConfigConverter implements Converter<Float> {
    @Override
    public Float convert(String value) throws IllegalArgumentException, NullPointerException {
        return Float.valueOf(value);
    }
}
