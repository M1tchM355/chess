package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

public class LoginService extends ChessService{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest req) throws DataAccessException,UnauthorizedException{
        UserData user = userDAO.getUser(req.username());
        if (!passwordMatches(req,user)){
            throw new UnauthorizedException("Wrong password");
        }
        AuthData auth = authDAO.createAuth(user);
        return new LoginResult(auth.username(), auth.authToken());
    }

    private boolean passwordMatches(LoginRequest req, UserData user){
        return req.password().equals(user.password());
    }
}
