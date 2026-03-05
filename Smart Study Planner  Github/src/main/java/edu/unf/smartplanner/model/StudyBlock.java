package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class StudyBlock implements Serializable {
    private final String taskId;
    private final LocalDate date;
    private final LocalTime start;
    private final int minutes;

    public StudyBlock(String taskId, LocalDate date, LocalTime start, int minutes) {
        this.taskId = Objects.requireNonNull(taskId);
        this.date = Objects.requireNonNull(date);
        this.start = Objects.requireNonNull(start);
        if (minutes <= 0) throw new IllegalArgumentException("Minutes must be positive.");
        this.minutes = minutes;
    }

    public String getTaskId() { return taskId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public int getMinutes() { return minutes; }

    @Override
    public String toString() {
        return date + " " + start + " (" + minutes + " min)";
    }
}
