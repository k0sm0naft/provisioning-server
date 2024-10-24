package com.voxloud.provisioning.service.config;

import com.voxloud.provisioning.entity.Device;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DeskConfigGenerator extends AbstractConfigGenerator {
    private static final String LINE_BREAK = "\n";
    private static final String EQUALS = "=";
    private static final int KAY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    @Override
    public Device.DeviceModel getDeviceModel() {
        return Device.DeviceModel.DESK;
    }

    @Override
    protected Map<String, Object> parseOverrideFragment(String overrideFragment) {
        return Arrays.stream(overrideFragment.split(LINE_BREAK))
                     .map(line -> line.split(EQUALS))
                     .collect(Collectors.toMap(
                             keyValue -> keyValue[KAY_INDEX],
                             keyValue -> keyValue[VALUE_INDEX]
                     ));
    }

    @Override
    protected String formatConfig(Map<String, Object> configMap) {
        return configMap.entrySet().stream()
                        .map(entry -> entry.getKey() + EQUALS + entry.getValue())
                        .collect(Collectors.joining(LINE_BREAK));
    }
}
