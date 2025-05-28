package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAOTest {
    @BeforeEach
    public void setup() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE user")) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void getUserSuccessTest() {
        String username = "TestUser";
        String hashedPassword = BCrypt.hashpw("testPassword", BCrypt.gensalt());
        String email = "email";
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,hashedPassword);
                ps.setString(3,email);

                ps.executeUpdate();
            }

            UserData returnedUser = new SQLUserDAO().getUser(username);
            boolean passwordMatches = BCrypt.checkpw("testPassword", returnedUser.password());
            Assertions.assertEquals(username,returnedUser.username());
            Assertions.assertEquals(email,returnedUser.email());
            Assertions.assertTrue(passwordMatches);
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
