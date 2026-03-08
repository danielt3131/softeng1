package smartplanner.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import smartplanner.model.Course;
import smartplanner.model.Planner;
import smartplanner.model.Priority;
import smartplanner.model.Profile;
import smartplanner.model.StudyBlock;
import smartplanner.model.StudyPlan;
import smartplanner.model.Task;
import smartplanner.model.TaskType;
import smartplanner.persistence.PlannerSerializer;
import smartplanner.service.PlanGenerator;

public class PlannerFrame extends JFrame {
    private final Planner planner;
    private final PlanGenerator generator;
    private final PlannerSerializer serializer;

    private final JTextField nameField;
    private final JTextField majorField;
    private final JSpinner preferredMinutesSpinner;

    private final JTextField courseCodeField;
    private final JTextField courseNameField;
    private final JTextField instructorField;
    private final JList<Course> courseList;

    private final JTextField taskTitleField;
    private final JComboBox<Course> taskCourseBox;
    private final JComboBox<TaskType> taskTypeBox;
    private final JComboBox<Priority> taskPriorityBox;
    private final JTextField taskDueDateField;
    private final JSpinner taskMinutesSpinner;
    private final JList<Task> taskList;

    private final JSpinner[] availabilitySpinners;
    private final JTextArea planArea;

    public PlannerFrame(Planner planner, PlanGenerator generator) {
        this.planner = planner;
        this.generator = generator;
        this.serializer = new PlannerSerializer();

        setTitle("Smart Study Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        setJMenuBar(buildMenuBar());

        nameField = new JTextField(15);
        majorField = new JTextField(15);
        preferredMinutesSpinner = new JSpinner(new SpinnerNumberModel(120, 30, 600, 15));

        courseCodeField = new JTextField(8);
        courseNameField = new JTextField(12);
        instructorField = new JTextField(12);
        courseList = new JList<>();
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        taskTitleField = new JTextField(12);
        taskCourseBox = new JComboBox<>();
        taskTypeBox = new JComboBox<>(TaskType.values());
        taskPriorityBox = new JComboBox<>(Priority.values());
        taskDueDateField = new JTextField("2026-03-15", 10);
        taskMinutesSpinner = new JSpinner(new SpinnerNumberModel(60, 15, 600, 15));
        taskList = new JList<>();
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        availabilitySpinners = new JSpinner[DayOfWeek.values().length];
        planArea = new JTextArea();
        planArea.setEditable(false);

        add(buildLeftPanel(), BorderLayout.WEST);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildRightPanel(), BorderLayout.EAST);

        refreshCourseViews();
        refreshTaskList();
        refreshPlanArea(new StudyPlan());
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save Planner");
        saveItem.addActionListener(event -> savePlanner());
        JMenuItem loadItem = new JMenuItem("Load Planner");
        loadItem.addActionListener(event -> loadPlanner());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        bar.add(fileMenu);
        return bar;
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(340, 650));

        JPanel profilePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile"));
        profilePanel.add(new JLabel("Student Name"));
        profilePanel.add(nameField);
        profilePanel.add(new JLabel("Major"));
        profilePanel.add(majorField);
        profilePanel.add(new JLabel("Daily Minutes"));
        profilePanel.add(preferredMinutesSpinner);
        JButton saveProfileButton = new JButton("Save Profile");
        saveProfileButton.addActionListener(event -> saveProfile());
        profilePanel.add(saveProfileButton);

        JPanel coursePanel = new JPanel(new BorderLayout(5, 5));
        coursePanel.setBorder(BorderFactory.createTitledBorder("Courses"));
        JPanel courseForm = new JPanel(new GridLayout(0, 2, 5, 5));
        courseForm.add(new JLabel("Code"));
        courseForm.add(courseCodeField);
        courseForm.add(new JLabel("Name"));
        courseForm.add(courseNameField);
        courseForm.add(new JLabel("Instructor"));
        courseForm.add(instructorField);
        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(event -> addCourse());
        courseForm.add(addCourseButton);
        coursePanel.add(courseForm, BorderLayout.NORTH);
        coursePanel.add(new JScrollPane(courseList), BorderLayout.CENTER);

