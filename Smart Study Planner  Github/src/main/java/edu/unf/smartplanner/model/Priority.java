package edu.unf.smartplanner.model;

import java.io.Serializable;

public enum Priority implements Serializable {
    LOW, MEDIUM, HIGH;

    public int weight() {
        return switch (this) {
            case HIGH -> 3;
            case MEDIUM -> 2;
            case LOW -> 1;
        };
    }
}
