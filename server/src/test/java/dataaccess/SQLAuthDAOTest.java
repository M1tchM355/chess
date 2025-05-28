package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAOTest {
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
        String username = "TestUser";
        String hashedPassword = BCrypt.hashpw("testPassword", BCrypt.gensalt());
        String email = "email";
        UserData user = new UserData(username, hashedPassword, email);
        try {
            AuthData returnedAuth = new SQLAuthDAO().createAuth(user);

            Assertions.assertEquals(username,returnedAuth.username());

            var statement = "SELECT authToken FROM auth WHERE username=?";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                    ps.setString(1,username);
                    try (var rs = ps.executeQuery()) {
                        rs.next();
                        var authToken = rs.getString("authToken");

                        Assertions.assertEquals(returnedAuth.authToken(),authToken);
                    }
                }
            }
        } catch (Exception e){
            Assertions.fail();
        }
    }
}
