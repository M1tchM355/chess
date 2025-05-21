package handlers;

import dataaccess.AuthDAO;
import service.ChessService;
import service.UnauthorizedException;

public class ChessHandler {
    public static void checkAuth(String authToken, AuthDAO authDAO) throws UnauthorizedException{
        new ChessService(authDAO).getAuth(authToken);
    }
}
