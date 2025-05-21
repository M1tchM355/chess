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
           LogoutRequest request = new Gson().fromJson(req.body(), LogoutRequest.class);
           LogoutService logoutService = new LogoutService(daoRecord.authDAO());
           LogoutResult result = logoutService.logout(request);
           return new Gson().toJson(result);
       } catch (UnauthorizedException e){
           res.status(401);
           return new Gson().toJson("Error: unauthorized");
       } catch (Exception e) {
           res.status(500);
           return new Gson().toJson("Error: "+e.toString());
       }
    }
}
