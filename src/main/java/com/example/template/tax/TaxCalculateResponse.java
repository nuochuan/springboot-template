package com.example.template.tax;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public class TaxCalculateResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxableIncome;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxRate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal quickDeduction;

    public TaxCalculateResponse() {
    }

    public TaxCalculateResponse(BigDecimal taxableIncome, BigDecimal taxAmount, BigDecimal taxRate, BigDecimal quickDeduction) {
        this.taxableIncome = taxableIncome;
        this.taxAmount = taxAmount;
        this.taxRate = taxRate;
        this.quickDeduction = quickDeduction;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getQuickDeduction() {
        return quickDeduction;
    }

    public void setQuickDeduction(BigDecimal quickDeduction) {
        this.quickDeduction = quickDeduction;
    }
}
