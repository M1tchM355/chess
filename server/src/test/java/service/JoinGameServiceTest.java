package service;

import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;

public class JoinGameServiceTest {
    @Test
    public void joinGameSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        JoinGameService joinGameService = new JoinGameService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        try {
            userDAO.createUser(user);
            String authToken = authDAO.createAuth(user).authToken();
            int gameID = gameDAO.createGame("game1");
            JoinGameRequest request = new JoinGameRequest("WHITE",gameID,authToken);
            joinGameService.joinGame(request);
            GameData game = gameDAO.getGame(gameID);
            String whiteUsername = game.whiteUsername();
            Assertions.assertEquals(whiteUsername,"username");
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void joinGameFail(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        JoinGameService joinGameService = new JoinGameService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        try {
            userDAO.createUser(user);
            String authToken = authDAO.createAuth(user).authToken();
            int gameID = gameDAO.createGame("game1");
            gameDAO.updateGame(gameID, "WHITE", "other username");
            JoinGameRequest request = new JoinGameRequest("WHITE",gameID,authToken);
            Assertions.assertThrows(AlreadyTakenException.class, () -> joinGameService.joinGame(request));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
