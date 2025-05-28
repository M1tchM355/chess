package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUserDAOTest {
    private final String username = "TestUser";
    private final String password = "testPassword";
    private final String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    private final String email = "email";

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
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,hashedPassword);
                ps.setString(3,email);

                ps.executeUpdate();
            }

            UserData returnedUser = new SQLUserDAO().getUser(username);
            boolean passwordMatches = BCrypt.checkpw(password, returnedUser.password());
            Assertions.assertEquals(username,returnedUser.username());
            Assertions.assertEquals(email,returnedUser.email());
            Assertions.assertTrue(passwordMatches);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void getUserFailTest() {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,hashedPassword);
                ps.setString(3,email);

                ps.executeUpdate();
            }

            UserData returnedUser = new SQLUserDAO().getUser("bad username");
            Assertions.assertNull(returnedUser);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void createUserSuccessTest() {
        UserData user = new UserData(username, password, email);
        try {
            new SQLUserDAO().createUser(user);

            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                    ps.setString(1,username);
                    try (var rs = ps.executeQuery()) {
                        rs.next();
                        var returnedUsername = rs.getString("username");
                        var returnedPassword = rs.getString("password");
                        var returnedEmail = rs.getString("email");

                        Assertions.assertEquals(returnedUsername,username);
                        boolean passwordMatches = BCrypt.checkpw("testPassword", returnedPassword);
                        Assertions.assertTrue(passwordMatches);
                        Assertions.assertEquals(returnedEmail,email);
                    }
                }
            }
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void createUserFailTest() {
        UserData user = new UserData(username, password, null);
        Assertions.assertThrows(DataAccessException.class, () -> new SQLUserDAO().createUser(user));
    }

    @Test
    public void clearUsersTest() {
        try {
            var statement1 = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            var statement2 = "SELECT * FROM user";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement1)) {
                    ps.setString(1,username);
                    ps.setString(2,hashedPassword);
                    ps.setString(3,email);

                    ps.executeUpdate();
                }

                new SQLUserDAO().clearUsers();
                try (var ps = conn.prepareStatement(statement2, RETURN_GENERATED_KEYS)) {
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Assertions.fail();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
