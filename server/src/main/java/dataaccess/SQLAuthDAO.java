package dataaccess;

import model.AuthData;
import model.UserData;
import service.UnauthorizedException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException{
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              authToken varchar(256) NOT NULL,
              username varchar(256) NOT NULL,
              PRIMARY KEY (username)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
    @Override
    public AuthData createAuth(UserData user) throws DataAccessException{
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                String token = UUID.randomUUID().toString();
                ps.setString(1,user.username());
                ps.setString(2, token);
                ps.executeUpdate();

                return new AuthData(token, user.username());
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws UnauthorizedException, DataAccessException {
        var statement = "SELECT username FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,authToken);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    var username = rs.getString("username");

                    return new AuthData(authToken,username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));

        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException{
        var statement = "DELETE FROM auth WHERE authToken=?";
        String authToken = authData.authToken();
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));

        }
    }

    @Override
    public void clearAuths() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE auth")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear database: %s", e.getMessage()));
        }
    }
}
