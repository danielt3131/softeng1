package smartplanner.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    private String title;
    private Course course;
    private TaskType type;
    private Priority priority;
    private LocalDate dueDate;
    private int estimatedMinutes;
    private TaskStatus status;

    public Task(String title, Course course, TaskType type, Priority priority, LocalDate dueDate, int estimatedMinutes) {
        setTitle(title);
        setCourse(course);
        setType(type);
        setPriority(priority);
        setDueDate(dueDate);
        setEstimatedMinutes(estimatedMinutes);
        this.status = TaskStatus.NOT_STARTED;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String value = Objects.requireNonNullElse(title, "").trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Task title is required.");
        }
        this.title = value;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Task must belong to a course.");
        }
        this.course = course;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = Objects.requireNonNull(type, "Task type is required.");
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = Objects.requireNonNull(priority, "Priority is required.");
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date is required.");
        }
        this.dueDate = dueDate;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException("Estimated minutes must be greater than 0.");
        }
        this.estimatedMinutes = estimatedMinutes;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void markCompleted() {
        this.status = TaskStatus.COMPLETED;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public long daysUntilDue(LocalDate fromDate) {
        return ChronoUnit.DAYS.between(fromDate, dueDate);
    }

    @Override
    public String toString() {
        return title + " (" + course.getCode() + ") - due " + dueDate;
    }
}
