package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskDialog extends JDialog {
    private boolean saved = false;

    private final JComboBox<Course> courseBox;
    private final JTextField title = new JTextField(22);
    private final JComboBox<TaskType> type = new JComboBox<>(TaskType.values());
    private final JTextField due = new JTextField(16); // yyyy-MM-dd HH:mm
    private final JTextField estHours = new JTextField(8); // blank = auto-suggest
    private final JComboBox<Priority> priority = new JComboBox<>(Priority.values());
    private final JTextArea notes = new JTextArea(4, 22);

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TaskDialog(Window owner, List<Course> courses) {
        super(owner, "Add Task", ModalityType.APPLICATION_MODAL);
        setSize(520, 360);
        setLocationRelativeTo(owner);

        courseBox = new JComboBox<>(courses.toArray(new Course[0]));
        due.setText(LocalDateTime.now().plusDays(3).withHour(23).withMinute(59).format(fmt));
        priority.setSelectedItem(Priority.MEDIUM);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;

        int r = 0;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Course:"), gc);
        gc.gridx=1; form.add(courseBox, gc); r++;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Title:"), gc);
        gc.gridx=1; form.add(title, gc); r++;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Type:"), gc);
        gc.gridx=1; form.add(type, gc); r++;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Due (yyyy-MM-dd HH:mm):"), gc);
        gc.gridx=1; form.add(due, gc); r++;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Estimated hours (blank = auto):"), gc);
        gc.gridx=1; form.add(estHours, gc); r++;

        gc.gridx=0; gc.gridy=r; form.add(new JLabel("Priority:"), gc);
        gc.gridx=1; form.add(priority, gc); r++;

        gc.gridx=0; gc.gridy=r; gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Notes:"), gc);
        gc.gridx=1;
        form.add(new JScrollPane(notes), gc);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.addActionListener(e -> { saved = true; setVisible(false); });
        cancelBtn.addActionListener(e -> { saved = false; setVisible(false); });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isSaved() { return saved; }

    public String getSelectedCourseId() {
        return ((Course) courseBox.getSelectedItem()).getId();
    }

    public String getTitleValue() { return title.getText(); }
    public TaskType getTypeValue() { return (TaskType) type.getSelectedItem(); }
    public Priority getPriorityValue() { return (Priority) priority.getSelectedItem(); }
    public String getNotesValue() { return notes.getText(); }

    public LocalDateTime getDueDateTimeValue() {
        return LocalDateTime.parse(due.getText().trim(), fmt);
    }

    public Double getEstimatedHoursOrNull() {
        String s = estHours.getText().trim();
        if (s.isEmpty()) return null;
        return Double.parseDouble(s);
    }
}
