package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService extends ChessService{

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws BadRequestException {
        if(req.gameName() == null){
            throw new BadRequestException("Missing parameter");
        }
        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }
}
