package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

public class Availability implements Serializable {
    private final EnumMap<DayOfWeek, Double> hoursPerDay = new EnumMap<>(DayOfWeek.class);

    public Availability() {
        for (DayOfWeek d : DayOfWeek.values()) {
            hoursPerDay.put(d, 0.0);
        }
    }

    public void setHours(DayOfWeek day, double hours) {
        if (day == null) throw new IllegalArgumentException("Day is required.");
        if (hours < 0 || hours > 12) throw new IllegalArgumentException("Availability hours must be 0..12.");
        hoursPerDay.put(day, hours);
    }

    public double getHours(DayOfWeek day) {
        return hoursPerDay.getOrDefault(day, 0.0);
    }

    public Map<DayOfWeek, Double> asMap() {
        return new EnumMap<>(hoursPerDay);
    }
}
