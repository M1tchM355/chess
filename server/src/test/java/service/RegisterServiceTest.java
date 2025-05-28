package service;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterServiceTest {

    @Test
    public void registerSuccess() {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            AuthDAO authDAO = new MemoryAuthDAO();
            GameDAO gameDAO = new MemoryGameDAO();
            RegisterService registerService = new RegisterService(gameDAO, authDAO, userDAO);
            String username = "username";
            String password = "pass123";
            String email = "myemail@gmail.com";
            RegisterRequest req = new RegisterRequest(username, password, email);
            RegisterResult res = registerService.register(req);
            Assertions.assertDoesNotThrow(() -> {
                UserData addedUser = userDAO.getUser(username);
            }, "Could not find the user");
            Assertions.assertDoesNotThrow(() -> {
                AuthData addedAuth = authDAO.getAuth(res.authToken());
            }, "Could not find the auth");
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void registerExistingUser() {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            AuthDAO authDAO = new MemoryAuthDAO();
            GameDAO gameDAO = new MemoryGameDAO();
            RegisterService registerService = new RegisterService(gameDAO, authDAO, userDAO);
            String username = "username";
            String password = "pass123";
            String email = "myemail@gmail.com";
            RegisterRequest req = new RegisterRequest(username, password, email);
            RegisterResult res1 = registerService.register(req);
            Assertions.assertThrows(AlreadyTakenException.class, () -> {
                RegisterResult res2 = registerService.register(req);
            });
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
