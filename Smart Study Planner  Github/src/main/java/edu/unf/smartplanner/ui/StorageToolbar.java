package edu.unf.smartplanner.ui;

import edu.unf.smartplanner.model.PlannerData;
import edu.unf.smartplanner.persistence.LocalStorage;
import edu.unf.smartplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StorageToolbar extends JPanel {

    private final LocalStorage storage = new LocalStorage();
    private final PlannerService service;

    public StorageToolbar(PlannerService service, JTabbedPane tabs) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.service = service;

        JButton save = new JButton("Save...");
        JButton load = new JButton("Load...");

        save.addActionListener(e -> onSave());
        load.addActionListener(e -> onLoad(tabs));

        add(save);
        add(load);
        add(new JLabel("Tip: Save file extension: .ssp"));
    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Planner Data");
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            storage.save(service.getData(), file);
            JOptionPane.showMessageDialog(this, "Saved to: " + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLoad(JTabbedPane tabs) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Planner Data");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            PlannerData loaded = storage.load(file);
            // Replace the data inside service by copying fields (avoid re-creating the whole UI/service)
            service.getData().setProfile(loaded.getProfile());
            service.getData().setAvailability(loaded.getAvailability());
            service.getData().setCourses(loaded.getCourses());
            service.getData().setTasks(loaded.getTasks());

            // Force refresh by switching tabs (simple and reliable)
            int idx = tabs.getSelectedIndex();
            tabs.setSelectedIndex((idx + 1) % tabs.getTabCount());
            tabs.setSelectedIndex(idx);

            JOptionPane.showMessageDialog(this, "Loaded from: " + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
