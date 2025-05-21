package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService extends ChessService{
    public RegisterService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        super(gameDAO,authDAO,userDAO);
    }

    public RegisterResult register(RegisterRequest req) throws AlreadyTakenException, BadRequestException{
        if(req.username() == null || req.password() == null || req.email() == null){
            throw new BadRequestException("Missing parameter");
        }
        try{
            UserData userCheck = userDAO.getUser(req.username());
            throw new AlreadyTakenException("Username is already taken");
        } catch (DataAccessException e) {
            UserData user = new UserData(req.username(),req.password(),req.email());
            userDAO.createUser(user);
            AuthData auth = authDAO.createAuth(user);
            return new RegisterResult(auth.username(), auth.authToken());
        }
    }
}
