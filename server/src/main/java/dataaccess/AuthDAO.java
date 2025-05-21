package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public AuthData createAuth(UserData user);
    public AuthData getAuth(String authToken);
    public void deleteAuth(AuthData authData);
}
