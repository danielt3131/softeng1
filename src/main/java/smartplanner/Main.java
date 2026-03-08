package smartplanner;

import javax.swing.SwingUtilities;
import smartplanner.model.Planner;
import smartplanner.service.PlanGenerator;
import smartplanner.ui.PlannerFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Planner planner = new Planner();
            PlannerFrame frame = new PlannerFrame(planner, new PlanGenerator());
            frame.setVisible(true);
        });
    }
}
