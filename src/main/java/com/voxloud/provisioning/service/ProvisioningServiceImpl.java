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
    private static final String MAC_ADDRESS_PATTERN =
            "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$|^([0-9A-Fa-f]{12})$";

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
        if (!macAddress.matches(MAC_ADDRESS_PATTERN)) {
            throw new IllegalArgumentException("Invalid MAC address format");
        }

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
