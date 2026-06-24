package com.example.template.routing;

public class Node {

    private String name;
    private Integer weight;
    private Boolean enabled;

    public Node() {
    }

    public Node(String name, Integer weight, Boolean enabled) {
        this.name = name;
        this.weight = weight;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
