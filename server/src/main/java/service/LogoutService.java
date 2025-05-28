package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService extends ChessService{
    public LogoutService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public LogoutResult logout(LogoutRequest req) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());
        authDAO.deleteAuth(authData);
        return new LogoutResult();
    }
}
