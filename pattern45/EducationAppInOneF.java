import java.util.*;
import java.util.stream.Collectors;

public class EducationApp {
    public static void main(String[] args) {
        MenuFacade menu = new MenuFacade();
        menu.showMainMenu();
    }
}

class MenuFacade {
    private final Scanner scanner = new Scanner(System.in);
    private final UserManager userManager = UserManager.getInstance();
    private User currentUser;

    public void showMainMenu() {
        while (true) {
            System.out.println("\n***WELCOME TO UniVerse***"); 
            System.out.println("\n==Main Menu==");
            System.out.println("1. Register user");
            System.out.println("2. Log in");
            System.out.println("3. Log out");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("See you soon! ");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void registerUser() {
        System.out.println("==Choose your preference==");
        System.out.println("1. Student");
        System.out.println("2. Mentor");
        String type = scanner.nextInt() == 1 ? "Student" : "Mentor";
        scanner.nextLine();

        System.out.print("Enter username: ");
        String name = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if ("Student".equals(type)) {
            System.out.println("==Choose location==  ");
            System.out.println("1. Kazakhstan Universities");
            System.out.println("2. USA Universities");
            String preference = scanner.nextInt() == 1 ? "Kazakhstan" : "USA";
            scanner.nextLine();

            String university = "";
            if ("Kazakhstan".equals(preference)) {
                System.out.println("==Choose your university type preference==");
                System.out.println("1. Nazarbayev University(NU)");
                System.out.println("2. Other Universities");
                university = scanner.nextInt() == 1 ? "Other Universities" : "Nazarbayev University";
            } else {
                System.out.println("==Choose a university== ");
                System.out.println("1. Harvard University (Ivy League)");
                System.out.println("2. Berkeley University(Top 100)");
                System.out.println("3. Wellesley College(Liberal Art University)"); 
                university = switch (scanner.nextInt()) {
                    case 1 -> "Harvard";
                    case 2 -> "Berkeley";
                    case 3 -> "Wellesley";
                    default -> "Unknown";
                };
            }
            scanner.nextLine();

            User user = UserFactory.createUser("Student", name, password, preference, university);
            userManager.registerUser(user);
        } else {
            System.out.println("==Choose expertise==");
            System.out.println("1. IELTS");
            System.out.println("2. SAT"); 
            System.out.println("3. ENT"); 
            String expertise = switch (scanner.nextInt()) {
                case 1 -> "IELTS";
                case 2 -> "SAT";
                case 3 -> "ENT";
                default -> "Unknown";
            };
            scanner.nextLine();

            User user = UserFactory.createUser("Mentor", name, password, expertise, null);
            userManager.registerUser(user);
        }
        System.out.println("Registration successful! Let's get you into University");
    }

    private void loginUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userManager.login(name, password);
        if (currentUser != null) {
            System.out.println("Login successful as " + currentUser.getRole());
            showUserMenu();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void showUserMenu() {
        if (currentUser instanceof Student student) {
            handleStudentMenu(student);
        } else if (currentUser instanceof Mentor mentor) {
            handleMentorMenu(mentor);
        }
    }

    private void handleStudentMenu(Student student) {
        while (true) {
            System.out.println("\n==Student Menu==");
            System.out.println("1. Checklist");
            System.out.println("2. Courses");
            System.out.println("3. View registered students");
            System.out.println("4. Log out");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> System.out.println("Checklist:\n 1. Apply for Universities \n 2. Prepare required documents \n 3. Prepare for exams(SAT,IELTS,NUET,ENT) \n 4. Write Writing Supplementals(Motivational Letter or Personal Statement)"); 
                case 2 -> chooseCourse(student);
                case 3 -> viewRegisteredStudents();
                case 4 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleMentorMenu(Mentor mentor) {
        while (true) {
            System.out.println("\n==Mentor Menu==");
            System.out.println("1. My students");
            System.out.println("2. Materials");
            System.out.println("3. Log out");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> viewMyStudents(mentor);
                case 2 -> showMaterials();
                case 3 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showMaterials() {
        System.out.println("Materials for IELTS:");
        System.out.println("1. Cambridge IELTS Series");
        System.out.println("2. The Official Cambridge Guide to IELTS");
        System.out.println("3. Barron’s IELTS Superpack");

        System.out.println("\nMaterials for SAT:");
        System.out.println("1. The Official SAT Study Guide");
        System.out.println("2. Barron’s SAT");
        System.out.println("3. Princeton Review: Cracking the SAT");

        System.out.println("\nMaterials for ENT:");
        System.out.println("1. YБТ КoмекшiМатериалдар");
        System.out.println("2. Mathematics for ENT by Zhambyl Zhumabayev");
        System.out.println("3. Physics ENT Prep by Zambak");
    }

    private void chooseCourse(Student student) {
        System.out.println("Choose a preference:");
        System.out.println("1. IELTS");
        System.out.println("2. SAT"); 
        System.out.println("3. ENT"); 

        String course = switch (scanner.nextInt()) {
            case 1 -> "IELTS";
            case 2 -> "SAT";
            case 3 -> "ENT";
            default -> "Unknown";
        };
        scanner.nextLine();

        List<Mentor> mentors = userManager.getAllUsers().stream()
                .filter(user -> user instanceof Mentor && ((Mentor) user).getExpertise().equals(course))
                .map(user -> (Mentor) user)
                .collect(Collectors.toList());

        if (mentors.isEmpty()) {
            System.out.println("No mentors available for " + course);
            return;
        }

        System.out.println("Available mentors:");
        for (int i = 0; i < mentors.size(); i++) {
            System.out.println((i + 1) + ". " + mentors.get(i).getName());
        }

        System.out.print("Choose a mentor: ");
        int mentorIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (mentorIndex >= 0 && mentorIndex < mentors.size()) {
            Mentor chosenMentor = mentors.get(mentorIndex);
            student.setMentor(chosenMentor);
            chosenMentor.addStudent(student);
            chosenMentor.update(student); // Notify the mentor
            System.out.println("You have chosen " + chosenMentor.getName());
            System.out.println("Wait and Mentor will contact you");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewRegisteredStudents() {
        List<User> students = userManager.getAllUsers().stream()
                .filter(user -> user instanceof Student)
                .collect(Collectors.toList());

        System.out.println("Registered students:");
        for (User student : students) {
            UserDecorator decorator = new UserDecorator(student);
            System.out.println("- " + decorator.getDetailedInfo());
        }
    }

    private void viewMyStudents(Mentor mentor) {
        List<Student> myStudents = mentor.getMyStudents();
        if (myStudents.isEmpty()) {
            System.out.println("You have no students yet.");
        } else {
            System.out.println("My students:");
            for (Student student : myStudents) {
                System.out.println("- " + student.getName());
            }
        }
    }
}

abstract class User {
    private final String name;
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getRole();
}

class Student extends User {
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

class Mentor extends User implements Observer {
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

interface Observer {
    void update(Student student);
}

class UserFactory {
    public static User createUser(String type, String name, String password, String preference, String detail) {
        if ("Student".equalsIgnoreCase(type)) {
            return new Student(name, password, preference, detail);
        } else if ("Mentor".equalsIgnoreCase(type)) {
            return new Mentor(name, password, preference);
        }
        throw new IllegalArgumentException("Invalid user type.");
    }
}

class UserDecorator {
    private final User user;

    public UserDecorator(User user) {
        this.user = user;
    }

    public String getDetailedInfo() {
        if (user instanceof Student student) {
            return user.getName() + " (Student, University: " + student.getUniversity() + ")";
        } else if (user instanceof Mentor mentor) {
            return user.getName() + " (Mentor, Expertise: " + mentor.getExpertise() + ")";
        }
        return user.getName();
    }
}

class UserManager {
    private static UserManager instance;
    private final Map<String, User> users = new HashMap<>();

    private UserManager() {}

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void registerUser(User user) {
        users.put(user.getName(), user);
    }

    public User login(String name, String password) {
        User user = users.get(name);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
