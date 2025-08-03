import javax.swing.*;

public class StudentManagementSystem {

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Central data manager for the whole application
            DataManager dataManager = new DataManager();

            JTabbedPane tabbedPane = new JTabbedPane();

            // Create panels for each tab
            JPanel studentPanel = new StudentPanel(dataManager);
            JPanel enrollmentPanel = new EnrollmentPanel(dataManager);
            JPanel gradePanel = new GradePanel(dataManager);

            // Add tabs to the pane
            tabbedPane.addTab("Student Management", studentPanel);
            tabbedPane.addTab("Course Enrollment", enrollmentPanel);
            tabbedPane.addTab("Grade Management", gradePanel);

            frame.add(tabbedPane);
            frame.setLocationRelativeTo(null); // Center the window
            frame.setVisible(true);
        });
    }
}
