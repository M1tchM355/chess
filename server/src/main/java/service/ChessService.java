package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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

    public ChessService(AuthDAO authDAO){
        this.gameDAO = null;
        this.authDAO = authDAO;
        this.userDAO = null;
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null){
            throw new UnauthorizedException("Unauthorized");
        }
        return auth;
    }
}
