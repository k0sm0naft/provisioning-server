package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.exception.UnsupportedDeviceTypeException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.utils.MacAddressUtils;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {
    private final DeviceRepository deviceRepository;
    private final Map<Device.DeviceModel, ConfigGenerator> configGenerators;

    public ProvisioningServiceImpl(DeviceRepository deviceRepository,
            List<ConfigGenerator> generators) {
        this.deviceRepository = deviceRepository;
        this.configGenerators =
                generators.stream()
                          .collect(Collectors
                                  .toMap(ConfigGenerator::getDeviceModel, Function.identity()));
    }

    @Override
    public String getProvisioningFile(String macAddress) {
        String normalizedMacAddress = MacAddressUtils.normalize(macAddress);

        Device device =
                deviceRepository.findByMacAddress(normalizedMacAddress)
                                .orElseThrow(() -> new DeviceNotFoundException(
                                        "Device with MAC " + macAddress + " not found"));

        ConfigGenerator generator = configGenerators.get(device.getModel());

        if (generator == null) {
            throw new UnsupportedDeviceTypeException(
                    "Unsupported device type: " + device.getModel());
        }

        return generator.generateConfig(device);
    }
}
