package org.example;

public enum EnvironmentVariables {
    KAFKA_HOST("KAFKA_HOST");

    private final String name;

    EnvironmentVariables(String name) {
        this.name = name;
    }

    public String getValue() {
        return System.getenv(this.name);
    }
}
