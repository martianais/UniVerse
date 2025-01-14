public class Student extends User {
    private String preference;
    private String university;
    private Mentor mentor;

    public Student(String name, String password, String preference, String university) {
        super(name, password);
        this.preference = preference;
        this.university = university;
    }

    public String getPreference() {
        return preference;
    }

    public String getUniversity() {
        return university;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
