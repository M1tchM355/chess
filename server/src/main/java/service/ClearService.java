package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import request.ClearRequest;

public class ClearService extends ChessService{
    public ClearService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public void clear(ClearRequest req){
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuths();
    }
}
