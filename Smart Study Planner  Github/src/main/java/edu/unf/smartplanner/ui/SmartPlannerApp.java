package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.PlannerData;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;

public class SmartPlannerApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            PlannerData data = new PlannerData();
            PlannerService service = new PlannerService(data);

            JFrame frame = new JFrame("Smart Study Planner");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(1050, 700));
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new SmartPlannerPanel(service));
            frame.setVisible(true);
        });
    }
}
