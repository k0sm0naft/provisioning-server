package com.voxloud.provisioning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.initialization-mode=never")
class ProvisioningApplicationTest {
    @Test
    void contextLoads() {
    }
}