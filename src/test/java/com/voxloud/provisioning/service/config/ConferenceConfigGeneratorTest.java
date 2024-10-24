package com.voxloud.provisioning.service.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import java.lang.reflect.Method;
import java.util.HashMap;
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
public class ConferenceConfigGeneratorTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String PASS = "pass";
    private static final String COLUMN = ":";
    private static final String COMMA = ",";
    private static final char QUOTES = '"';

    @Mock
    private Environment environment;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConferenceConfigGenerator conferenceConfigGenerator;


    @Before
    public void setUp() throws Exception {
        when(environment.getProperty("provisioning.domain")).thenReturn("test.domain");
        when(environment.getProperty("provisioning.port")).thenReturn("8080");
        when(environment.getProperty("provisioning.codecs")).thenReturn("G711,G729");

        ReflectionTestUtils.setField(conferenceConfigGenerator, "environment", environment);

        Method initMethod = AbstractConfigGenerator.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(conferenceConfigGenerator);
    }

    @Test
    public void testGenerateConfig_ValidDevice() throws Exception {
        Device device = new Device();
        device.setUsername(USER);
        device.setPassword(PASS);
        String overrideFragment = '{' + QUOTES + KEY + QUOTES + COLUMN + QUOTES + VALUE + QUOTES + '}';
        device.setOverrideFragment(overrideFragment);

        Map<String, Object> configMap = new HashMap<>();
        configMap.put(USERNAME, USER);
        configMap.put(PASSWORD, PASS);
        configMap.put(KEY, VALUE);

        when(objectMapper.readValue(device.getOverrideFragment(), HashMap.class)).thenReturn((HashMap) configMap);
        String expected = '{'
                + QUOTES + USERNAME + QUOTES + COLUMN + QUOTES + USER + QUOTES + COMMA
                + QUOTES + PASSWORD + QUOTES + COLUMN + QUOTES + PASS + QUOTES + COMMA
                + QUOTES + KEY + QUOTES + COLUMN + QUOTES + VALUE + QUOTES
                + '}';

        when(objectMapper.writeValueAsString(anyMap())).thenReturn(expected);

        String result = conferenceConfigGenerator.generateConfig(device);

        assertEquals(expected, result);
    }
}
