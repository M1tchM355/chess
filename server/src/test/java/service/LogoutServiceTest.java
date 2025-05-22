package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LogoutRequest;

public class LogoutServiceTest {
    @Test
    public void logoutSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        LogoutService logoutService = new LogoutService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user).authToken();
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Assertions.assertDoesNotThrow(() -> {
            logoutService.logout(logoutRequest);
        });
    }

    @Test
    public void logoutFail(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        LogoutService logoutService = new LogoutService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        UserData user = new UserData(username,password,email);
        userDAO.createUser(user);
        String authToken = "not an authToken";
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            logoutService.logout(logoutRequest);
        });
    }
}
