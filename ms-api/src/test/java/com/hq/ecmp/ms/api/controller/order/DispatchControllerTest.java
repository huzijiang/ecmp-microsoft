package com.hq.ecmp.ms.api.controller.order;

import com.hq.ecmp.ms.api.MsApiApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@SpringBootTest(classes = {  MsApiApplication.class})
public class DispatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void companyDepartment() throws Exception {

        System.out.println("companyDepartment");

        mockMvc.perform(post("/dispatch/getWaitSelectedDrivers")
                .header("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImQ5MTRlZDM1LTNiMzctNGM2Ny04Y2QyLWFhMDIyNzRjNjAxMyJ9.715zmWfFV6LgU1FG0DJ35k946OjQ4rHNJfFlRAvqBA4EFz1EijqdzzFGKpDSBElGhLtHLpci2PTKm_V4A4HUfA")
                .header("token","eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImQ5MTRlZDM1LTNiMzctNGM2Ny04Y2QyLWFhMDIyNzRjNjAxMyJ9.715zmWfFV6LgU1FG0DJ35k946OjQ4rHNJfFlRAvqBA4EFz1EijqdzzFGKpDSBElGhLtHLpci2PTKm_V4A4HUfA")
                .param("orderNo", "325")
                .param("carGroupServiceMode", "CA00")
                .param("carGroupSource", "C000")
                .param("itIsSelfDriver", "N111"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}