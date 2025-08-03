import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GradePanel extends JPanel {
    private final DataManager dataManager;
    private final JComboBox<Student> studentComboBox;
    private final JTable gradesTable;
    private final GradeTableModel tableModel;
    private final JTextField gradeField = new JTextField(10);

    public GradePanel(DataManager dm) {
        this.dataManager = dm;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel for student selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Student:"));
        studentComboBox = new JComboBox<>(dataManager.getAllStudents().toArray(new Student[0]));
        topPanel.add(studentComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel for grades table
        tableModel = new GradeTableModel(dataManager);
        gradesTable = new JTable(tableModel);
        add(new JScrollPane(gradesTable), BorderLayout.CENTER);

        // Bottom Panel for grade assignment
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Enter Grade:"));
        bottomPanel.add(gradeField);
        JButton assignButton = new JButton("Assign Grade");
        bottomPanel.add(assignButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listener to update table when student selection changes
        studentComboBox.addActionListener(e -> updateGradesTable());

        // Listener for assigning a grade
        assignButton.addActionListener(e -> {
            int selectedRow = gradesTable.getSelectedRow();
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            String grade = gradeField.getText().trim();

            if (selectedStudent == null || selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student and a course.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a grade.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Enrollment enrollment = tableModel.getEnrollmentAt(selectedRow);
            dataManager.assignGrade(enrollment.getStudentId(), enrollment.getCourseId(), grade);

            JOptionPane.showMessageDialog(this, "Grade assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            gradeField.setText("");
            tableModel.fireTableRowsUpdated(selectedRow, selectedRow); // Refresh the row
        });

        // Initial population
        updateGradesTable();
    }

    private void updateGradesTable() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            List<Enrollment> enrollments = dataManager.getEnrollmentsForStudent(selectedStudent);
            tableModel.setEnrollments(enrollments);
        }
    }

    private static class GradeTableModel extends AbstractTableModel {
        private final DataManager dataManager;
        private List<Enrollment> enrollments = new ArrayList<>();
        private final String[] columnNames = {"Course ID", "Course Name", "Grade"};

        public GradeTableModel(DataManager dm) { this.dataManager = dm; }

        public void setEnrollments(List<Enrollment> enrollments) {
            this.enrollments = enrollments;
            fireTableDataChanged();
        }

        public Enrollment getEnrollmentAt(int row) { return enrollments.get(row); }
        @Override public int getRowCount() { return enrollments.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int col) { return columnNames[col]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Enrollment e = enrollments.get(rowIndex);
            Course c = dataManager.getCourseById(e.getCourseId());
            switch (columnIndex) {
                case 0: return e.getCourseId();
                case 1: return (c != null) ? c.getCourseName() : "Unknown";
                case 2: return e.getGrade();
                default: return null;
            }
        }
    }
}
