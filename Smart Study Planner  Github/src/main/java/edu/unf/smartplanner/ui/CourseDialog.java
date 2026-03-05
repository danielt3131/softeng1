package edu.unf.smartplanner.ui;

import javax.swing.*;
import java.awt.*;

public class CourseDialog extends JDialog {
    private boolean saved = false;

    private final JTextField name = new JTextField(20);
    private final JSpinner credits = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
    private final JTextField tag = new JTextField(20);

    public CourseDialog(Window owner) {
        super(owner, "Add Course", ModalityType.APPLICATION_MODAL);
        setSize(420, 220);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx=0; gc.gridy=0; form.add(new JLabel("Course name:"), gc);
        gc.gridx=1; form.add(name, gc);

        gc.gridx=0; gc.gridy=1; form.add(new JLabel("Credit hours (1-6):"), gc);
        gc.gridx=1; form.add(credits, gc);

        gc.gridx=0; gc.gridy=2; form.add(new JLabel("Tag (optional):"), gc);
        gc.gridx=1; form.add(tag, gc);

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
    public String getNameValue() { return name.getText(); }
    public int getCreditHoursValue() { return ((Number) credits.getValue()).intValue(); }
    public String getTagValue() { return tag.getText(); }
}
