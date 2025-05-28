package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAOTest {
    private final String gameName1 = "test game";
    private final String whiteUsername1 = "white1";
    private final String blackUsername1 = null;
    private final ChessGame chessGame1 = new ChessGame();
    private final String game1 = new Gson().toJson(chessGame1);

    private final String gameName2 = "better test game";
    private final String whiteUsername2 = "cool guy";
    private final String blackUsername2 = "cooler guy";
    private final ChessGame chessGame2 = new ChessGame();
    private final String game2 = new Gson().toJson(chessGame2);

    @BeforeEach
    public void setup() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE game")) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void createGameSuccessTest() {
        try {
            int gameID = new SQLGameDAO().createGame(gameName1);

            var statement = "SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                    ps.setInt(1,gameID);
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            var whiteUser = rs.getString("whiteUsername");
                            var blackUser = rs.getString("blackUsername");
                            var retGameName = rs.getString("gameName");
                            var retGame = rs.getString("game");

                            Assertions.assertNull(whiteUser);
                            Assertions.assertNull(blackUser);
                            Assertions.assertEquals(retGameName, gameName1);
                            String game = new Gson().toJson(new ChessGame());
                            Assertions.assertEquals(retGame, game);
                        } else {
                            Assertions.fail();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void getGameSuccessTest() {
        String statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, whiteUsername1);
                ps.setString(2, blackUsername1);
                ps.setString(3, gameName1);
                ps.setString(4, game1);

                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                rs.next();
                int gameID = rs.getInt(1);

                GameData returnedGame = new SQLGameDAO().getGame(gameID);
                Assertions.assertEquals(whiteUsername1, returnedGame.whiteUsername());
                Assertions.assertEquals(blackUsername1, returnedGame.blackUsername());
                Assertions.assertEquals(gameName1, returnedGame.gameName());
                Assertions.assertEquals(chessGame1, returnedGame.game());
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void listGamesSuccessTest() {
        String statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, whiteUsername1);
                ps.setString(2, blackUsername1);
                ps.setString(3, gameName1);
                ps.setString(4, game1);

                ps.executeUpdate();

                ps.setString(1, whiteUsername2);
                ps.setString(2, blackUsername2);
                ps.setString(3, gameName2);
                ps.setString(4, game2);

                ps.executeUpdate();

                ArrayList<GameData> games = (ArrayList<GameData>) new SQLGameDAO().listGames();
                GameData expected1 = new GameData(1,whiteUsername1,blackUsername1,gameName1,chessGame1);
                GameData expected2 = new GameData(2,whiteUsername2,blackUsername2,gameName2,chessGame2);
                Assertions.assertEquals(games.get(0), expected1);
                Assertions.assertEquals(games.get(1), expected2);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void updateGameSuccessTest() {
        String statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String statement2 = "SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, whiteUsername1);
                ps.setString(2, blackUsername1);
                ps.setString(3, gameName1);
                ps.setString(4, game1);

                ps.executeUpdate();
            }

            new SQLGameDAO().updateGame(1, "BLACK", blackUsername2);

            try (var ps2 = conn.prepareStatement(statement2, RETURN_GENERATED_KEYS)) {
                ps2.setInt(1,1);
                try (var rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        var whiteUser = rs.getString("whiteUsername");
                        var blackUser = rs.getString("blackUsername");
                        var retGameName = rs.getString("gameName");
                        var retGame = rs.getString("game");

                        Assertions.assertEquals(whiteUser, whiteUsername1);
                        Assertions.assertEquals(blackUser, blackUsername2);
                        Assertions.assertEquals(retGameName, gameName1);
                        String game = new Gson().toJson(new ChessGame());
                        Assertions.assertEquals(retGame, game);
                    } else {
                        Assertions.fail();
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void clearGamesTest() {
        try {
            String statement1 = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            var statement2 = "SELECT * FROM game";
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement(statement1)) {
                    ps.setString(1, whiteUsername1);
                    ps.setString(2, blackUsername1);
                    ps.setString(3, gameName1);
                    ps.setString(4, game1);

                    ps.executeUpdate();
                }

                new SQLGameDAO().clearGames();

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
