package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provisioning")
@RequiredArgsConstructor
public class ProvisioningController {
    private final ProvisioningService provisioningService;

    @GetMapping("/{macAddress}")
    @ResponseStatus(HttpStatus.OK)
    public String getProvisioning(@PathVariable String macAddress) {
        return provisioningService.getProvisioningFile(macAddress);
    }

}