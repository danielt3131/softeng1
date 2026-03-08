package smartplanner.model;

import java.time.DayOfWeek;
import java.util.Objects;

public class StudyBlock {
    private final DayOfWeek day;
    private final Task task;
    private final int minutes;

    public StudyBlock(DayOfWeek day, Task task, int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Study block minutes must be greater than 0.");
        }
        this.day = Objects.requireNonNull(day, "Day is required.");
        this.task = Objects.requireNonNull(task, "Task is required.");
        this.minutes = minutes;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public Task getTask() {
        return task;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return day + ": " + task.getTitle() + " - " + minutes + " mins";
    }
}
