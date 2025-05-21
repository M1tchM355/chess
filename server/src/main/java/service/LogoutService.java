package service;

import dataaccess.AuthDAO;
import model.AuthData;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService extends ChessService{
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public LogoutResult logout(LogoutRequest req) throws UnauthorizedException{
        AuthData authData = authDAO.getAuth(req.authToken());
        authDAO.deleteAuth(authData);
        return new LogoutResult();
    }
}
