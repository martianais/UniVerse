public class UserFactory {
    public static User createUser(String type, String name, String password, String preference, String detail) {
        if ("Student".equalsIgnoreCase(type)) {
            return new Student(name, password, preference, detail);
        } else if ("Mentor".equalsIgnoreCase(type)) {
            return new Mentor(name, password, preference);
        }
        throw new IllegalArgumentException("Invalid user type.");
    }
}
