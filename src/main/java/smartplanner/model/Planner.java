package smartplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planner {
    private Profile profile;
    private Availability availability;
    private final List<Course> courses;
    private final List<Task> tasks;
    private StudyPlan studyPlan;

    public Planner() {
        this.profile = new Profile();
        this.availability = new Availability();
        this.courses = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.studyPlan = new StudyPlan();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void markTaskCompleted(Task task) {
        task.markCompleted();
    }

    public List<Task> getIncompleteTasks() {
        List<Task> remaining = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                remaining.add(task);
            }
        }
        return remaining;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }
}
