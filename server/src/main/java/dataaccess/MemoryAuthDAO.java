package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    @Override
    public AuthData createAuth(UserData user){
        return new AuthData(UUID.randomUUID().toString(),user.username());
    }
}
