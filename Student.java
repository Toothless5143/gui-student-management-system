// Model class for a Student
public class Student {
    private final int id;
    private String firstName;
    private String lastName;
    private String email;
    private static int nextId = 1;

    public Student(String firstName, String lastName, String email) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return firstName + " " + lastName; // Used for JComboBox display
    }
}
