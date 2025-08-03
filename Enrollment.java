// Model class for an Enrollment record
public class Enrollment {
    private final int studentId;
    private final String courseId;
    private String grade;

    public Enrollment(int studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = "N/A"; // Default grade
    }

    // Getters
    public int getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public String getGrade() { return grade; }

    // Setter
    public void setGrade(String grade) { this.grade = grade; }
}
