package handlers;

import com.google.gson.Gson;
import dataaccess.DAORecord;
import request.ClearRequest;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends ChessHandler {
    public String clear(Request req, Response res, DAORecord daoRecord) {
        try {
            ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);
            ClearService clearService = new ClearService(daoRecord.gameDAO(), daoRecord.authDAO(), daoRecord.userDAO());
            clearService.clear(request);
            return new Gson().toJson(null);
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: " + e));
        }
    }
}
