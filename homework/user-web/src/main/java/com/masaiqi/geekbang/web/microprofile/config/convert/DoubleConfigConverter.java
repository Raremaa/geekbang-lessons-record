package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:18 PM
 */
public class DoubleConfigConverter implements Converter<Double> {
    @Override
    public Double convert(String value) throws IllegalArgumentException, NullPointerException {
        return Double.valueOf(value);
    }
}