        JPanel availabilityPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        availabilityPanel.setBorder(BorderFactory.createTitledBorder("Weekly Availability"));
        int index = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            availabilityPanel.add(new JLabel(day.name()));
            availabilitySpinners[index] = new JSpinner(new SpinnerNumberModel(120, 0, 480, 15));
            availabilityPanel.add(availabilitySpinners[index]);
            index++;
        }
        JButton saveAvailabilityButton = new JButton("Save Availability");
        saveAvailabilityButton.addActionListener(event -> saveAvailability());
        availabilityPanel.add(saveAvailabilityButton);

        panel.add(profilePanel);
        panel.add(coursePanel);
        panel.add(availabilityPanel);
        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel taskPanel = new JPanel(new BorderLayout(5, 5));
        taskPanel.setBorder(BorderFactory.createTitledBorder("Tasks"));
        JPanel taskForm = new JPanel(new GridLayout(0, 2, 5, 5));
        taskForm.add(new JLabel("Title"));
        taskForm.add(taskTitleField);
        taskForm.add(new JLabel("Course"));
        taskForm.add(taskCourseBox);
        taskForm.add(new JLabel("Type"));
        taskForm.add(taskTypeBox);
        taskForm.add(new JLabel("Priority"));
        taskForm.add(taskPriorityBox);
        taskForm.add(new JLabel("Due Date (YYYY-MM-DD)"));
        taskForm.add(taskDueDateField);
        taskForm.add(new JLabel("Estimated Minutes"));
        taskForm.add(taskMinutesSpinner);
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(event -> addTask());
        taskForm.add(addTaskButton);
        taskPanel.add(taskForm, BorderLayout.NORTH);
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton completeTaskButton = new JButton("Mark Complete");
        completeTaskButton.addActionListener(event -> markSelectedTaskComplete());
        JButton generateButton = new JButton("Generate Plan");
        generateButton.addActionListener(event -> generatePlan());
        JButton refreshButton = new JButton("Refresh Lists");
        refreshButton.addActionListener(event -> {
            refreshCourseViews();
            refreshTaskList();
        });
        actions.add(completeTaskButton);
        actions.add(generateButton);
        actions.add(refreshButton);

        panel.add(taskPanel, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generated Study Plan"));
        panel.setPreferredSize(new Dimension(320, 650));
        panel.add(new JScrollPane(planArea), BorderLayout.CENTER);
        return panel;
    }

    private void saveProfile() {
        try {
            planner.setProfile(new Profile(nameField.getText(), majorField.getText(), (Integer) preferredMinutesSpinner.getValue()));
            showInfo("Profile saved.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void addCourse() {
        try {
            planner.addCourse(new Course(courseCodeField.getText(), courseNameField.getText(), instructorField.getText()));
            refreshCourseViews();
            courseCodeField.setText("");
            courseNameField.setText("");
            instructorField.setText("");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void addTask() {
        if (taskCourseBox.getSelectedItem() == null) {
            showError("Add a course before creating tasks.");
            return;
        }
        try {
            Task task = new Task(
                    taskTitleField.getText(),
                    (Course) taskCourseBox.getSelectedItem(),
                    (TaskType) taskTypeBox.getSelectedItem(),
                    (Priority) taskPriorityBox.getSelectedItem(),
                    LocalDate.parse(taskDueDateField.getText().trim()),
                    (Integer) taskMinutesSpinner.getValue()
            );
            planner.addTask(task);
            refreshTaskList();
            taskTitleField.setText("");
        } catch (Exception exception) {
            showError("Could not add task. Check title, course, date, and time.");
        }
    }

    private void saveAvailability() {
        int index = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            planner.getAvailability().setMinutes(day, (Integer) availabilitySpinners[index].getValue());
            index++;
        }
        showInfo("Availability saved.");
    }

    private void markSelectedTaskComplete() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask == null) {
            showError("Select a task first.");
            return;
        }
        planner.markTaskCompleted(selectedTask);
        refreshTaskList();
    }

    private void generatePlan() {
        saveAvailability();
        StudyPlan plan = generator.generate(planner);
        refreshPlanArea(plan);
    }

    private void refreshCourseViews() {
        courseList.setListData(planner.getCourses().toArray(new Course[0]));
        taskCourseBox.setModel(new DefaultComboBoxModel<>(planner.getCourses().toArray(new Course[0])));
    }

    private void refreshTaskList() {
        taskList.setListData(planner.getTasks().toArray(new Task[0]));
    }

    private void refreshPlanArea(StudyPlan plan) {
        if (plan.isEmpty()) {
            planArea.setText("No study plan generated yet.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        DayOfWeek currentDay = null;
        for (StudyBlock block : plan.getBlocks()) {
            if (currentDay != block.getDay()) {
                currentDay = block.getDay();
                builder.append(currentDay.name()).append('\n');
            }
            builder.append("  - ")
                    .append(block.getTask().getTitle())
                    .append(" (")
                    .append(block.getTask().getCourse().getCode())
                    .append(") ")
                    .append(block.getMinutes())
                    .append(" mins\n");
        }
        planArea.setText(builder.toString());
    }

    private void savePlanner() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                serializer.save(planner, chooser.getSelectedFile().toPath());
                showInfo("Planner saved.");
            } catch (Exception exception) {
                showError("Save failed.");
            }
        }
    }

    private void loadPlanner() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Planner loaded = serializer.load(Path.of(chooser.getSelectedFile().getAbsolutePath()));
                planner.setProfile(loaded.getProfile());
                planner.setAvailability(loaded.getAvailability());
                for (Course existing : planner.getCourses().toArray(new Course[0])) {
                    // immutable view, no-op placeholder for simplicity
                }
                replacePlannerContents(loaded);
                populateFieldsFromPlanner();
                refreshCourseViews();
                refreshTaskList();
                refreshPlanArea(planner.getStudyPlan());
                showInfo("Planner loaded.");
            } catch (Exception exception) {
                showError("Load failed.");
            }
        }
    }

    private void replacePlannerContents(Planner loaded) {
        Planner target = this.planner;
        try {
            java.lang.reflect.Field coursesField = Planner.class.getDeclaredField("courses");
            java.lang.reflect.Field tasksField = Planner.class.getDeclaredField("tasks");
            coursesField.setAccessible(true);
            tasksField.setAccessible(true);
            java.util.List<Course> targetCourses = (java.util.List<Course>) coursesField.get(target);
            java.util.List<Task> targetTasks = (java.util.List<Task>) tasksField.get(target);
            targetCourses.clear();
            targetTasks.clear();
            targetCourses.addAll(loaded.getCourses());
            targetTasks.addAll(loaded.getTasks());
            target.setStudyPlan(loaded.getStudyPlan());
        } catch (Exception exception) {
            throw new IllegalStateException("Could not replace planner contents.", exception);
        }
    }

    private void populateFieldsFromPlanner() {
        Profile profile = planner.getProfile();
        nameField.setText(profile.getStudentName());
        majorField.setText(profile.getMajor());
        preferredMinutesSpinner.setValue(profile.getPreferredDailyStudyMinutes());

        int index = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            availabilitySpinners[index].setValue(planner.getAvailability().getMinutes(day));
            index++;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Smart Study Planner", JOptionPane.INFORMATION_MESSAGE);
    }
}
