package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class LoginServiceTest {
    @Test
    public void loginSuccess(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        LoginService loginService = new LoginService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        userDAO.createUser(new UserData(username,password,email));
        LoginRequest request = new LoginRequest(username,password);
        try {
            LoginResult result = loginService.login(request);
            Assertions.assertEquals(result.authToken(), authDAO.getAuth(result.authToken()).authToken());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void loginFails(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        LoginService loginService = new LoginService(gameDAO, authDAO, userDAO);
        String username = "username";
        String password = "pass123";
        String email = "myemail@gmail.com";
        String badPass = "bad password";
        userDAO.createUser(new UserData(username,password,email));
        LoginRequest request = new LoginRequest(username,badPass);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            LoginResult result = loginService.login(request);
        });
    }
}
