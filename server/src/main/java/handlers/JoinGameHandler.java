package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.JoinGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends ChessHandler{
    public String joinGame(Request req, Response res, DAORecord daoRecord){
        try{
            String authToken = req.headers("Authorization");
            checkAuth(authToken, daoRecord.authDAO());
            JoinGameRequest partialRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
            JoinGameRequest request = new JoinGameRequest(partialRequest.playerColor(),partialRequest.gameID(),authToken);
            JoinGameService joinGameService = new JoinGameService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
            JoinGameResult result = joinGameService.joinGame(request);
            return new Gson().toJson(result);
        } catch (UnauthorizedException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        } catch (BadRequestException e){
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: bad request"));
        } catch (AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson(new ErrorResponse("Error: already taken"));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: " + e));
        }
    }
}
