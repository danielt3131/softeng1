package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.UserProfile;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private final PlannerService service;

    private final JTextField nameField = new JTextField(20);
    private final JSpinner weeklyGoal = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 100.0, 0.5));
    private final JComboBox<Integer> blockMinutes = new JComboBox<>(new Integer[]{30, 45, 60, 90, 120});

    public ProfilePanel(PlannerService service) {
        super(new BorderLayout());
        this.service = service;

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("Name:"), gc);
        gc.gridx = 1;
        form.add(nameField, gc);

        gc.gridx = 0; gc.gridy = 1;
        form.add(new JLabel("Weekly Study Goal (hours):"), gc);
        gc.gridx = 1;
        form.add(weeklyGoal, gc);

        gc.gridx = 0; gc.gridy = 2;
        form.add(new JLabel("Preferred Block Length (minutes):"), gc);
        gc.gridx = 1;
        form.add(blockMinutes, gc);

        JButton apply = new JButton("Apply");
        apply.addActionListener(e -> onApply());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(apply);

        add(form, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);

        loadFromModel();
    }

    private void loadFromModel() {
        UserProfile p = service.getData().getProfile();
        nameField.setText(p.getName());
        weeklyGoal.setValue(p.getWeeklyStudyGoalHours());
        blockMinutes.setSelectedItem(p.getPreferredBlockMinutes());
    }

    private void onApply() {
        try {
            UserProfile updated = new UserProfile(
                    nameField.getText(),
                    ((Number) weeklyGoal.getValue()).doubleValue(),
                    (Integer) blockMinutes.getSelectedItem()
            );
            service.getData().setProfile(updated);
            JOptionPane.showMessageDialog(this, "Profile updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
