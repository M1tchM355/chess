package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAOTest {
    private final String username = "TestUser";
    private final String password = "testPassword";
    private final String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    private final String email = "email";
    private final String authToken = UUID.randomUUID().toString();

    @BeforeEach
    public void setup() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE auth")) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void createAuthSuccessTest() {
        UserData user = new UserData(username, hashedPassword, email);
        try {
            AuthData returnedAuth = new SQLAuthDAO().createAuth(user);

            Assertions.assertEquals(username,returnedAuth.username());

            var statement = "SELECT authToken FROM auth WHERE username=?";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                    ps.setString(1,username);
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            var authToken = rs.getString("authToken");

                            Assertions.assertEquals(returnedAuth.authToken(), authToken);
                        } else {
                            Assertions.fail();
                        }
                    }
                }
            }
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void getAuthSuccessTest() {
        String statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,authToken);

                ps.executeUpdate();
            }

            AuthData returnedAuth = new SQLAuthDAO().getAuth(authToken);
            Assertions.assertEquals(username,returnedAuth.username());
            Assertions.assertEquals(authToken,returnedAuth.authToken());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void deleteAuthSuccessTest() {
        String statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,authToken);

                ps.executeUpdate();
            }

            new SQLAuthDAO().deleteAuth(new AuthData(authToken,username));

            try (var ps = conn.prepareStatement("SELECT * FROM auth", RETURN_GENERATED_KEYS)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Assertions.fail();
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void clearAuthsTest() {
        try {
            var statement1 = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
            var statement2 = "SELECT * FROM auth";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement1)) {
                    ps.setString(1,username);
                    ps.setString(2,authToken);

                    ps.executeUpdate();
                }

                new SQLAuthDAO().clearAuths();
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
