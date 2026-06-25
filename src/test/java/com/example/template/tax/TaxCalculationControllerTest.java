package com.example.template.tax;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSuccessfulTaxCalculationResponse() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":120000,\"annualSpecialDeduction\":12000,\"annualOtherDeduction\":8000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.taxableIncome").value("40000.00"))
                .andExpect(jsonPath("$.data.taxAmount").value("1480.00"))
                .andExpect(jsonPath("$.data.taxRate").value("0.10"))
                .andExpect(jsonPath("$.data.quickDeduction").value("2520.00"));
    }

    @Test
    void shouldReturnBadRequestForEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("request body must not be empty"));
    }

    @Test
    void shouldReturnBadRequestForMissingRequiredField() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSpecialDeduction\":12000,\"annualOtherDeduction\":8000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSalaryIncome is required"));
    }

    @Test
    void shouldReturnBadRequestForNullField() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":120000,\"annualSpecialDeduction\":null,\"annualOtherDeduction\":8000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSpecialDeduction is required"));
    }

    @Test
    void shouldReturnBadRequestForNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":-1,\"annualSpecialDeduction\":0,\"annualOtherDeduction\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSalaryIncome must be greater than or equal to 0"));
    }

    @Test
    void shouldReturnBadRequestForInvalidNumber() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":\"abc\",\"annualSpecialDeduction\":0,\"annualOtherDeduction\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSalaryIncome must be a valid number"));
    }

    @Test
    void shouldReturnBadRequestForScaleGreaterThanTwo() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":100000.001,\"annualSpecialDeduction\":0,\"annualOtherDeduction\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSalaryIncome scale must be less than or equal to 2"));
    }

    @Test
    void shouldReturnFirstValidationErrorWhenMultipleFieldsAreInvalid() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"annualSalaryIncome\":null,\"annualSpecialDeduction\":-1,\"annualOtherDeduction\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("annualSalaryIncome is required"));
    }
}
