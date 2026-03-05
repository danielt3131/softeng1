package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.*;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TasksPanel extends JPanel {
    private final PlannerService service;
    private final TasksTableModel model;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TasksPanel(PlannerService service) {
        super(new BorderLayout());
        this.service = service;
        this.model = new TasksTableModel(service.getData().getTasks(), service);

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton add = new JButton("Add");
        JButton status = new JButton("Set Status");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> onAdd());
        status.addActionListener(e -> onSetStatus(table));
        delete.addActionListener(e -> onDelete(table));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(add);
        top.add(status);
        top.add(delete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void onAdd() {
        if (service.getData().getCourses().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one course first.");
            return;
        }

        TaskDialog dlg = new TaskDialog(SwingUtilities.getWindowAncestor(this), service.getData().getCourses());
        dlg.setVisible(true);
        if (!dlg.isSaved()) return;

        try {
            service.addTask(
                    dlg.getSelectedCourseId(),
                    dlg.getTitleValue(),
                    dlg.getTypeValue(),
                    dlg.getDueDateTimeValue(),
                    dlg.getEstimatedHoursOrNull(),
                    dlg.getPriorityValue(),
                    dlg.getNotesValue()
            );
            model.fireTableDataChanged();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSetStatus(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Task t = service.getData().getTasks().get(row);

        TaskStatus selected = (TaskStatus) JOptionPane.showInputDialog(
                this,
                "Select status:",
                "Set Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                TaskStatus.values(),
                t.getStatus()
        );
        if (selected == null) return;

        service.setTaskStatus(t.getId(), selected);
        model.fireTableDataChanged();
    }

    private void onDelete(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Task t = service.getData().getTasks().get(row);
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete task '" + t.getTitle() + "'?",
                "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        service.getData().getTasks().removeIf(x -> x.getId().equals(t.getId()));
        model.fireTableDataChanged();
    }

    private class TasksTableModel extends AbstractTableModel {
        private final List<Task> tasks;
        private final PlannerService service;
        private final String[] cols = {"Title", "Course", "Type", "Due", "Est. Hours", "Priority", "Status"};

        TasksTableModel(List<Task> tasks, PlannerService service) { this.tasks = tasks; this.service = service; }

        @Override public int getRowCount() { return tasks.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Task t = tasks.get(rowIndex);
            String courseName = "";
            try { courseName = service.findCourse(t.getCourseId()).getName(); } catch (Exception ignored) {}

            return switch (columnIndex) {
                case 0 -> t.getTitle();
                case 1 -> courseName;
                case 2 -> t.getType();
                case 3 -> t.getDueDateTime().format(fmt);
                case 4 -> t.getEstimatedHours();
                case 5 -> t.getPriority();
                case 6 -> t.getStatus();
                default -> "";
            };
        }
    }
}
