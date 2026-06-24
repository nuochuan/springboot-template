package com.example.template.routing;

import com.example.template.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/router")
public class WeightedRouterController {

    private final WeightedRouterService weightedRouterService;

    public WeightedRouterController(WeightedRouterService weightedRouterService) {
        this.weightedRouterService = weightedRouterService;
    }

    @PostMapping("/route")
    public ApiResponse<Node> route(@RequestBody List<Node> nodes) {
        return ApiResponse.ok(weightedRouterService.route(nodes));
    }
}
