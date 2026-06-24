package com.example.template.demo;

import com.example.template.common.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DemoServiceTest {

    private final DemoService demoService = new DemoService();

    @Test
    void shouldReturnHelloMessage() {
        DemoResponse response = demoService.sayHello("Noah");

        assertEquals("Hello, Noah", response.getMessage());
    }

    @Test
    void shouldTrimName() {
        DemoResponse response = demoService.sayHello("  Noah  ");

        assertEquals("Hello, Noah", response.getMessage());
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(BusinessException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                demoService.sayHello(" ");
            }
        });
    }
}
