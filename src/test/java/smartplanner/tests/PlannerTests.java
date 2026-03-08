package smartplanner.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import smartplanner.model.Availability;
import smartplanner.model.Course;
import smartplanner.model.Planner;
import smartplanner.model.Priority;
import smartplanner.model.Task;
import smartplanner.model.TaskStatus;
import smartplanner.model.TaskType;
import smartplanner.persistence.PlannerSerializer;
import smartplanner.service.PlanGenerator;

public class PlannerTests {
    @Test
    void testAddCourseAndTask() {
        Planner planner = new Planner();
        Course course = new Course("CEN4010", "Software Engineering", "Dr. Smith");
        planner.addCourse(course);
        Task task = new Task(
                "Iteration 1 PDF",
                course,
                TaskType.PROJECT,
                Priority.HIGH,
                LocalDate.now().plusDays(2),
                90);
        planner.addTask(task);

        assertEquals(1, planner.getCourses().size());
        assertEquals(1, planner.getTasks().size());
        assertEquals("Iteration 1 PDF", planner.getTasks().get(0).getTitle());
    }

    @Test
    void testMarkTaskCompleted() {
        Planner planner = new Planner();
        Course course = new Course("COP3530", "Data Structures", "Dr. Lee");
        Task task = new Task(
                "Homework 4",
                course,
                TaskType.HOMEWORK,
                Priority.MEDIUM,
                LocalDate.now().plusDays(4),
                60);
        planner.addCourse(course);
        planner.addTask(task);

        planner.markTaskCompleted(task);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        assertEquals(0, planner.getIncompleteTasks().size());
    }

    @Test
    void testGeneratePlanPrioritizesUrgentWork() {
        Planner planner = new Planner();
        Course course = new Course("MAD2104", "Discrete Math", "Dr. Patel");
        planner.addCourse(course);

        Task urgent = new Task(
                "Quiz Prep",
                course,
                TaskType.QUIZ,
                Priority.HIGH,
                LocalDate.now().plusDays(1),
                60);
        Task later = new Task(
                "Reading",
                course,
                TaskType.READING,
                Priority.LOW,
                LocalDate.now().plusDays(10),
                60);
        planner.addTask(urgent);
        planner.addTask(later);

        Availability availability = planner.getAvailability();
        for (DayOfWeek day : DayOfWeek.values()) {
            availability.setMinutes(day, 60);
        }

        PlanGenerator generator = new PlanGenerator();
        String firstAssignedTask = generator.generate(planner).getBlocks().get(0).getTask().getTitle();
        assertEquals("Quiz Prep", firstAssignedTask);
    }

    @Test
    void testSerializerSaveAndLoad() throws Exception {
        Planner planner = new Planner();
        Course course = new Course("CAP4630", "AI", "Dr. Gomez");
        planner.addCourse(course);
        planner.addTask(new Task(
                "FRQ practice",
                course,
                TaskType.EXAM_PREP,
                Priority.HIGH,
                LocalDate.of(2026, 3, 20),
                75));

        Path tempFile = Files.createTempFile("planner", ".txt");
        PlannerSerializer serializer = new PlannerSerializer();
        serializer.save(planner, tempFile);
        Planner loaded = serializer.load(tempFile);

        assertEquals(1, loaded.getCourses().size());
        assertEquals(1, loaded.getTasks().size());
        assertEquals("FRQ practice", loaded.getTasks().get(0).getTitle());

        Files.deleteIfExists(tempFile);
    }
}
