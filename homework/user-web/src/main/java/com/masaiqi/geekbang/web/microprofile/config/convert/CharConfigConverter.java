package com.masaiqi.geekbang.web.microprofile.config.convert;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author masaiqi
 * @date 2021/3/15 11:19 PM
 */
public class CharConfigConverter implements Converter<Character> {
    @Override
    public Character convert(String value) throws IllegalArgumentException, NullPointerException {
        char[] chars = value.toCharArray();
        if(chars.length > 0) {
            throw new IllegalArgumentException("String length must be 1 for Character");
        }
        return chars[0];
    }
}
