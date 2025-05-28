package handlers;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import service.ChessService;
import service.UnauthorizedException;

public class ChessHandler {
    public static void checkAuth(String authToken, AuthDAO authDAO) throws UnauthorizedException, DataAccessException {
        new ChessService(authDAO).getAuth(authToken);
    }
}
