package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import request.ListGamesRequest;
import result.ListGamesResult;

import java.util.Collection;

public class ListGamesService extends ChessService{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest req) throws UnauthorizedException{
        authDAO.getAuth(req.authToken());
        Collection<GameData> games = gameDAO.listGames();
        return new ListGamesResult(games);
    }
}
