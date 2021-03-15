package com.masaiqi.geekbang.web.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 操作系统环境变量数据源
 *
 * @author masaiqi
 * @date 2021/3/15 5:12 PM
 */
public class OperatingSystemEnvironmentConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public OperatingSystemEnvironmentConfigSource() {
        this.properties = new HashMap<>(System.getenv());
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.properties.keySet();
    }

    @Override
    public String getValue(String s) {
        return this.properties.get(s);
    }

    @Override
    public String getName() {
        return "Operating System Environment";
    }
}
