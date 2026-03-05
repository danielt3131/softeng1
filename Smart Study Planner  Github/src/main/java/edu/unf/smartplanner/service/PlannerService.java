package edu.unf.smartplanner.service;

import edu.unf.smartplanner.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PlannerService {

    private final PlannerData data;
    private final StudyPlanGenerator generator;
    private StudyPlan lastPlan;

    public PlannerService(PlannerData data) {
        this.data = Objects.requireNonNull(data);
        this.generator = new StudyPlanGenerator();
    }

    public PlannerData getData() { return data; }
    public Optional<StudyPlan> getLastPlan() { return Optional.ofNullable(lastPlan); }

    public Course addCourse(String name, int creditHours, String tag) {
        Course c = new Course(name, creditHours, tag);
        data.getCourses().add(c);
        return c;
    }

    public void deleteCourse(String courseId) {
        data.getCourses().removeIf(c -> c.getId().equals(courseId));
        data.getTasks().removeIf(t -> t.getCourseId().equals(courseId));
    }

    public Task addTask(String courseId, String title, TaskType type, LocalDateTime due,
                        Double estimatedHoursOrNull, Priority priority, String notes) {
        Course c = findCourse(courseId);
        double estimated = (estimatedHoursOrNull == null)
                ? Math.max(0.5, c.getCreditHours() * 1.0)
                : estimatedHoursOrNull;

        Task t = new Task(courseId, title, type, due, estimated, priority, notes);
        data.getTasks().add(t);
        return t;
    }

    public void setTaskStatus(String taskId, TaskStatus status) {
        Task t = findTask(taskId);
        t.setStatus(status);
        if (status == TaskStatus.COMPLETED) {
            // removing remaining blocks is handled by regeneration: completed tasks are ignored
        }
    }

    public StudyPlan generatePlan(LocalDate start, LocalDate end) {
        lastPlan = generator.generate(start, end, data.getProfile(), data.getAvailability(), data.getTasks());
        return lastPlan;
    }

    public Course findCourse(String courseId) {
        return data.getCourses().stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Course not found."));
    }

    public Task findTask(String taskId) {
        return data.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found."));
    }
}
