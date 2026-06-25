package com.example.template.tax;

import com.example.template.common.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
public class TaxCalculationService {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final BigDecimal BASIC_DEDUCTION = new BigDecimal("60000.00");
    private static final int OUTPUT_SCALE = 2;

    private static final List<TaxBracket> TAX_BRACKETS = Arrays.asList(
            new TaxBracket(new BigDecimal("36000.00"), new BigDecimal("0.03"), new BigDecimal("0.00")),
            new TaxBracket(new BigDecimal("144000.00"), new BigDecimal("0.10"), new BigDecimal("2520.00")),
            new TaxBracket(new BigDecimal("300000.00"), new BigDecimal("0.20"), new BigDecimal("16920.00")),
            new TaxBracket(new BigDecimal("420000.00"), new BigDecimal("0.25"), new BigDecimal("31920.00")),
            new TaxBracket(new BigDecimal("660000.00"), new BigDecimal("0.30"), new BigDecimal("52920.00")),
            new TaxBracket(new BigDecimal("960000.00"), new BigDecimal("0.35"), new BigDecimal("85920.00")),
            new TaxBracket(null, new BigDecimal("0.45"), new BigDecimal("181920.00"))
    );

    public TaxCalculateResponse calculate(TaxCalculateRequest request) {
        validateRequest(request);

        BigDecimal taxableIncome = request.getAnnualSalaryIncome()
                .subtract(BASIC_DEDUCTION)
                .subtract(request.getAnnualSpecialDeduction())
                .subtract(request.getAnnualOtherDeduction());

        if (taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return new TaxCalculateResponse(ZERO, ZERO, ZERO, ZERO);
        }

        TaxBracket taxBracket = findTaxBracket(taxableIncome);
        BigDecimal taxAmount = taxableIncome.multiply(taxBracket.getTaxRate()).subtract(taxBracket.getQuickDeduction());

        return new TaxCalculateResponse(
                scaleMoney(taxableIncome),
                scaleMoney(taxAmount),
                taxBracket.getTaxRate().setScale(OUTPUT_SCALE, RoundingMode.DOWN),
                scaleMoney(taxBracket.getQuickDeduction())
        );
    }

    private void validateRequest(TaxCalculateRequest request) {
        if (request == null) {
            throw new BusinessException("request body must not be empty");
        }
        validateAmount("annualSalaryIncome", request.getAnnualSalaryIncome());
        validateAmount("annualSpecialDeduction", request.getAnnualSpecialDeduction());
        validateAmount("annualOtherDeduction", request.getAnnualOtherDeduction());
    }

    private void validateAmount(String fieldName, BigDecimal amount) {
        if (amount == null) {
            throw new BusinessException(fieldName + " is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(fieldName + " must be greater than or equal to 0");
        }
        if (amount.scale() > OUTPUT_SCALE) {
            throw new BusinessException(fieldName + " scale must be less than or equal to 2");
        }
    }

    private TaxBracket findTaxBracket(BigDecimal taxableIncome) {
        for (TaxBracket taxBracket : TAX_BRACKETS) {
            if (taxBracket.getUpperLimit() == null || taxableIncome.compareTo(taxBracket.getUpperLimit()) <= 0) {
                return taxBracket;
            }
        }
        throw new IllegalStateException("tax bracket not found");
    }

    private BigDecimal scaleMoney(BigDecimal amount) {
        return amount.setScale(OUTPUT_SCALE, RoundingMode.DOWN);
    }

    private static class TaxBracket {

        private final BigDecimal upperLimit;
        private final BigDecimal taxRate;
        private final BigDecimal quickDeduction;

        TaxBracket(BigDecimal upperLimit, BigDecimal taxRate, BigDecimal quickDeduction) {
            this.upperLimit = upperLimit;
            this.taxRate = taxRate;
            this.quickDeduction = quickDeduction;
        }

        BigDecimal getUpperLimit() {
            return upperLimit;
        }

        BigDecimal getTaxRate() {
            return taxRate;
        }

        BigDecimal getQuickDeduction() {
            return quickDeduction;
        }
    }
}
