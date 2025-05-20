package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO{
    final static private HashMap<String, UserData> users = new HashMap<>();

    public static UserData getUser(String username) throws DataAccessException{
        UserData user = users.get(username);
        if(user == null){
            throw new DataAccessException("User does not exist");
        }
        return user;
    }

    public static void createUser(UserData user){
        users.put(user.username(),user);
    }

    public static AuthData createAuth(UserData user){
        return new AuthData(UUID.randomUUID().toString(),user.username());
    }
}
