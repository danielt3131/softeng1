package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.Availability;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.EnumMap;

public class AvailabilityPanel extends JPanel {
    private final PlannerService service;
    private final EnumMap<DayOfWeek, JSpinner> spinners = new EnumMap<>(DayOfWeek.class);

    public AvailabilityPanel(PlannerService service) {
        super(new BorderLayout());
        this.service = service;

        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 8));
        for (DayOfWeek d : DayOfWeek.values()) {
            grid.add(new JLabel(d.toString()));
            JSpinner sp = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 12.0, 0.5));
            spinners.put(d, sp);
            grid.add(sp);
        }

        JButton apply = new JButton("Apply");
        apply.addActionListener(e -> onApply());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(apply);

        add(new JLabel("Set study availability per day (hours, 0–12):"), BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadFromModel();
    }

    private void loadFromModel() {
        Availability a = service.getData().getAvailability();
        for (DayOfWeek d : DayOfWeek.values()) {
            spinners.get(d).setValue(a.getHours(d));
        }
    }

    private void onApply() {
        try {
            Availability a = service.getData().getAvailability();
            for (DayOfWeek d : DayOfWeek.values()) {
                double hours = ((Number) spinners.get(d).getValue()).doubleValue();
                a.setHours(d, hours);
            }
            JOptionPane.showMessageDialog(this, "Availability updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
