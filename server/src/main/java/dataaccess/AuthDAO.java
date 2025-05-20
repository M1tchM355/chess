package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public AuthData createAuth(UserData user);
}
