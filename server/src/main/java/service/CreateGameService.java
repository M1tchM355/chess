package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService extends ChessService{

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException, BadRequestException {
        if(req.gameName() == null){
            throw new BadRequestException("Missing parameter");
        }
        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }
}
