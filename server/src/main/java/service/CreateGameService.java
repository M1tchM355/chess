package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService extends ChessService{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());
        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }
}
