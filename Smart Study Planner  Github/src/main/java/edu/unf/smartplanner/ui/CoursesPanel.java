package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.Course;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class CoursesPanel extends JPanel {
    private final PlannerService service;
    private final CoursesTableModel model;

    public CoursesPanel(PlannerService service) {
        super(new BorderLayout());
        this.service = service;
        this.model = new CoursesTableModel(service.getData().getCourses());

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> onAdd());
        delete.addActionListener(e -> onDelete(table));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(add);
        top.add(delete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void onAdd() {
        CourseDialog dlg = new CourseDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (!dlg.isSaved()) return;

        try {
            service.addCourse(dlg.getNameValue(), dlg.getCreditHoursValue(), dlg.getTagValue());
            model.fireTableDataChanged();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Course c = service.getData().getCourses().get(row);
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete course '" + c.getName() + "'? This also deletes its tasks.",
                "Confirm", JOptionPane.OK_CANCEL_OPTION);

        if (ok != JOptionPane.OK_OPTION) return;

        service.deleteCourse(c.getId());
        model.fireTableDataChanged();
    }

    private static class CoursesTableModel extends AbstractTableModel {
        private final List<Course> courses;
        private final String[] cols = {"Name", "Credit Hours", "Tag"};

        CoursesTableModel(List<Course> courses) { this.courses = courses; }

        @Override public int getRowCount() { return courses.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Course c = courses.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> c.getName();
                case 1 -> c.getCreditHours();
                case 2 -> c.getTag();
                default -> "";
            };
        }
    }
}
