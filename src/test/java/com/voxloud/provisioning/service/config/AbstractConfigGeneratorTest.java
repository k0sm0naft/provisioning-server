package com.voxloud.provisioning.service.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.voxloud.provisioning.entity.Device;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class AbstractConfigGeneratorTest {
    private static final String TEST_DOMAIN = "test.domain";
    private static final String PORT = "8080";
    private static final String CODECS = "G711,G729";

    @Mock
    private Environment environment;

    @InjectMocks
    private AbstractConfigGeneratorTestImpl abstractConfigGenerator;

    @Before
    public void setUp() throws Exception {
        when(environment.getProperty("provisioning.domain")).thenReturn(TEST_DOMAIN);
        when(environment.getProperty("provisioning.port")).thenReturn(PORT);
        when(environment.getProperty("provisioning.codecs")).thenReturn(CODECS);

        ReflectionTestUtils.setField(abstractConfigGenerator, "environment", environment);

        Method initMethod = AbstractConfigGenerator.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(abstractConfigGenerator);
    }

    @Test
    public void testInit_LoadsDefaultConfig() throws Exception {
        Field defaultConfigField = AbstractConfigGenerator.class.getDeclaredField("defaultConfig");
        defaultConfigField.setAccessible(true);
        Map<String, String> defaultConfig =
                (Map<String, String>) defaultConfigField.get(abstractConfigGenerator);

        assertEquals(TEST_DOMAIN, defaultConfig.get("domain"));
        assertEquals(PORT, defaultConfig.get("port"));
        assertEquals(CODECS, defaultConfig.get("codecs"));
    }

    @Test
    public void testGenerateConfigMap_OverrideFragment() {
        String username = "user";
        String password = "pass";
        String customKey = "customKey";
        String customValue = "customValue";

        Device device = new Device();
        device.setUsername(username);
        device.setPassword(password);
        device.setOverrideFragment("{\"" + customKey + "\":\"" + customValue + "\"}");

        Map<String, Object> configMap = abstractConfigGenerator.generateConfigMap(device);

        assertEquals(username, configMap.get("username"));
        assertEquals(password, configMap.get("password"));
        assertEquals(customValue, configMap.get(customKey));
    }

    private static class AbstractConfigGeneratorTestImpl extends AbstractConfigGenerator {
        @Override
        public Device.DeviceModel getDeviceModel() {
            return Device.DeviceModel.DESK;
        }

        @Override
        protected Map<String, Object> parseOverrideFragment(String overrideFragment) {
            return Collections.singletonMap("customKey", "customValue");
        }

        @Override
        protected String formatConfig(Map<String, Object> configMap) {
            return "formattedConfig";
        }
    }
}
