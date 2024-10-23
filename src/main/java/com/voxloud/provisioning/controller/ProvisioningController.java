package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import javax.validation.constraints.Pattern;
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
    private static final String MAC_ADDRESS_PATTERN =
            "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$|^([0-9A-Fa-f]{12})$";

    private final ProvisioningService provisioningService;

    @GetMapping("/{macAddress}")
    @ResponseStatus(HttpStatus.OK)
    public String getProvisioning(
            @PathVariable
            @Pattern(regexp = MAC_ADDRESS_PATTERN,
                    message = "Invalid MAC address format")
            String macAddress
    ) {
        return provisioningService.getProvisioningFile(macAddress);
    }

}