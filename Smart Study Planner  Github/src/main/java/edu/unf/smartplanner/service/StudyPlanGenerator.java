package edu.unf.smartplanner.service;

import edu.unf.smartplanner.model.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class StudyPlanGenerator {

    public StudyPlan generate(LocalDate startDate,
                              LocalDate endDate,
                              UserProfile profile,
                              Availability availability,
                              List<Task> tasks) {

        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        Objects.requireNonNull(profile);
        Objects.requireNonNull(availability);
        Objects.requireNonNull(tasks);

        if (endDate.isBefore(startDate)) throw new IllegalArgumentException("End date must be on/after start date.");

        int blockMinutes = profile.getPreferredBlockMinutes();
        double blockHours = blockMinutes / 60.0;

        // Remaining hours per task (ignore completed tasks)
        Map<String, Double> remaining = new HashMap<>();
        for (Task t : tasks) {
            if (!t.isCompleted()) {
                remaining.put(t.getId(), t.getEstimatedHours());
            }
        }

        // Greedy ordering: earliest due date first, then higher priority first
        List<Task> sorted = tasks.stream()
                .filter(t -> !t.isCompleted())
                .sorted(Comparator
                        .comparing(Task::getDueDateTime)
                        .thenComparing((Task t) -> t.getPriority().weight(), Comparator.reverseOrder())
                        .thenComparing(Task::getTitle))
                .collect(Collectors.toList());

        List<StudyBlock> blocks = new ArrayList<>();
        // Track time used per day
        Map<LocalDate, Double> usedHours = new HashMap<>();

        // For each day, allocate blocks to tasks in sorted order
        LocalDate day = startDate;
        while (!day.isAfter(endDate)) {
            DayOfWeek dow = day.getDayOfWeek();
            double availableHours = availability.getHours(dow);
            double used = usedHours.getOrDefault(day, 0.0);

            // Start time for blocks (purely for display)
            LocalTime nextStart = LocalTime.of(8, 0).plusMinutes((long) Math.round(used * 60));

            boolean madeProgress = true;
            while (madeProgress) {
                madeProgress = false;

                // If day is full, stop
                if (used + blockHours > availableHours + 1e-9) break;

                for (Task t : sorted) {
                    double rem = remaining.getOrDefault(t.getId(), 0.0);
                    if (rem <= 1e-9) continue;

                    // Cannot schedule after due date/time
                    LocalDateTime due = t.getDueDateTime();
                    if (LocalDateTime.of(day, LocalTime.MAX).isAfter(due)) {
                        // still allowed earlier on due day; but not if day is after due date
                        if (day.isAfter(due.toLocalDate())) continue;
                    }
                    if (day.isEqual(due.toLocalDate())) {
                        // If due time is before our block would start+duration, reject this block.
                        LocalTime blockEnd = nextStart.plusMinutes(blockMinutes);
                        if (!blockEnd.isBefore(due.toLocalTime().plusSeconds(1))) {
                            continue;
                        }
                    }

                    // Allocate one block
                    blocks.add(new StudyBlock(t.getId(), day, nextStart, blockMinutes));
                    remaining.put(t.getId(), Math.max(0.0, rem - blockHours));
                    used += blockHours;
                    usedHours.put(day, used);
                    nextStart = nextStart.plusMinutes(blockMinutes);
                    madeProgress = true;
                    break; // re-check capacity, restart task loop
                }
            }

            day = day.plusDays(1);
        }

        // Flag tasks not fully scheduled
        List<String> flagged = new ArrayList<>();
        for (Task t : sorted) {
            double rem = remaining.getOrDefault(t.getId(), 0.0);
            if (rem > 1e-6) {
                flagged.add(t.getId());
            }
        }

        return new StudyPlan(startDate, endDate, blocks, flagged);
    }
}
