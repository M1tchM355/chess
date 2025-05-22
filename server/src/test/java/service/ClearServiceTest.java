package service;

import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.ListGamesRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ClearServiceTest {
    @Test
    public void clearSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ClearService clearService = new ClearService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        userDAO.createUser(user);
        ClearRequest request = new ClearRequest();
        gameDAO.createGame("game1");
        try {
            clearService.clear(request);
            Assertions.assertEquals(new ArrayList<>(), new ArrayList<>(gameDAO.listGames()));
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
