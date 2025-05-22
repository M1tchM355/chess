package service;

import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;

public class CreateGameServiceTest {
    @Test
    public void createGameSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user).authToken();
        String gameName = "Really cool game name";
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        try{
            int gameID = createGameService.createGame(request).gameID();
            GameData game = gameDAO.getGame(gameID);
            Assertions.assertEquals(game.gameName(),gameName);
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void createGameFail(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user).authToken();
        CreateGameRequest request = new CreateGameRequest(authToken, null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            createGameService.createGame(request);
        });
    }
}
