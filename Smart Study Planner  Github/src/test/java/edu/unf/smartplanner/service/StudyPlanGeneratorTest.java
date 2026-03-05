package edu.unf.smartplanner.service;

import edu.unf.smartplanner.model.*;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudyPlanGeneratorTest {

    @Test
    void doesNotScheduleAfterDueDateTime_onDueDay() {
        UserProfile profile = new UserProfile("K", 10, 60);
        Availability avail = new Availability();
        avail.setHours(DayOfWeek.MONDAY, 6);

        LocalDate monday = LocalDate.of(2026, 3, 2); // Monday
        Task t = new Task("c1", "Exam", TaskType.EXAM,
                LocalDateTime.of(monday, LocalTime.of(9, 0)), // due at 9am
                2.0, Priority.HIGH, "");

        StudyPlan plan = new StudyPlanGenerator().generate(monday, monday, profile, avail, List.of(t));

        // Blocks start at 8:00 with 60 min. The first ends at 9:00, which is not before due => should schedule 0 blocks.
        assertEquals(0, plan.getBlocks().size());
        assertEquals(1, plan.getFlaggedTaskIds().size());
        assertTrue(plan.getFlaggedTaskIds().contains(t.getId()));
    }

    @Test
    void schedulesBeforeDueDate_onPreviousDays() {
        UserProfile profile = new UserProfile("K", 10, 60);
        Availability avail = new Availability();
        avail.setHours(DayOfWeek.SUNDAY, 2);
        avail.setHours(DayOfWeek.MONDAY, 2);

        LocalDate sunday = LocalDate.of(2026, 3, 1);
        LocalDate monday = LocalDate.of(2026, 3, 2);

        Task t = new Task("c1", "Project", TaskType.PROJECT,
                LocalDateTime.of(monday, LocalTime.of(12, 0)), // due Monday noon
                3.0, Priority.HIGH, "");

        StudyPlan plan = new StudyPlanGenerator().generate(sunday, monday, profile, avail, List.of(t));

        // Total availability = 4 hours, need 3 => should fully schedule and no flag
        assertEquals(3, plan.getBlocks().size());
        assertEquals(0, plan.getFlaggedTaskIds().size());
    }

    @Test
    void ignoresCompletedTasks() {
        UserProfile profile = new UserProfile("K", 10, 60);
        Availability avail = new Availability();
        avail.setHours(DayOfWeek.MONDAY, 6);

        LocalDate monday = LocalDate.of(2026, 3, 2);
        Task t = new Task("c1", "Assignment", TaskType.ASSIGNMENT,
                LocalDateTime.of(monday.plusDays(3), LocalTime.of(23, 59)),
                2.0, Priority.MEDIUM, "");
        t.setStatus(TaskStatus.COMPLETED);

        StudyPlan plan = new StudyPlanGenerator().generate(monday, monday, profile, avail, List.of(t));
        assertEquals(0, plan.getBlocks().size());
        assertEquals(0, plan.getFlaggedTaskIds().size());
    }
}
