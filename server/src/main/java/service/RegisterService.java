package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService extends ChessService{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req) throws AlreadyTakenException{
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
