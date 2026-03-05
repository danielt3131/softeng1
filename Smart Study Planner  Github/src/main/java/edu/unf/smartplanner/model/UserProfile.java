package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.util.Objects;

public class UserProfile implements Serializable {
    private String name;
    private double weeklyStudyGoalHours;
    private int preferredBlockMinutes; // 30, 60, 90...

    public UserProfile(String name, double weeklyStudyGoalHours, int preferredBlockMinutes) {
        setName(name);
        setWeeklyStudyGoalHours(weeklyStudyGoalHours);
        setPreferredBlockMinutes(preferredBlockMinutes);
    }

    public String getName() { return name; }
    public double getWeeklyStudyGoalHours() { return weeklyStudyGoalHours; }
    public int getPreferredBlockMinutes() { return preferredBlockMinutes; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name is required.");
        this.name = name.trim();
    }

    public void setWeeklyStudyGoalHours(double hours) {
        if (hours < 0 || hours > 100) throw new IllegalArgumentException("Weekly goal must be between 0 and 100.");
        this.weeklyStudyGoalHours = hours;
    }

    public void setPreferredBlockMinutes(int minutes) {
        if (minutes <= 0 || minutes > 240) throw new IllegalArgumentException("Preferred block minutes must be 1..240.");
        this.preferredBlockMinutes = minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        return Double.compare(that.weeklyStudyGoalHours, weeklyStudyGoalHours) == 0
                && preferredBlockMinutes == that.preferredBlockMinutes
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name, weeklyStudyGoalHours, preferredBlockMinutes); }
}
