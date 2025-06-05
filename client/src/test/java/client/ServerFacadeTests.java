package client;

import dataaccess.DatabaseManager;
import org.junit.jupiter.api.*;
import request.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("TRUNCATE user")) {
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("TRUNCATE game")) {
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("TRUNCATE auth")) {
                ps.executeUpdate();
            }
            facade.register(new RegisterRequest("test", "test", "test"));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFail() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertThrows(Exception.class, () -> facade.register(new RegisterRequest("player1", "password", "p1@email.com")));
    }

    @Test
    void loginSuccess() throws Exception {
        var loginData = facade.login(new LoginRequest("test", "test"));
        Assertions.assertTrue(loginData.authToken().length() > 10);
    }

    @Test
    void loginFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.login(new LoginRequest("username", "password")));
    }

    @Test
    void logoutSuccess() throws Exception {
        var loginData = facade.login(new LoginRequest("test", "test"));
        facade.logout(new LogoutRequest(loginData.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.login(new LoginRequest("test", "test")));
    }

    @Test
    void logoutFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.logout(new LogoutRequest("")));
    }

    @Test
    void listGamesSuccess() throws Exception {
        var loginData = facade.login(new LoginRequest("test", "test"));
        var gameData = facade.listGames(new ListGamesRequest(loginData.authToken()));
        Assertions.assertTrue(gameData.games().isEmpty());
    }

    @Test
    void listGamesFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.listGames(new ListGamesRequest("")));
    }

    @Test
    void createGameSuccess() throws Exception {
        var loginData = facade.login(new LoginRequest("test", "test"));
        facade.createGame(new CreateGameRequest(loginData.authToken(), "game"));
        var gameData = facade.listGames(new ListGamesRequest(loginData.authToken()));
        Assertions.assertFalse(gameData.games().isEmpty());
    }

    @Test
    void createGameFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.createGame(new CreateGameRequest("", "")));
    }

    @Test
    void joinGameSuccess() throws Exception {
        var loginData = facade.login(new LoginRequest("test", "test"));
        facade.createGame(new CreateGameRequest(loginData.authToken(), "game"));
        var gameData = facade.listGames(new ListGamesRequest(loginData.authToken()));
        facade.joinGame(new JoinGameRequest("WHITE", 1, loginData.authToken()));
        var gameData2 = facade.listGames(new ListGamesRequest(loginData.authToken()));
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(new JoinGameRequest("WHITE", 1, loginData.authToken())));
    }

    @Test
    void joinGameFail() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(new JoinGameRequest("", 1, "")));
    }
}
