package com.example.template.demo;

import com.example.template.common.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public DemoResponse sayHello(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("name must not be blank");
        }
        return new DemoResponse("Hello, " + name.trim());
    }
}
