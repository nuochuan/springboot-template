package com.example.template.demo;

import com.example.template.common.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public DemoResponse sayHello(String name) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new BusinessException("name must not be blank");
        }
        return new DemoResponse("Hello, " + normalizedName);
    }
}
