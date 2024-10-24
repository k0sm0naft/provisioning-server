package com.voxloud.provisioning.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.exception.UnsupportedDeviceTypeException;
import com.voxloud.provisioning.repository.DeviceRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProvisioningServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ConfigGenerator configGenerator;

    private ProvisioningService provisioningService;

    @Before
    public void setUp() {
        List<ConfigGenerator> generators = Collections.singletonList(configGenerator);
        when(configGenerator.getDeviceModel()).thenReturn(Device.DeviceModel.DESK);
        provisioningService = new ProvisioningServiceImpl(deviceRepository, generators);
    }

    @Test
    public void testGetProvisioningFile_Success() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        String expected = "config data";

        Device device = new Device();
        device.setModel(Device.DeviceModel.DESK);

        when(deviceRepository.findByMacAddress(anyString())).thenReturn(Optional.of(device));
        when(configGenerator.generateConfig(device)).thenReturn(expected);

        String actual = provisioningService.getProvisioningFile(macAddress);

        assertEquals(expected, actual);
    }


    @Test
    public void testGetProvisioningFile_InvalidMacAddress_ShouldThrowException() {
        String invalidMacAddress = "invalid-mac";
        String expected = "Invalid MAC address format";

        Exception actual = assertThrows(IllegalArgumentException.class,
                () -> provisioningService.getProvisioningFile(invalidMacAddress));

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void testGetProvisioningFile_DeviceNotFound_ShouldThrowException() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        String expected = "Device with MAC " + macAddress + " not found";

        Exception actual = assertThrows(DeviceNotFoundException.class,
                () -> provisioningService.getProvisioningFile(macAddress));

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void testGetProvisioningFile_UnsupportedDeviceType_ShouldThrowException() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        Device unsupportedDevice = new Device();
        unsupportedDevice.setModel(Device.DeviceModel.CONFERENCE);
        String expected = "Unsupported device type: " + unsupportedDevice.getModel();

        when(deviceRepository.findByMacAddress(anyString()))
                .thenReturn(Optional.of(unsupportedDevice));

        Exception actual = assertThrows(UnsupportedDeviceTypeException.class,
                () -> provisioningService.getProvisioningFile(macAddress));

        assertEquals(expected, actual.getMessage());
    }
}
