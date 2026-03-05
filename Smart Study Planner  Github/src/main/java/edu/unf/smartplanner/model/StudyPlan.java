package edu.unf.smartplanner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class StudyPlan implements Serializable {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<StudyBlock> blocks;
    private final List<String> flaggedTaskIds; // tasks that could not be fully scheduled

    public StudyPlan(LocalDate startDate, LocalDate endDate, List<StudyBlock> blocks, List<String> flaggedTaskIds) {
        this.startDate = Objects.requireNonNull(startDate);
        this.endDate = Objects.requireNonNull(endDate);
        this.blocks = new ArrayList<>(Objects.requireNonNull(blocks));
        this.flaggedTaskIds = new ArrayList<>(Objects.requireNonNull(flaggedTaskIds));
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<StudyBlock> getBlocks() { return Collections.unmodifiableList(blocks); }
    public List<String> getFlaggedTaskIds() { return Collections.unmodifiableList(flaggedTaskIds); }
}
