package smartplanner.model;

import java.util.Objects;

public class Course {
    private String code;
    private String name;
    private String instructor;

    public Course(String code, String name, String instructor) {
        setCode(code);
        setName(name);
        setInstructor(instructor);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        String value = Objects.requireNonNullElse(code, "").trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Course code is required.");
        }
        this.code = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String value = Objects.requireNonNullElse(name, "").trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Course name is required.");
        }
        this.name = value;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = Objects.requireNonNullElse(instructor, "").trim();
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
