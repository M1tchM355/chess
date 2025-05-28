package service;

import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.ListGamesRequest;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesServiceTest {
    @Test
    public void listGamesSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        try{
            userDAO.createUser(user);
            String authToken = authDAO.createAuth(user).authToken();
            ListGamesRequest request = new ListGamesRequest(authToken);
            int gameID = gameDAO.createGame("game1");
            ArrayList<GameData> expectedGames = new ArrayList<>();
            expectedGames.add(gameDAO.getGame(gameID));
            Collection<GameData> games = listGamesService.listGames(request).games();
            ArrayList<GameData> actualGames = new ArrayList<>(games);
            Assertions.assertEquals(expectedGames,actualGames);
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void listGamesFail(){
        try {
            UserDAO userDAO = new MemoryUserDAO();
            AuthDAO authDAO = new MemoryAuthDAO();
            GameDAO gameDAO = new MemoryGameDAO();
            ListGamesService listGamesService = new ListGamesService(null, authDAO, null);
            String username = "username";
            String password = "pass123";
            String email = "myemail@gmail.com";
            UserData user = new UserData(username, password, email);
            userDAO.createUser(user);
            ListGamesRequest request = new ListGamesRequest("wrong auth token");
            gameDAO.createGame("game1");
            Assertions.assertThrows(Exception.class, () -> listGamesService.listGames(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
