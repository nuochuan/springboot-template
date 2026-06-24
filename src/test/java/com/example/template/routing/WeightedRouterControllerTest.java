package com.example.template.routing;

import com.example.template.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeightedRouterController.class)
@Import(GlobalExceptionHandler.class)
class WeightedRouterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeightedRouterService weightedRouterService;

    @Test
    void shouldReturnSelectedNode() throws Exception {
        when(weightedRouterService.route(anyList())).thenReturn(new Node("B", 3, true));

        mockMvc.perform(post("/api/router/route")
                        .contentType("application/json")
                        .content("[{\"name\":\"A\",\"weight\":5,\"enabled\":true},{\"name\":\"B\",\"weight\":3,\"enabled\":true}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("B"))
                .andExpect(jsonPath("$.data.weight").value(3))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    void shouldReturnBadRequestWhenNoAvailableNode() throws Exception {
        when(weightedRouterService.route(anyList())).thenThrow(new IllegalStateException("No available node"));

        mockMvc.perform(post("/api/router/route")
                        .contentType("application/json")
                        .content("[{\"name\":\"A\",\"weight\":0,\"enabled\":true}]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("No available node"));
    }
}
