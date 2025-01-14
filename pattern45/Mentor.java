import java.util.ArrayList;
import java.util.List;

public class Mentor extends User implements Observer {
    private String expertise;
    private final List<Student> myStudents = new ArrayList<>();

    public Mentor(String name, String password, String expertise) {
        super(name, password);
        this.expertise = expertise;
    }

    public String getExpertise() {
        return expertise;
    }

    public List<Student> getMyStudents() {
        return myStudents;
    }

    public void addStudent(Student student) {
        if (!myStudents.contains(student)) {
            myStudents.add(student);
        }
    }

    @Override
    public void update(Student student) {
        System.out.println("Mentor " + getName() + " has a new student: " + student.getName());
    }

    @Override
    public String getRole() {
        return "Mentor";
    }
}
