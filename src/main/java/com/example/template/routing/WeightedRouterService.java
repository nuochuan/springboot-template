package com.example.template.routing;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WeightedRouterService {

    private static final String NO_AVAILABLE_NODE_MESSAGE = "No available node";

    private final Random random;

    public WeightedRouterService() {
        this(new Random());
    }

    public WeightedRouterService(Random random) {
        this.random = random == null ? new Random() : random;
    }

    public Node route(List<Node> nodes) {
        List<Node> availableNodes = filterAvailableNodes(nodes);
        if (availableNodes.isEmpty()) {
            throw new IllegalStateException(NO_AVAILABLE_NODE_MESSAGE);
        }

        long totalWeight = 0L;
        for (Node node : availableNodes) {
            totalWeight += node.getWeight();
        }

        double threshold = random.nextDouble() * totalWeight;
        long cumulativeWeight = 0L;
        for (Node node : availableNodes) {
            cumulativeWeight += node.getWeight();
            if (threshold < cumulativeWeight) {
                return node;
            }
        }

        return availableNodes.get(availableNodes.size() - 1);
    }

    private List<Node> filterAvailableNodes(List<Node> nodes) {
        List<Node> availableNodes = new ArrayList<Node>();
        if (nodes == null) {
            return availableNodes;
        }

        for (Node node : nodes) {
            if (node == null) {
                continue;
            }
            if (!Boolean.TRUE.equals(node.getEnabled())) {
                continue;
            }
            if (node.getWeight() == null || node.getWeight() <= 0) {
                continue;
            }
            availableNodes.add(node);
        }
        return availableNodes;
    }
}
