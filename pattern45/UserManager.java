import java.util.*;

public class UserManager {
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
