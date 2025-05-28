package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;

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
        try {
            userDAO.createUser(new UserData(username,password,email));
            LoginRequest request = new LoginRequest(username,password);
            LoginResult result = loginService.login(request);
            Assertions.assertEquals(result.authToken(), authDAO.getAuth(result.authToken()).authToken());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void loginFails() {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            AuthDAO authDAO = new MemoryAuthDAO();
            GameDAO gameDAO = new MemoryGameDAO();
            LoginService loginService = new LoginService(gameDAO, authDAO, userDAO);
            String username = "username";
            String password = "pass123";
            String email = "myemail@gmail.com";
            String badPass = "bad password";
            userDAO.createUser(new UserData(username, password, email));
            LoginRequest request = new LoginRequest(username, badPass);
            Assertions.assertThrows(UnauthorizedException.class, () -> {
                loginService.login(request);
            });
        } catch (Exception e){
            throw new RuntimeException();
        }
    }
}
