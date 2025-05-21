package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.ListGamesRequest;
import result.ListGamesResult;
import service.ListGamesService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends ChessHandler{
    public String listGames(Request req, Response res, DAORecord daoRecord){
        try{
            ListGamesRequest request = new Gson().fromJson(req.body(), ListGamesRequest.class);
            ListGamesService listGamesService = new ListGamesService(daoRecord.gameDAO(), daoRecord.authDAO());
            ListGamesResult result = listGamesService.listGames(request);
            return new Gson().toJson(result);
        } catch (UnauthorizedException e){
            res.status(401);
            return new Gson().toJson("Error: unauthorized");
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson("Error: "+e.toString());
        }
    }
}
