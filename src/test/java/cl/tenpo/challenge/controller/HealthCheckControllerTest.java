package cl.tenpo.challenge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cl.tenpo.challenge.repository.jpa.DynamicPercentageCallHistoryRepository;

@WebMvcTest(controllers = HealthCheckController.class, excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DynamicPercentageCallHistoryRepository.class}))
public class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void healthCheck_returnsServiceUpAndRunning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/health"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Service is up and running"));
    }
}