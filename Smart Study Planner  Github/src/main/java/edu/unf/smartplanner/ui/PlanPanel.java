package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.StudyBlock;
import edu.unf.smartplanner.model.StudyPlan;
import edu.unf.smartplanner.model.Task;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PlanPanel extends JPanel {
    private final PlannerService service;

    private final JTextField start = new JTextField(10);
    private final JTextField end = new JTextField(10);

    private final PlanTableModel model = new PlanTableModel();

    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PlanPanel(PlannerService service) {
        super(new BorderLayout());
        this.service = service;

        LocalDate today = LocalDate.now();
        start.setText(today.format(df));
        end.setText(today.plusDays(6).format(df));

        JButton generate = new JButton("Generate Plan");
        generate.addActionListener(e -> onGenerate());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Start (yyyy-MM-dd):"));
        top.add(start);
        top.add(new JLabel("End (yyyy-MM-dd):"));
        top.add(end);
        top.add(generate);

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void onGenerate() {
        try {
            LocalDate s = LocalDate.parse(start.getText().trim(), df);
            LocalDate e = LocalDate.parse(end.getText().trim(), df);

            StudyPlan plan = service.generatePlan(s, e);

            Map<String, Task> taskMap = service.getData().getTasks().stream()
                    .collect(Collectors.toMap(Task::getId, t -> t, (a,b) -> a));

            model.setPlan(plan, taskMap);
            model.fireTableDataChanged();

            if (!plan.getFlaggedTaskIds().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Some tasks could not be fully scheduled before their due date/time. They are flagged in the table.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class PlanRow {
        String date;
        String start;
        int minutes;
        String taskTitle;
        String flagged;
    }

    private static class PlanTableModel extends AbstractTableModel {
        private final List<PlanRow> rows = new ArrayList<>();
        private final String[] cols = {"Date", "Start", "Minutes", "Task", "Flagged Task?"};

        void setPlan(StudyPlan plan, Map<String, Task> taskMap) {
            rows.clear();

            Set<String> flaggedIds = new HashSet<>(plan.getFlaggedTaskIds());

            for (StudyBlock b : plan.getBlocks()) {
                PlanRow r = new PlanRow();
                r.date = b.getDate().toString();
                r.start = b.getStart().toString();
                r.minutes = b.getMinutes();
                Task t = taskMap.get(b.getTaskId());
                r.taskTitle = (t == null) ? "(deleted task)" : t.getTitle();
                r.flagged = flaggedIds.contains(b.getTaskId()) ? "YES" : "";
                rows.add(r);
            }

            // Add extra rows showing flagged tasks that got 0 blocks
            Set<String> seenTaskIds = plan.getBlocks().stream().map(StudyBlock::getTaskId).collect(Collectors.toSet());
            for (String id : flaggedIds) {
                if (!seenTaskIds.contains(id)) {
                    PlanRow r = new PlanRow();
                    r.date = "";
                    r.start = "";
                    r.minutes = 0;
                    Task t = taskMap.get(id);
                    r.taskTitle = (t == null) ? "(deleted task)" : t.getTitle();
                    r.flagged = "YES (0 blocks)";
                    rows.add(r);
                }
            }
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PlanRow r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.date;
                case 1 -> r.start;
                case 2 -> r.minutes;
                case 3 -> r.taskTitle;
                case 4 -> r.flagged;
                default -> "";
            };
        }
    }
}
