package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import request.JoinGameRequest;
import result.JoinGameResult;

public class JoinGameService extends ChessService{
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());
        gameDAO.updateGame(req.gameID(),req.playerColor(),authData.username());
        return new JoinGameResult();
    }
}
