package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:12 PM
 */
public class ByteConfigConverter implements Converter<Byte> {
    @Override
    public Byte convert(String value) throws IllegalArgumentException, NullPointerException {
        return Byte.valueOf(value);
    }
}
