package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import request.JoinGameRequest;
import result.JoinGameResult;

public class JoinGameService extends ChessService{
    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws UnauthorizedException, DataAccessException, BadRequestException {
        if(req.gameID() == 0 || req.playerColor() == null){
            throw new BadRequestException("Missing parameter");
        }
        AuthData authData = authDAO.getAuth(req.authToken());
        gameDAO.updateGame(req.gameID(),req.playerColor(),authData.username());
        return new JoinGameResult();
    }
}
