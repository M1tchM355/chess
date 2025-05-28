package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.JoinGameResult;

public class JoinGameService extends ChessService{
    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws UnauthorizedException, DataAccessException,
                                                               BadRequestException, AlreadyTakenException {
        if(req.gameID() == 0 || req.playerColor() == null){
            throw new BadRequestException("Missing parameter");
        }
        AuthData authData = authDAO.getAuth(req.authToken());
        String playerCol = req.playerColor();
        if (!playerCol.equals("WHITE") && !playerCol.equals("BLACK")) {
            throw new BadRequestException("Invalid color");
        }
        GameData game = gameDAO.getGame(req.gameID());
        if ((playerCol.equals("WHITE") && game.whiteUsername() == null) || (playerCol.equals("BLACK") && game.blackUsername() == null)) {
            gameDAO.updateGame(req.gameID(),playerCol,authData.username());
        } else {
            throw new AlreadyTakenException("Color already taken");
        }
        return new JoinGameResult();
    }
}
