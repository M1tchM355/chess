package dataaccess;

import model.AuthData;
import model.UserData;
import service.UnauthorizedException;

public interface AuthDAO {
    public AuthData createAuth(UserData user);
    public AuthData getAuth(String authToken) throws UnauthorizedException;
    public void deleteAuth(AuthData authData);
    public void clearAuths();
}
