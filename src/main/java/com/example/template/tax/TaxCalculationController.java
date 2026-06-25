package com.example.template.tax;

import com.example.template.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax")
public class TaxCalculationController {

    private final TaxCalculationService taxCalculationService;

    public TaxCalculationController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    @PostMapping("/calculate")
    public ApiResponse<TaxCalculateResponse> calculate(@RequestBody(required = false) TaxCalculateRequest request) {
        return ApiResponse.ok(taxCalculationService.calculate(request));
    }
}
