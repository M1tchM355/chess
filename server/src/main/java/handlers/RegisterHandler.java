package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.RegisterRequest;
import result.RegisterResult;
import service.AlreadyTakenException;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends ChessHandler{
    public String register(Request req, Response res, DAORecord daoRecord){
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterService registerService = new RegisterService(daoRecord.userDAO(),daoRecord.authDAO());
        try {
            RegisterResult result = registerService.register(request);
            res.header("Code",200);
            return gson.toJson(result);
        } catch (AlreadyTakenException e) {
            res.header("Code",403);
            return "Error: already taken";
        }
    }
}
