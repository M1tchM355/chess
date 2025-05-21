package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import request.ListGamesRequest;
import result.ListGamesResult;

import java.util.Collection;

public class ListGamesService extends ChessService{
    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws UnauthorizedException{
        authDAO.getAuth(req.authToken());
        Collection<GameData> games = gameDAO.listGames();
        return new ListGamesResult(games);
    }
}
