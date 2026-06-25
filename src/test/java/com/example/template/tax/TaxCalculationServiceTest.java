package com.example.template.tax;

import com.example.template.common.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxCalculationServiceTest {

    private final TaxCalculationService taxCalculationService = new TaxCalculationService();

    @Test
    void shouldCalculateTaxWithTenPercentBracket() {
        TaxCalculateResponse response = taxCalculationService.calculate(request("120000", "12000", "8000"));

        assertTaxResponse(response, "40000.00", "1480.00", "0.10", "2520.00");
    }

    @Test
    void shouldCalculateTaxWithThreePercentBracket() {
        TaxCalculateResponse response = taxCalculationService.calculate(request("90000", "0", "0"));

        assertTaxResponse(response, "30000.00", "900.00", "0.03", "0.00");
    }

    @Test
    void shouldReturnZeroTaxWhenTaxableIncomeIsZero() {
        TaxCalculateResponse response = taxCalculationService.calculate(request("60000", "0", "0"));

        assertTaxResponse(response, "0.00", "0.00", "0.00", "0.00");
    }

    @Test
    void shouldReturnZeroTaxWhenTaxableIncomeIsNegative() {
        TaxCalculateResponse response = taxCalculationService.calculate(request("50000", "10000", "0"));

        assertTaxResponse(response, "0.00", "0.00", "0.00", "0.00");
    }

    @ParameterizedTest
    @CsvSource({
            "96000.00,36000.00,0.03,0.00,1080.00",
            "204000.00,144000.00,0.10,2520.00,11880.00",
            "360000.00,300000.00,0.20,16920.00,43080.00",
            "480000.00,420000.00,0.25,31920.00,73080.00",
            "720000.00,660000.00,0.30,52920.00,145080.00",
            "1020000.00,960000.00,0.35,85920.00,250080.00"
    })
    void shouldUseLowerBracketWhenTaxableIncomeIsOnBoundary(String salaryIncome, String taxableIncome, String taxRate, String quickDeduction, String taxAmount) {
        TaxCalculateResponse response = taxCalculationService.calculate(request(salaryIncome, "0", "0"));

        assertTaxResponse(response, taxableIncome, taxAmount, taxRate, quickDeduction);
    }

    @ParameterizedTest
    @CsvSource({
            "96000.01,36000.01,0.10,2520.00,1080.00",
            "204000.01,144000.01,0.20,16920.00,11880.00",
            "360000.01,300000.01,0.25,31920.00,43080.00",
            "480000.01,420000.01,0.30,52920.00,73080.00",
            "720000.01,660000.01,0.35,85920.00,145080.00",
            "1020000.01,960000.01,0.45,181920.00,250080.00"
    })
    void shouldUseNextBracketWhenTaxableIncomeExceedsBoundaryByOneCent(String salaryIncome, String taxableIncome, String taxRate, String quickDeduction, String taxAmount) {
        TaxCalculateResponse response = taxCalculationService.calculate(request(salaryIncome, "0", "0"));

        assertTaxResponse(response, taxableIncome, taxAmount, taxRate, quickDeduction);
    }

    @Test
    void shouldCalculateTaxForLargeAmount() {
        TaxCalculateResponse response = taxCalculationService.calculate(request("2000000", "0", "0"));

        assertTaxResponse(response, "1940000.00", "691080.00", "0.45", "181920.00");
    }

    @Test
    void shouldRejectEmptyRequest() {
        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(null));

        assertEquals("request body must not be empty", ex.getMessage());
    }

    @Test
    void shouldRejectMissingRequiredField() {
        TaxCalculateRequest request = new TaxCalculateRequest();
        request.setAnnualSpecialDeduction(new BigDecimal("12000"));
        request.setAnnualOtherDeduction(new BigDecimal("8000"));

        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(request));

        assertEquals("annualSalaryIncome is required", ex.getMessage());
    }

    @Test
    void shouldRejectNullField() {
        TaxCalculateRequest request = request("120000", "0", "8000");
        request.setAnnualSpecialDeduction(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(request));

        assertEquals("annualSpecialDeduction is required", ex.getMessage());
    }

    @Test
    void shouldRejectNegativeAmount() {
        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(request("-1", "0", "0")));

        assertEquals("annualSalaryIncome must be greater than or equal to 0", ex.getMessage());
    }

    @Test
    void shouldRejectScaleGreaterThanTwo() {
        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(request("100000.001", "0", "0")));

        assertEquals("annualSalaryIncome scale must be less than or equal to 2", ex.getMessage());
    }

    @Test
    void shouldReturnFirstValidationErrorWhenMultipleFieldsAreInvalid() {
        TaxCalculateRequest request = new TaxCalculateRequest();
        request.setAnnualSalaryIncome(null);
        request.setAnnualSpecialDeduction(new BigDecimal("-1"));
        request.setAnnualOtherDeduction(BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () -> taxCalculationService.calculate(request));

        assertEquals("annualSalaryIncome is required", ex.getMessage());
    }

    @Test
    void shouldReturnSameResponseForSameInput() {
        TaxCalculateRequest request = request("120000", "12000", "8000");

        TaxCalculateResponse first = taxCalculationService.calculate(request);
        TaxCalculateResponse second = taxCalculationService.calculate(request);

        assertTaxResponse(first, "40000.00", "1480.00", "0.10", "2520.00");
        assertTaxResponse(second, "40000.00", "1480.00", "0.10", "2520.00");
    }

    private TaxCalculateRequest request(String annualSalaryIncome, String annualSpecialDeduction, String annualOtherDeduction) {
        TaxCalculateRequest request = new TaxCalculateRequest();
        request.setAnnualSalaryIncome(new BigDecimal(annualSalaryIncome));
        request.setAnnualSpecialDeduction(new BigDecimal(annualSpecialDeduction));
        request.setAnnualOtherDeduction(new BigDecimal(annualOtherDeduction));
        return request;
    }

    private void assertTaxResponse(TaxCalculateResponse response, String taxableIncome, String taxAmount, String taxRate, String quickDeduction) {
        assertEquals(new BigDecimal(taxableIncome), response.getTaxableIncome());
        assertEquals(new BigDecimal(taxAmount), response.getTaxAmount());
        assertEquals(new BigDecimal(taxRate), response.getTaxRate());
        assertEquals(new BigDecimal(quickDeduction), response.getQuickDeduction());
    }
}
