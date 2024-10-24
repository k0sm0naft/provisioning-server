package com.voxloud.provisioning.service.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Property {
    DOMAIN("domain", "provisioning.domain"),
    PORT("port", "provisioning.port"),
    CODECS("codecs", "provisioning.codecs");

    private final String name;
    private final String key;
}
