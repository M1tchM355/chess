package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;

public class ChessService {
    protected final GameDAO gameDAO;
    protected final AuthDAO authDAO;
    protected final UserDAO userDAO;

    public ChessService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException{
        return authDAO.getAuth(authToken);
    }
}
