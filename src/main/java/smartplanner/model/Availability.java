package smartplanner.model;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

public class Availability {
    private final Map<DayOfWeek, Integer> dailyMinutes;

    public Availability() {
        dailyMinutes = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            dailyMinutes.put(day, 120);
        }
    }

    public int getMinutes(DayOfWeek day) {
        return dailyMinutes.get(day);
    }

    public void setMinutes(DayOfWeek day, int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Availability minutes cannot be negative.");
        }
        dailyMinutes.put(day, minutes);
    }

    public Map<DayOfWeek, Integer> getAllMinutes() {
        return new EnumMap<>(dailyMinutes);
    }
}
