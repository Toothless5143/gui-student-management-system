import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Manages all application data (in-memory)
public class DataManager {
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    public DataManager() {
        // Pre-populate with some sample data
        students.add(new Student("John", "Doe", "john.doe@example.com"));
        students.add(new Student("Jane", "Smith", "jane.smith@example.com"));
        students.add(new Student("Peter", "Jones", "peter.jones@example.com"));

        courses.add(new Course("CS101", "Introduction to Computer Science"));
        courses.add(new Course("MA203", "Calculus I"));
        courses.add(new Course("EN101", "English Composition"));
    }

    // Student Management
    public List<Student> getAllStudents() { return students; }
    public void addStudent(Student student) { students.add(student); }
    public Student getStudentById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    // Course Management
    public List<Course> getAllCourses() { return courses; }
    public Course getCourseById(String id) {
        return courses.stream().filter(c -> c.getCourseId().equals(id)).findFirst().orElse(null);
    }

    // Enrollment and Grade Management
    public void enrollStudent(int studentId, String courseId) {
        enrollments.add(new Enrollment(studentId, courseId));
    }

    public List<Student> getEligibleStudentsForCourse(Course course) {
        List<Integer> enrolledStudentIds = enrollments.stream()
                .filter(e -> e.getCourseId().equals(course.getCourseId()))
                .map(Enrollment::getStudentId)
                .collect(Collectors.toList());

        return students.stream()
                .filter(s -> !enrolledStudentIds.contains(s.getId()))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForStudent(Student student) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == student.getId())
                .collect(Collectors.toList());
    }

    public void assignGrade(int studentId, String courseId, String grade) {
        enrollments.stream()
            .filter(e -> e.getStudentId() == studentId && e.getCourseId().equals(courseId))
            .findFirst()
            .ifPresent(e -> e.setGrade(grade));
    }
}
