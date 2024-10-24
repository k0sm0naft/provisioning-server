package com.voxloud.provisioning.service.config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.voxloud.provisioning.entity.Device;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class DeskConfigGeneratorTest {
    @Mock
    private Environment environment;

    @InjectMocks
    private DeskConfigGenerator deskConfigGenerator;

    @Before
    public void setUp() throws Exception {
        when(environment.getProperty("provisioning.domain")).thenReturn("test.domain");
        when(environment.getProperty("provisioning.port")).thenReturn("8080");
        when(environment.getProperty("provisioning.codecs")).thenReturn("G711,G729");

        ReflectionTestUtils.setField(deskConfigGenerator, "environment", environment);

        Method initMethod = AbstractConfigGenerator.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(deskConfigGenerator);
    }

    @Test
    public void testGenerateConfig_ValidDevice() {
        Device device = new Device();
        device.setUsername("user");
        device.setPassword("pass");
        device.setOverrideFragment("key1=value1\nkey2=value2");

        String result = deskConfigGenerator.generateConfig(device);

        String expected1 = "username=user";
        String expected2 = "password=pass";
        String expected3 = "key1=value1";
        String expected4 = "key2=value2";
        String expected5 = "domain=test.domain";
        String expected6 = "port=8080";
        String expected7 = "codecs=G711,G729";

        assertTrue(result.contains(expected1));
        assertTrue(result.contains(expected2));
        assertTrue(result.contains(expected3));
        assertTrue(result.contains(expected4));
        assertTrue(result.contains(expected5));
        assertTrue(result.contains(expected6));
        assertTrue(result.contains(expected7));
    }
}
