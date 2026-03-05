package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;

public class SmartPlannerPanel extends JPanel {

    public SmartPlannerPanel(PlannerService service) {
        super(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Profile", new ProfilePanel(service));
        tabs.addTab("Courses", new CoursesPanel(service));
        tabs.addTab("Tasks", new TasksPanel(service));
        tabs.addTab("Availability", new AvailabilityPanel(service));
        tabs.addTab("Plan", new PlanPanel(service));

        add(tabs, BorderLayout.CENTER);
        add(new StorageToolbar(service, tabs), BorderLayout.NORTH);
    }
}
