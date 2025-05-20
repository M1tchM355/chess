package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if(user == null){
            throw new DataAccessException("User does not exist");
        }
        return user;
    }

    @Override
    public void createUser(UserData user){
        users.put(user.username(),user);
    }
}
