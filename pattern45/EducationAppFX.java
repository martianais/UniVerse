import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos; 
import javafx.scene.text.TextAlignment; 

import java.util.*;
import java.util.stream.Collectors;

public class EducationAppFX extends Application {

    private final UserManager userManager = UserManager.getInstance();
    private User currentUser;
    private Scene scene;
    private VBox layout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UniVerse - Educational Platform");
        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #ADD8E6;");
        showMainMenu();

        scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainMenu() {
        layout.getChildren().clear();
         layout.setAlignment(Pos.CENTER);

        Text welcomeText = new Text("***WELCOME TO UniVerse***");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Log in");
        Button logoutButton = new Button("Exit");

        VBox buttonBox = new VBox(10, registerButton, loginButton, logoutButton);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(welcomeText, registerButton, loginButton, logoutButton);

        registerButton.setOnAction(e -> showRegisterMenu());
        loginButton.setOnAction(e -> showLoginMenu());
        logoutButton.setOnAction(e -> System.exit(0));
    }

    private void showRegisterMenu() {
        layout.getChildren().clear();

        Text registerText = new Text("Register as:");
        Button studentButton = new Button("Student");
        Button mentorButton = new Button("Mentor");

        layout.getChildren().addAll(registerText, studentButton, mentorButton);

        studentButton.setOnAction(e -> showStudentRegistrationMenu());
        mentorButton.setOnAction(e -> showMentorRegistrationMenu());
    }

