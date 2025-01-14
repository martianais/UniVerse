public class UserDecorator {
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
