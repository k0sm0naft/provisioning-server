package com.voxloud.provisioning.service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.FormatConfigException;
import com.voxloud.provisioning.exception.ParseOverrideFragmentException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConferenceConfigGenerator extends AbstractConfigGenerator {
    private static final String COMMA = ",";

    private final ObjectMapper objectMapper;

    @Override
    public Device.DeviceModel getDeviceModel() {
        return Device.DeviceModel.CONFERENCE;
    }

    @Override
    public Map<String, Object> parseOverrideFragment(String overrideFragment) {
        try {
            return objectMapper.readValue(overrideFragment, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new ParseOverrideFragmentException("Error parsing override fragment", e);
        }
    }

    @Override
    public String formatConfig(Map<String, Object> configMap) {
        configMap.forEach((key, value) -> {
            if (value instanceof String && ((String) value).contains(COMMA)) {
                configMap.put(key, ((String) value).split(COMMA));
            }
        });

        try {
            return objectMapper.writeValueAsString(configMap);
        } catch (Exception e) {
            throw new FormatConfigException("Error formatting config", e);
        }
    }
}