    private void showStudentRegistrationMenu() {
        layout.getChildren().clear();

        Text registrationText = new Text("Student Registration");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        ComboBox<String> preferenceComboBox = new ComboBox<>();
        preferenceComboBox.getItems().addAll("Kazakhstan", "USA");

        ComboBox<String> universityComboBox = new ComboBox<>();
        universityComboBox.setDisable(true);

        preferenceComboBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if ("Kazakhstan".equals(newValue)) {
                universityComboBox.setItems(FXCollections.observableArrayList("Nazarbayev University", "Other Universities"));
            } else if ("USA".equals(newValue)) {
                universityComboBox.setItems(FXCollections.observableArrayList("Harvard", "Berkeley", "Wellesley"));
            }
            universityComboBox.setDisable(false);
        });

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Main Menu");

        layout.getChildren().addAll(registrationText, nameField, passwordField, preferenceComboBox, universityComboBox, registerButton);

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String password = passwordField.getText();
            String preference = preferenceComboBox.getValue();
            String university = universityComboBox.getValue();

            if (name.isEmpty() || password.isEmpty() || preference == null || university == null) {
                showError("Please fill all fields.");
                return;
            }

            User user = UserFactory.createUser("Student", name, password, preference, university);
            userManager.registerUser(user);
            showMessage("Student registration successful! Welcome " + name);
            goBackToMainMenu();
        });
        backButton.setOnAction(e -> showMainMenu()); 
    }

    private void showMentorRegistrationMenu() {
        layout.getChildren().clear();

        Text registrationText = new Text("Mentor Registration");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        ComboBox<String> expertiseComboBox = new ComboBox<>();
        expertiseComboBox.getItems().addAll("IELTS", "SAT", "ENT");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Main Menu");

        layout.getChildren().addAll(registrationText, nameField, passwordField, expertiseComboBox, registerButton);

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String password = passwordField.getText();
            String expertise = expertiseComboBox.getValue();

            if (name.isEmpty() || password.isEmpty() || expertise == null) {
                showError("Please fill all fields.");
                return;
            }

            User user = UserFactory.createUser("Mentor", name, password, expertise, null);
            userManager.registerUser(user);
            showMessage("Mentor registration successful! Welcome " + name);
            goBackToMainMenu();
        });
        backButton.setOnAction(e -> showMainMenu()); 
    }

    private void showLoginMenu() {
        layout.getChildren().clear();

        Text loginText = new Text("Login to your account");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Log in");

        layout.getChildren().addAll(loginText, nameField, passwordField, loginButton);

        loginButton.setOnAction(e -> {
            String name = nameField.getText();
            String password = passwordField.getText();

            currentUser = userManager.login(name, password);
            if (currentUser != null) {
                showMessage("Login successful as " + currentUser.getRole());
                showUserMenu();
            } else {
                showError("Invalid credentials. Try again.");
            }
        });
    }

    private void showUserMenu() {
        layout.getChildren().clear();

        if (currentUser instanceof Student student) {
            handleStudentMenu(student);
        } else if (currentUser instanceof Mentor mentor) {
            handleMentorMenu(mentor);
        }
    }

    private void handleStudentMenu(Student student) {
        layout.getChildren().clear();

        Text studentMenuText = new Text("Welcome " + student.getName());
        Button checklistButton = new Button("Checklist");
        Button coursesButton = new Button("Courses");
        Button viewRegisteredStudentsButton = new Button("View Registered Students");
        Button logoutButton = new Button("Log Out");

        layout.getChildren().addAll(studentMenuText, checklistButton, coursesButton, viewRegisteredStudentsButton, logoutButton);

        checklistButton.setOnAction(e -> showChecklist(student));
        coursesButton.setOnAction(e -> chooseCourse(student));
        viewRegisteredStudentsButton.setOnAction(e -> viewRegisteredStudents());
        logoutButton.setOnAction(e -> goBackToMainMenu());
    }

    private void handleMentorMenu(Mentor mentor) {
        layout.getChildren().clear();

        Text mentorMenuText = new Text("Welcome " + mentor.getName());
        Button studentsButton = new Button("My Students");
        Button materialsButton = new Button("Materials");
        Button logoutButton = new Button("Log Out");

        layout.getChildren().addAll(mentorMenuText, studentsButton, materialsButton, logoutButton);

        studentsButton.setOnAction(e -> viewMyStudents(mentor));
        materialsButton.setOnAction(e -> showMaterials());
        logoutButton.setOnAction(e -> goBackToMainMenu());
    }

    private void chooseCourse(Student student) {
        layout.getChildren().clear();
        ComboBox<String> courseComboBox = new ComboBox<>();
        courseComboBox.getItems().addAll("IELTS", "SAT", "ENT");

        Button chooseButton = new Button("Choose Course");
        Button backButton = new Button("Back to Student Menu");

        layout.getChildren().addAll(new Text("Choose a Course:"), courseComboBox, chooseButton, backButton);

        chooseButton.setOnAction(e -> {
            String course = courseComboBox.getValue();
            if (course == null) {
                showError("Please choose a course.");
                return;
            }
            List<Mentor> mentors = userManager.getAllUsers().stream()
                    .filter(user -> user instanceof Mentor && ((Mentor) user).getExpertise().equals(course))
                    .map(user -> (Mentor) user)
                    .collect(Collectors.toList());

            if (mentors.isEmpty()) {
                showErrorWithBack("No mentors available for " + course, student);
                return;
            }

            showMentorOptions(mentors, student);
        });

        backButton.setOnAction(e -> handleStudentMenu(student));
    }

    private void showErrorWithBack(String errorMessage, Student student) {
        layout.getChildren().clear();

        Text errorText = new Text(errorMessage);
        Button backButton = new Button("Back to Student Menu");

        layout.getChildren().addAll(errorText, backButton);

        backButton.setOnAction(e -> handleStudentMenu(student));
    }

    private void showMentorOptions(List<Mentor> mentors, Student student) {
        layout.getChildren().clear();

        ListView<String> mentorListView = new ListView<>();
        ObservableList<String> mentorNames = FXCollections.observableArrayList(
                mentors.stream().map(Mentor::getName).collect(Collectors.toList()));
        mentorListView.setItems(mentorNames);

        Button selectButton = new Button("Select Mentor");
        Button backButton = new Button("Back to Student Menu");

        layout.getChildren().addAll(new Text("Available Mentors:"), mentorListView, selectButton, backButton);

        selectButton.setOnAction(e -> {
            int selectedIndex = mentorListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Mentor selectedMentor = mentors.get(selectedIndex);
                student.setMentor(selectedMentor);
                selectedMentor.addStudent(student);
                showMessage("You have chosen " + selectedMentor.getName());
            } else {
                showError("Please select a mentor.");
            }
        });

        backButton.setOnAction(e -> handleStudentMenu(student));
    }

    private void showChecklist(Student student) {
        layout.getChildren().clear();

        Text checklistText = new Text("Checklist for Student:\n1. Apply for Universities\n2. Prepare Documents\n3. Prepare for Exams\n4. Write Supplementals");

        Button backButton = new Button("Back to Student Menu");

        layout.getChildren().addAll(checklistText, backButton);

        backButton.setOnAction(e -> handleStudentMenu(student));
    }

    private void showMaterials() {
        layout.getChildren().clear();
        Text materialsText = new Text("Materials for Exam Preparation:\n1. IELTS Materials\n2. SAT Materials\n3. ENT Materials");

        Button backButton = new Button("Back to Mentor Menu");

        layout.getChildren().addAll(materialsText, backButton);

        backButton.setOnAction(e -> handleMentorMenu((Mentor) currentUser));
    }

    private void viewRegisteredStudents() {
        layout.getChildren().clear();

        List<User> students = userManager.getAllUsers().stream()
                .filter(user -> user instanceof Student)
                .collect(Collectors.toList());

        ListView<String> studentsListView = new ListView<>();
        ObservableList<String> studentNames = FXCollections.observableArrayList(
                students.stream().map(User::getName).collect(Collectors.toList()));
        studentsListView.setItems(studentNames);

        Button backButton = new Button("Back to Student Menu");

        layout.getChildren().addAll(new Text("Registered Students:"), studentsListView, backButton);

        backButton.setOnAction(e -> handleStudentMenu((Student) currentUser));
    }

    private void viewMyStudents(Mentor mentor) {
        layout.getChildren().clear();

        List<Student> students = mentor.getMyStudents();
        ListView<String> studentsListView = new ListView<>();
        ObservableList<String> studentNames = FXCollections.observableArrayList(
                students.stream().map(Student::getName).collect(Collectors.toList()));
        studentsListView.setItems(studentNames);

        Button backButton = new Button("Back to Mentor Menu");

        layout.getChildren().addAll(new Text("Your Students:"), studentsListView, backButton);

        backButton.setOnAction(e -> handleMentorMenu(mentor));
    }

    private void showMessage(String message) {
        layout.getChildren().clear();

        Text messageText = new Text(message);
        Button backButton = new Button("Back to Main Menu");

        layout.getChildren().addAll(messageText, backButton);

        backButton.setOnAction(e -> goBackToMainMenu());
    }

    private void showError(String error) {
        layout.getChildren().clear();

        Text errorText = new Text(error);
        Button backButton = new Button("Back to Main Menu");

        layout.getChildren().addAll(errorText, backButton);

        backButton.setOnAction(e -> goBackToMainMenu());
    }

    private void goBackToMainMenu() {
        showMainMenu();
    }
}
