package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.BadRequestException;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends ChessHandler{
    public String createGame(Request req, Response res, DAORecord daoRecord){
        try{
            String authToken = req.headers("Authorization");
            checkAuth(authToken, daoRecord.authDAO());
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            CreateGameService createGameService = new CreateGameService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
            CreateGameResult result = createGameService.createGame(request);
            return new Gson().toJson(result);
        } catch (UnauthorizedException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        } catch (BadRequestException e){
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: bad request"));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: " + e));
        }
    }
}
