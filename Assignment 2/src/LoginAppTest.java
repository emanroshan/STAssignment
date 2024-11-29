import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class LoginAppTest {

    private LoginApp loginApp;
    private JTextField emailField;
    private JPasswordField passwordField;

    @BeforeEach
    void setUp() {
       
        loginApp = new LoginApp();
        emailField = loginApp.getEmailField();
        passwordField = loginApp.getPasswordField();
    }

    // Test 1: Valid Login Test
    @Test
    void testValidLogin() {
        String email = "johndoe@example.com";
        String password = "password123";
        String expectedUserName = "John Doe";

     
        emailField.setText(email);
        passwordField.setText(password);

        String userName = loginApp.authenticateUser(email, password);

      
        assertNotNull(userName, "UserName should not be null for valid login");
        assertEquals(expectedUserName, userName, "Expected username doesn't match the result");

   
        JOptionPane.showMessageDialog(null, "Welcome, " + userName + "!");
 
    }

    // Test 2: Invalid Email Test
    @Test
    void testInvalidEmail() {
        String email = "johnde@example.com";
        String password = "password123";

        emailField.setText(email);
        passwordField.setText(password);

        String userName = loginApp.authenticateUser(email, password);

        assertNull(userName, "Login should fail for invalid credentials");

        JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
      
    }

    // Test 3: Empty Email Field Test
    @Test
    void testEmptyEmailField() {
        String emptyEmail = "";
        String password = "eman";

       
        emailField.setText(emptyEmail);
        passwordField.setText(password);

     
        assertTrue(emailField.getText().isEmpty(), "Email should be empty");

  
        String userName = loginApp.authenticateUser(emptyEmail, password);
        assertNull(userName, "Login should fail for empty email");

    
        JOptionPane.showMessageDialog(null, "Please enter both email and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // Test 4: Database Connectivity Test
    @Test
    void testDatabaseConnection() {

        String email = "johndoe@example.com";
        String password = "password123";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "eman", "strawberry!@#")) {
            assertNotNull(conn, "Database connection should not be null");

          
            String query = "SELECT Name FROM User WHERE Email = ? AND Password = ?";
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            var rs = stmt.executeQuery();
            assertTrue(rs.next(), "Query should return a result");

        } catch (SQLException e) {
            fail("Database connection or query execution failed: " + e.getMessage());
        }
    }

    // Test 5: Password Validation Test
    @Test
    void testPasswordValidation() {
        String email = "johndoe@example.com";
        String validPassword = "password123";       
        String incorrectPassword = "wrongPassword"; 

      
        emailField.setText(email);
        passwordField.setText(validPassword);
        String userName = loginApp.authenticateUser(email, validPassword);

    
        assertNotNull(userName, "Login should succeed for valid password");
        assertEquals("John Doe", userName, "Expected username doesn't match the result");  

  
        passwordField.setText(incorrectPassword);
        userName = loginApp.authenticateUser(email, incorrectPassword);

    
        assertNull(userName, "Login should fail for incorrect password");

        JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
}