package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService extends ChessService{
    public RegisterResult register(RegisterRequest req) throws AlreadyTakenException{
        try{
            UserData userCheck = MemoryUserDAO.getUser(req.username());
            throw new AlreadyTakenException("Username is already taken");
        } catch (DataAccessException e) {
            UserData user = new UserData(req.username(),req.password(),req.email());
            MemoryUserDAO.createUser(user);
            AuthData auth = MemoryUserDAO.createAuth(user);
            return new RegisterResult(auth.username(), auth.authToken());
        }
    }
}
