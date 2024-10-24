package com.voxloud.provisioning.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProvisioningControllerIntegrationTest {

    private static final String URL_TEMPLATE = "/api/v1/provisioning/{macAddress}";
    private static MockMvc mockMvc;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    public void testGetProvisioningDeskDeviceType_Success() throws Exception {
        String deskMacAddress = "aa-bb-cc-dd-ee-ff";

        mockMvc.perform(get(URL_TEMPLATE, deskMacAddress)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers
                       .content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
               .andExpect(MockMvcResultMatchers
                       .content().string(Matchers.containsString("=")));
    }

    @Test
    public void testGetProvisioningConferenceDeviceType_Success() throws Exception {
        String conferenceMacAddress = "f1-e2-d3-c4-b5-a6";

        mockMvc.perform(get(URL_TEMPLATE, conferenceMacAddress)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers
                       .content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
               .andExpect(MockMvcResultMatchers
                       .content().string(Matchers.containsString("{")))
               .andExpect(MockMvcResultMatchers
                       .content().string(Matchers.containsString("}")));
    }

    @Test
    public void testGetProvisioning_InvalidMacAddress() throws Exception {
        String invalidMacAddress = "invalid-mac";

        mockMvc.perform(get(URL_TEMPLATE, invalidMacAddress)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers
                       .content().string("Invalid MAC address format"));
    }

    @Test
    public void testGetProvisioning_DeviceNotFound() throws Exception {
        String notExistingMacAddress = "00:1A:2B:3C:4D:5E";

        mockMvc.perform(get(URL_TEMPLATE, notExistingMacAddress)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers
                       .content()
                       .string("Device with MAC " + notExistingMacAddress + " not found"));
    }
}