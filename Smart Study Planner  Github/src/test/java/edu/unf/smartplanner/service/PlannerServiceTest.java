package edu.unf.smartplanner.service;

import edu.unf.smartplanner.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PlannerServiceTest {

    @Test
    void autoSuggestEstimatedHours_usesCreditHours() {
        PlannerData data = new PlannerData();
        PlannerService service = new PlannerService(data);

        Course c = service.addCourse("CEN4010", 4, "blue");

        Task t = service.addTask(c.getId(), "HW1", TaskType.ASSIGNMENT,
                LocalDateTime.now().plusDays(2),
                null, Priority.LOW, "");

        assertEquals(4.0, t.getEstimatedHours(), 1e-9);
    }
}
