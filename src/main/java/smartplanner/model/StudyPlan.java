package smartplanner.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudyPlan {
    private final List<StudyBlock> blocks;

    public StudyPlan() {
        this.blocks = new ArrayList<>();
    }

    public void addBlock(StudyBlock block) {
        blocks.add(block);
        blocks.sort(Comparator.comparing(StudyBlock::getDay));
    }

    public void clear() {
        blocks.clear();
    }

    public List<StudyBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public List<StudyBlock> getBlocksForDay(DayOfWeek day) {
        return blocks.stream()
                .filter(block -> block.getDay() == day)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return blocks.isEmpty();
    }
}
