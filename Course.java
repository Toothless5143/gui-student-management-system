// Model class for a Course
public class Course {
    private final String courseId;
    private final String courseName;

    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    // Getters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }

    @Override
    public String toString() {
        return courseName; // Used for JComboBox display
    }
}
