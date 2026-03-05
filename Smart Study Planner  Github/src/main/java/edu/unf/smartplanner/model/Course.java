package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Course implements Serializable {
    private final String id;
    private String name;
    private int creditHours; // 1-6
    private String tag; // optional (color/tag as text)

    public Course(String name, int creditHours, String tag) {
        this.id = UUID.randomUUID().toString();
        setName(name);
        setCreditHours(creditHours);
        setTag(tag);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCreditHours() { return creditHours; }
    public String getTag() { return tag; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Course name is required.");
        this.name = name.trim();
    }

    public void setCreditHours(int creditHours) {
        if (creditHours < 1 || creditHours > 6) throw new IllegalArgumentException("Credit hours must be 1..6.");
        this.creditHours = creditHours;
    }

    public void setTag(String tag) {
        this.tag = (tag == null) ? "" : tag.trim();
    }

    @Override
    public String toString() {
        return name + " (" + creditHours + " cr)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
