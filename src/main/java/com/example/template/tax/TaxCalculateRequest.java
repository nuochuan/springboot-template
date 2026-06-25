package com.example.template.tax;

import java.math.BigDecimal;

public class TaxCalculateRequest {

    private BigDecimal annualSalaryIncome;
    private BigDecimal annualSpecialDeduction;
    private BigDecimal annualOtherDeduction;

    public TaxCalculateRequest() {
    }

    public BigDecimal getAnnualSalaryIncome() {
        return annualSalaryIncome;
    }

    public void setAnnualSalaryIncome(BigDecimal annualSalaryIncome) {
        this.annualSalaryIncome = annualSalaryIncome;
    }

    public BigDecimal getAnnualSpecialDeduction() {
        return annualSpecialDeduction;
    }

    public void setAnnualSpecialDeduction(BigDecimal annualSpecialDeduction) {
        this.annualSpecialDeduction = annualSpecialDeduction;
    }

    public BigDecimal getAnnualOtherDeduction() {
        return annualOtherDeduction;
    }

    public void setAnnualOtherDeduction(BigDecimal annualOtherDeduction) {
        this.annualOtherDeduction = annualOtherDeduction;
    }
}
