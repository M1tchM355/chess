package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException{
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS game (
              gameID INT NOT NULL AUTO_INCREMENT,
              whiteUsername varchar(256),
              blackUsername varchar(256),
              gameName varchar(256) NOT NULL,
              game TEXT NOT NULL,
              PRIMARY KEY (gameID)
            )
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (gameName, game) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameName);
                String jsonGame = new Gson().toJson(new ChessGame());
                ps.setString(2, jsonGame);

                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,gameID);
                try (var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var jsonGame = rs.getString("game");
                        ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    Collection<GameData> games = new ArrayList<>();
                    while(rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var jsonGame = rs.getString("game");
                        ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class);
                        games.add(new GameData(gameID,whiteUsername,blackUsername,gameName,game));
                    }
                    return games;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username, ChessGame game) throws DataAccessException {
        String statement = getString(playerColor, game);

        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                if (playerColor == null) {
                    ps.setString(1, new Gson().toJson(game));
                    ps.setInt(2, gameID);
                } else {
                    ps.setString(1, username);
                    if (game == null) {
                        ps.setInt(2,gameID);
                    } else {
                        ps.setString(2, new Gson().toJson(game));
                        ps.setInt(3, gameID);
                    }
                }

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));
        }
    }

    private String getString(String playerColor, ChessGame game) throws DataAccessException {
        String statement;
        if (playerColor == null) {
            if (game != null) {
                return "UPDATE game SET game=? WHERE gameID=?";
            } else {
                throw new DataAccessException("Can't have null player color and game");
            }
        }
        if (playerColor.equals("WHITE")) {
            if (game != null) {
                statement = "UPDATE game SET whiteUsername=?, game=? WHERE gameID=?";
            } else {
                statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
            }
        } else if (playerColor.equals("BLACK")) {
            if (game != null) {
                statement = "UPDATE game SET blackUsername=?, game=? WHERE gameID=?";
            } else {
                statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
            }        } else {
            throw new DataAccessException("Invalid color");
        }
        return statement;
    }

    @Override
    public void clearGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE game")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear database: %s", e.getMessage()));
        }
    }
}
