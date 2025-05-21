package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import dataaccess.DataAccessException;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends ChessHandler{
    public String createGame(Request req, Response res, DAORecord daoRecord){
        try{
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            CreateGameService createGameService = new CreateGameService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
            CreateGameResult result = createGameService.createGame(request);
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
