package smartplanner.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import smartplanner.model.Availability;
import smartplanner.model.Course;
import smartplanner.model.Planner;
import smartplanner.model.Priority;
import smartplanner.model.Profile;
import smartplanner.model.Task;
import smartplanner.model.TaskType;

public class PlannerSerializer {
    private static final String PROFILE = "PROFILE|";
    private static final String AVAILABILITY = "AVAILABILITY|";
    private static final String COURSE = "COURSE|";
    private static final String TASK = "TASK|";

    public void save(Planner planner, Path path) throws IOException {
        StringBuilder builder = new StringBuilder();
        Profile profile = planner.getProfile();
        builder.append(PROFILE)
                .append(escape(profile.getStudentName())).append('|')
                .append(escape(profile.getMajor())).append('|')
                .append(profile.getPreferredDailyStudyMinutes()).append('\n');

        for (DayOfWeek day : DayOfWeek.values()) {
            builder.append(AVAILABILITY)
                    .append(day.name()).append('|')
                    .append(planner.getAvailability().getMinutes(day)).append('\n');
        }

        for (Course course : planner.getCourses()) {
            builder.append(COURSE)
                    .append(escape(course.getCode())).append('|')
                    .append(escape(course.getName())).append('|')
                    .append(escape(course.getInstructor())).append('\n');
        }

        for (Task task : planner.getTasks()) {
            builder.append(TASK)
                    .append(escape(task.getTitle())).append('|')
                    .append(escape(task.getCourse().getCode())).append('|')
                    .append(task.getType().name()).append('|')
                    .append(task.getPriority().name()).append('|')
                    .append(task.getDueDate()).append('|')
                    .append(task.getEstimatedMinutes()).append('|')
                    .append(task.getStatus().name()).append('\n');
        }

        Files.writeString(path, builder.toString());
    }

    public Planner load(Path path) throws IOException {
        Planner planner = new Planner();
        Map<String, Course> courseLookup = new HashMap<>();
        Availability availability = new Availability();
        planner.setAvailability(availability);

        for (String line : Files.readAllLines(path)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("(?<!\\\\)\\|", -1);
            String type = parts[0];
            switch (type) {
                case "PROFILE" -> planner.setProfile(new Profile(unescape(parts[1]), unescape(parts[2]), Integer.parseInt(parts[3])));
                case "AVAILABILITY" -> availability.setMinutes(DayOfWeek.valueOf(parts[1]), Integer.parseInt(parts[2]));
                case "COURSE" -> {
                    Course course = new Course(unescape(parts[1]), unescape(parts[2]), unescape(parts[3]));
                    planner.addCourse(course);
                    courseLookup.put(course.getCode(), course);
                }
                case "TASK" -> {
                    Course course = courseLookup.get(unescape(parts[2]));
                    Task task = new Task(
                            unescape(parts[1]),
                            course,
                            TaskType.valueOf(parts[3]),
                            Priority.valueOf(parts[4]),
                            LocalDate.parse(parts[5]),
                            Integer.parseInt(parts[6])
                    );
                    if ("COMPLETED".equals(parts[7])) {
                        task.markCompleted();
                    }
                    planner.addTask(task);
                }
                default -> throw new IOException("Unknown record type: " + type);
            }
        }
        return planner;
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    private String unescape(String value) {
        return value.replace("\\|", "|").replace("\\\\", "\\");
    }
}
