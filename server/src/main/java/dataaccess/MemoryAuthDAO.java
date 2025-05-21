package dataaccess;

import model.AuthData;
import model.UserData;
import service.UnauthorizedException;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(UserData user){
        AuthData auth = new AuthData(UUID.randomUUID().toString(),user.username());
        auths.put(auth.authToken(),auth);
        return auth;
    }

    @Override
    public AuthData getAuth(String authToken) throws UnauthorizedException{
        AuthData auth = auths.get(authToken);
        if(auth==null){
            throw new UnauthorizedException("Invalid authToken");
        }
        return auth;
    }

    @Override
    public void deleteAuth(AuthData authData){
        auths.remove(authData.authToken());
    }

    @Override
    public void clearAuths() {
        auths.clear();
    }
}
