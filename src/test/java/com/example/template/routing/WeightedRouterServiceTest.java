package com.example.template.routing;

import com.example.template.common.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightedRouterServiceTest {

    @Test
    void shouldRouteByWeightDistribution() {
        WeightedRouterService service = new WeightedRouterService(new Random(12345L));
        List<Node> nodes = Arrays.asList(
                new Node("A", 5, true),
                new Node("B", 3, true),
                new Node("C", 2, true)
        );

        int countA = 0;
        int countB = 0;
        int countC = 0;
        int totalRuns = 10000;
        for (int i = 0; i < totalRuns; i++) {
            Node selected = service.route(nodes);
            if ("A".equals(selected.getName())) {
                countA++;
            } else if ("B".equals(selected.getName())) {
                countB++;
            } else if ("C".equals(selected.getName())) {
                countC++;
            }
        }

        assertEquals(totalRuns, countA + countB + countC);
        assertWithinRange(countA, 4700, 5300);
        assertWithinRange(countB, 2700, 3300);
        assertWithinRange(countC, 1700, 2300);
    }

    @Test
    void shouldOnlyReturnEnabledNode() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));
        List<Node> nodes = Arrays.asList(
                new Node("A", 10, false),
                new Node("B", 1, true)
        );

        for (int i = 0; i < 20; i++) {
            assertEquals("B", service.route(nodes).getName());
        }
    }

    @Test
    void shouldIgnoreZeroWeightNode() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));
        List<Node> nodes = Arrays.asList(
                new Node("A", 0, true),
                new Node("B", 5, true)
        );

        for (int i = 0; i < 20; i++) {
            assertEquals("B", service.route(nodes).getName());
        }
    }

    @Test
    void shouldThrowWhenAllNodesUnavailable() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));
        List<Node> nodes = Arrays.asList(
                new Node("A", 0, true),
                new Node("B", 3, false)
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                service.route(nodes);
            }
        });

        assertEquals("No available node", exception.getMessage());
    }

    @Test
    void shouldThrowWhenNodesIsNull() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));

        assertThrows(IllegalStateException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                service.route(null);
            }
        });
    }

    @Test
    void shouldThrowWhenNodesIsEmpty() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));

        assertThrows(IllegalStateException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                service.route(Collections.<Node>emptyList());
            }
        });
    }

    @Test
    void shouldIgnoreNullNodeElements() {
        WeightedRouterService service = new WeightedRouterService(new Random(1L));
        List<Node> nodes = Arrays.asList(
                null,
                new Node("A", 0, true),
                new Node("B", 1, true)
        );

        for (int i = 0; i < 10; i++) {
            assertEquals("B", service.route(nodes).getName());
        }
    }

    private static void assertWithinRange(int value, int minInclusive, int maxInclusive) {
        if (value < minInclusive || value > maxInclusive) {
            throw new AssertionError("value " + value + " is not in range [" + minInclusive + ", " + maxInclusive + "]");
        }
    }
}
