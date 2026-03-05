package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlannerData implements Serializable {
    private UserProfile profile;
    private Availability availability;
    private List<Course> courses;
    private List<Task> tasks;

    public PlannerData() {
        this.profile = new UserProfile("Student", 10, 60);
        this.availability = new Availability();
        this.courses = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public UserProfile getProfile() { return profile; }
    public Availability getAvailability() { return availability; }
    public List<Course> getCourses() { return courses; }
    public List<Task> getTasks() { return tasks; }

    public void setProfile(UserProfile profile) { this.profile = profile; }
    public void setAvailability(Availability availability) { this.availability = availability; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
