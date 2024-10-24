package com.voxloud.provisioning.service.config;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.service.ConfigGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class AbstractConfigGenerator implements ConfigGenerator {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private Environment environment;
    private Map<String, String> defaultConfig;


    @Override
    public String generateConfig(Device device) {
        Map<String, Object> configMap = generateConfigMap(device);
        return formatConfig(configMap);
    }

    protected Map<String, Object> generateConfigMap(Device device) {
        Map<String, Object> configMap = getDefaultConfigMap(device);

        if (device.getOverrideFragment() != null) {
            Map<String, Object> overrideMap = parseOverrideFragment(device.getOverrideFragment());
            configMap.putAll(overrideMap);
        }

        return configMap;
    }

    protected abstract Map<String, Object> parseOverrideFragment(String overrideFragment);

    protected abstract String formatConfig(Map<String, Object> configMap);

    private Map<String, Object> getDefaultConfigMap(Device device) {
        Map<String, Object> configMap = new HashMap<>();

        configMap.put(USERNAME, device.getUsername());
        configMap.put(PASSWORD, device.getPassword());

        configMap.putAll(defaultConfig);

        return configMap;
    }

    @PostConstruct
    private void init() {
        defaultConfig = Arrays.stream(Property.values())
                              .collect(Collectors.toMap(
                                      Property::getName,
                                      property -> environment.getProperty(property.getKey()))
                              );
    }
}
