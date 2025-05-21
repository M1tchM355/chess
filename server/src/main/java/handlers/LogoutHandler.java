package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DAORecord;
import request.LogoutRequest;
import result.LogoutResult;
import service.LogoutService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LogoutHandler extends ChessHandler{
    public String logout(Request req, Response res, DAORecord daoRecord){
       try {
           String authToken = req.headers("Authorization");
           checkAuth(authToken, daoRecord.authDAO());
           LogoutRequest request = new LogoutRequest(authToken);
           LogoutService logoutService = new LogoutService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
           LogoutResult result = logoutService.logout(request);
           return new Gson().toJson(result);
       } catch (UnauthorizedException e){
           res.status(401);
           return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
       } catch (Exception e) {
           res.status(500);
           return new Gson().toJson(new ErrorResponse("Error: "+e.toString()));
       }
    }
}
