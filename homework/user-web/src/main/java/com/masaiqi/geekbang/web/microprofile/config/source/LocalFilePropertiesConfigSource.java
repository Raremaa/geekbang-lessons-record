package com.masaiqi.geekbang.web.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 本地文件配置源
 *
 * @author masaiqi
 * @date 2021/3/15 5:07 PM
 */
public class LocalFilePropertiesConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public LocalFilePropertiesConfigSource() {
        Properties localProperties = new Properties();

        try (
                InputStream inputStream =this.getClass().getClassLoader().getResourceAsStream("META-INF/local.properties");
                BufferedInputStream localFileBufferedInputStream = new BufferedInputStream(inputStream);
        ) {
            localProperties.load(localFileBufferedInputStream);

            // HashTable convert to HashMap for Better Concurrent-Read Performance
            properties = new HashMap(localProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String s) {
        return properties.get(s);
    }

    @Override
    public String getName() {
        return "Local File Properties";
    }
}
