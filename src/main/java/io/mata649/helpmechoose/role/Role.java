package io.mata649.helpmechoose.role;

public enum Role {
    USER("USER"),
    ADMINISTRATOR("ADMINISTRATOR");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
