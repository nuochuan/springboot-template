package com.example.template.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoController.class)
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemoService demoService;

    @Test
    void shouldReturnHelloResponse() throws Exception {
        when(demoService.sayHello(eq("Noah"))).thenReturn(new DemoResponse("Hello, Noah"));

        mockMvc.perform(get("/api/demo/hello").param("name", "Noah"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Hello, Noah"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        when(demoService.sayHello(eq(" "))).thenThrow(new com.example.template.common.BusinessException("name must not be blank"));

        mockMvc.perform(get("/api/demo/hello").param("name", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("name must not be blank"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        when(demoService.sayHello(isNull())).thenThrow(new com.example.template.common.BusinessException("name must not be blank"));

        mockMvc.perform(get("/api/demo/hello"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("name must not be blank"));
    }
}
