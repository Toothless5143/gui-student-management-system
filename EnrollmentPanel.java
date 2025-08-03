import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentPanel extends JPanel {
    private final DataManager dataManager;
    private final JComboBox<Course> courseComboBox;
    private final JTable eligibleStudentsTable;
    private final EligibleStudentTableModel tableModel;

    public EnrollmentPanel(DataManager dm) {
        this.dataManager = dm;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel for course selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course:"));
        courseComboBox = new JComboBox<>(dataManager.getAllCourses().toArray(new Course[0]));
        topPanel.add(courseComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel for table of eligible students
        tableModel = new EligibleStudentTableModel();
        eligibleStudentsTable = new JTable(tableModel);
        add(new JScrollPane(eligibleStudentsTable), BorderLayout.CENTER);

        // Bottom Panel for enrollment button
        JPanel bottomPanel = new JPanel();
        JButton enrollButton = new JButton("Enroll Student");
        bottomPanel.add(enrollButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listener for ComboBox to update the table
        courseComboBox.addActionListener(e -> updateEligibleStudentsTable());

        // Event listener for Enroll button
        enrollButton.addActionListener(e -> {
            int selectedRow = eligibleStudentsTable.getSelectedRow();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();

            if (selectedRow == -1 || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course and a student.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student selectedStudent = tableModel.getStudentAt(selectedRow);
            dataManager.enrollStudent(selectedStudent.getId(), selectedCourse.getCourseId());

            JOptionPane.showMessageDialog(this, "Student enrolled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateEligibleStudentsTable(); // Refresh the list
        });

        // Initial population
        updateEligibleStudentsTable();
    }

    private void updateEligibleStudentsTable() {
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        if (selectedCourse != null) {
            List<Student> eligibleStudents = dataManager.getEligibleStudentsForCourse(selectedCourse);
            tableModel.setStudents(eligibleStudents);
        }
    }

    private static class EligibleStudentTableModel extends AbstractTableModel {
        private List<Student> students = new ArrayList<>();
        private final String[] columnNames = {"ID", "Full Name"};

        public void setStudents(List<Student> students) {
            this.students = students;
            fireTableDataChanged();
        }

        public Student getStudentAt(int row) { return students.get(row); }
        @Override public int getRowCount() { return students.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int col) { return columnNames[col]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Student s = students.get(rowIndex);
            switch (columnIndex) {
                case 0: return s.getId();
                case 1: return s.getFirstName() + " " + s.getLastName();
                default: return null;
            }
        }
    }
}
