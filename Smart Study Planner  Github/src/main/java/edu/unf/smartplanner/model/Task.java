package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Task implements Serializable {
    private final String id;
    private String courseId;
    private String title;
    private TaskType type;
    private LocalDateTime dueDateTime;
    private double estimatedHours; // 0.5-50
    private Priority priority;
    private String notes;
    private TaskStatus status;

    public Task(String courseId, String title, TaskType type, LocalDateTime dueDateTime,
                double estimatedHours, Priority priority, String notes) {
        this.id = UUID.randomUUID().toString();
        setCourseId(courseId);
        setTitle(title);
        setType(type);
        setDueDateTime(dueDateTime);
        setEstimatedHours(estimatedHours);
        setPriority(priority);
        setNotes(notes);
        this.status = TaskStatus.NOT_STARTED;
    }

    public String getId() { return id; }
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public TaskType getType() { return type; }
    public LocalDateTime getDueDateTime() { return dueDateTime; }
    public double getEstimatedHours() { return estimatedHours; }
    public Priority getPriority() { return priority; }
    public String getNotes() { return notes; }
    public TaskStatus getStatus() { return status; }

    public void setCourseId(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) throw new IllegalArgumentException("Course is required.");
        this.courseId = courseId;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Task title is required.");
        this.title = title.trim();
    }

    public void setType(TaskType type) {
        if (type == null) throw new IllegalArgumentException("Task type is required.");
        this.type = type;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        if (dueDateTime == null) throw new IllegalArgumentException("Due date/time is required.");
        this.dueDateTime = dueDateTime;
    }

    public void setEstimatedHours(double estimatedHours) {
        if (estimatedHours < 0.5 || estimatedHours > 50) throw new IllegalArgumentException("Estimated hours must be 0.5..50.");
        this.estimatedHours = estimatedHours;
    }

    public void setPriority(Priority priority) {
        if (priority == null) throw new IllegalArgumentException("Priority is required.");
        this.priority = priority;
    }

    public void setNotes(String notes) {
        this.notes = (notes == null) ? "" : notes.trim();
    }

    public void setStatus(TaskStatus status) {
        if (status == null) throw new IllegalArgumentException("Status is required.");
        this.status = status;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
