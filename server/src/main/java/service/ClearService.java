package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import request.ClearRequest;

public class ClearService extends ChessService{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public ClearService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear(ClearRequest req){
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuths();
    }
}
