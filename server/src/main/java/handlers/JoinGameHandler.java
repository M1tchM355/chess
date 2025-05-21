package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import dataaccess.DataAccessException;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.JoinGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends ChessHandler{
    public String joinGame(Request req, Response res, DAORecord daoRecord){
        try{
            JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
            JoinGameService joinGameService = new JoinGameService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
            JoinGameResult result = joinGameService.joinGame(request);
            return new Gson().toJson(result);
        } catch (UnauthorizedException e) {
            res.status(401);
            return new Gson().toJson("Error: unauthorized");
        } catch (DataAccessException e){
            res.status(400);
            return new Gson().toJson("Error: bad request");
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson("Error: "+e.toString());
        }
    }
}
