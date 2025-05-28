package dataaccess;

import model.AuthData;
import model.UserData;
import service.UnauthorizedException;

public interface AuthDAO {
    public AuthData createAuth(UserData user) throws DataAccessException;
    public AuthData getAuth(String authToken) throws UnauthorizedException, DataAccessException;
    public void deleteAuth(AuthData authData) throws DataAccessException;
    public void clearAuths() throws DataAccessException;
}
