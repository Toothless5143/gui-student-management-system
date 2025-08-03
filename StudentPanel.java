import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private final DataManager dataManager;
    private final JTable studentTable;
    private final StudentTableModel tableModel;

    public StudentPanel(DataManager dm) {
        this.dataManager = dm;
        setLayout(new BorderLayout());

        // Table Model and JTable
        tableModel = new StudentTableModel(dataManager.getAllStudents());
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Student Action
        addButton.addActionListener(e -> {
            AddUpdateStudentDialog dialog = new AddUpdateStudentDialog(null, "Add Student");
            if (dialog.isSaved()) {
                Student newStudent = new Student(
                    dialog.getFirstName(), dialog.getLastName(), dialog.getEmail()
                );
                dataManager.addStudent(newStudent);
                tableModel.fireTableDataChanged(); // Refresh table
            }
        });

        // Update Student Action
        updateButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student studentToUpdate = tableModel.getStudentAt(selectedRow);
            AddUpdateStudentDialog dialog = new AddUpdateStudentDialog(studentToUpdate, "Update Student");
            if (dialog.isSaved()) {
                studentToUpdate.setFirstName(dialog.getFirstName());
                studentToUpdate.setLastName(dialog.getLastName());
                studentToUpdate.setEmail(dialog.getEmail());
                tableModel.fireTableRowsUpdated(selectedRow, selectedRow); // Refresh specific row
            }
        });
    }

    // Custom TableModel for Students
    private static class StudentTableModel extends AbstractTableModel {
        private final List<Student> students;
        private final String[] columnNames = {"ID", "First Name", "Last Name", "Email"};

        public StudentTableModel(List<Student> students) {
            this.students = students;
        }

        public Student getStudentAt(int row) {
            return students.get(row);
        }

        @Override public int getRowCount() { return students.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int column) { return columnNames[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Student student = students.get(rowIndex);
            switch (columnIndex) {
                case 0: return student.getId();
                case 1: return student.getFirstName();
                case 2: return student.getLastName();
                case 3: return student.getEmail();
                default: return null;
            }
        }
    }
}

// Dialog for adding/updating students
class AddUpdateStudentDialog extends JDialog {
    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private boolean saved = false;

    public AddUpdateStudentDialog(Student student, String title) {
        setTitle(title);
        setModal(true);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Email:"));
        add(emailField);

        if (student != null) { // Pre-fill for updating
            firstNameField.setText(student.getFirstName());
            lastNameField.setText(student.getLastName());
            emailField.setText(student.getEmail());
        }

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            if (firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        add(saveButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getFirstName() { return firstNameField.getText(); }
    public String getLastName() { return lastNameField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public boolean isSaved() { return saved; }
}
