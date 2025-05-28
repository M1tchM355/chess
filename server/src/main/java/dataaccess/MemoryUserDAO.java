package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
//        if(user == null){
//            throw new DataAccessException("User does not exist");
//        }
        return user;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserData userWithHashedPass = new UserData(user.username(),hashedPassword, user.email());
        users.put(user.username(),userWithHashedPass);
    }

    @Override
    public void clearUsers() throws DataAccessException{
        users.clear();
    }
}
