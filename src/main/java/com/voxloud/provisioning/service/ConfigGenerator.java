package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;

public interface ConfigGenerator {
    Device.DeviceModel getDeviceModel();

    String generateConfig(Device device);
}
