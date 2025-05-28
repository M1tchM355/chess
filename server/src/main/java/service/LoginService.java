package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import result.LoginResult;

public class LoginService extends ChessService{
    public LoginService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public LoginResult login(LoginRequest req) throws DataAccessException, UnauthorizedException, BadRequestException{
        if(req.username() == null || req.password() == null){
            throw new BadRequestException("Missing parameter");
        }
        UserData user = userDAO.getUser(req.username());
        if(user == null) {
            throw new UnauthorizedException("User does not exist");
        }
        if (!passwordMatches(req,user)){
            throw new UnauthorizedException("Wrong password");
        }
        AuthData auth = authDAO.createAuth(user);
        return new LoginResult(auth.username(), auth.authToken());
    }

    private boolean passwordMatches(LoginRequest req, UserData user){
        return BCrypt.checkpw(req.password(), user.password());
    }
}
