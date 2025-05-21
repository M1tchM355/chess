package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LoginHandler extends ChessHandler{
    public String login(Request req, Response res, DAORecord daoRecord){
        try{
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = new LoginService(daoRecord.userDAO(), daoRecord.authDAO()).login(request);
            return new Gson().toJson(result);
        } catch (UnauthorizedException e) {
            res.status(401);
            return new Gson().toJson("Error: unauthorized");
        } catch (Exception e) {
            res.status(400);
            return new Gson().toJson("Error: bad request");
        }
    }
}
