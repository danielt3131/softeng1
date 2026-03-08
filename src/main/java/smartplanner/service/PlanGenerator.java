package smartplanner.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import smartplanner.model.Availability;
import smartplanner.model.Planner;
import smartplanner.model.StudyBlock;
import smartplanner.model.StudyPlan;
import smartplanner.model.Task;

public class PlanGenerator {
    public StudyPlan generate(Planner planner) {
        StudyPlan plan = new StudyPlan();
        List<TaskWorkItem> items = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Task task : planner.getIncompleteTasks()) {
            items.add(new TaskWorkItem(task, task.getEstimatedMinutes()));
        }

        items.sort(Comparator
                .comparingInt((TaskWorkItem item) -> urgencyScore(item.task(), today)).reversed()
                .thenComparing(item -> item.task().getDueDate()));

        Availability availability = planner.getAvailability();
        for (DayOfWeek day : DayOfWeek.values()) {
            int remainingDayMinutes = availability.getMinutes(day);
            for (TaskWorkItem item : items) {
                if (remainingDayMinutes <= 0) {
                    break;
                }
                if (item.remainingMinutes() <= 0) {
                    continue;
                }
                int minutesToAssign = Math.min(remainingDayMinutes, item.remainingMinutes());
                if (minutesToAssign > 0) {
                    plan.addBlock(new StudyBlock(day, item.task(), minutesToAssign));
                    item.reduce(minutesToAssign);
                    remainingDayMinutes -= minutesToAssign;
                }
            }
        }

        planner.setStudyPlan(plan);
        return plan;
    }

    private int urgencyScore(Task task, LocalDate today) {
        long daysUntilDue = task.daysUntilDue(today);
        int dueSoonWeight;
        if (daysUntilDue <= 1) {
            dueSoonWeight = 5;
        } else if (daysUntilDue <= 3) {
            dueSoonWeight = 4;
        } else if (daysUntilDue <= 7) {
            dueSoonWeight = 3;
        } else {
            dueSoonWeight = 1;
        }
        return (task.getPriority().getWeight() * 10) + dueSoonWeight;
    }

    private static final class TaskWorkItem {
        private final Task task;
        private int remainingMinutes;

        private TaskWorkItem(Task task, int remainingMinutes) {
            this.task = task;
            this.remainingMinutes = remainingMinutes;
        }

        private Task task() {
            return task;
        }

        private int remainingMinutes() {
            return remainingMinutes;
        }

        private void reduce(int assignedMinutes) {
            remainingMinutes -= assignedMinutes;
        }
    }
}
