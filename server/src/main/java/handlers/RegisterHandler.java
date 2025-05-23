package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.RegisterRequest;
import result.RegisterResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends ChessHandler{
    public String register(Request req, Response res, DAORecord daoRecord){
        Gson gson = new Gson();
        try {
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
            RegisterService registerService = new RegisterService(daoRecord.gameDAO(),daoRecord.authDAO(), daoRecord.userDAO());
            RegisterResult result = registerService.register(request);
            return gson.toJson(result);
        } catch (AlreadyTakenException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (BadRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: " + e));
        }
    }
}
