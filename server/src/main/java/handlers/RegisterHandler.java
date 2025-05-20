package handlers;

import com.google.gson.Gson;
import request.RegisterRequest;
import spark.Request;
import spark.Response;

public class RegisterHandler extends ChessHandler{
    public String register(Request req, Response res){

        Gson gson = new Gson();
        RegisterRequest request = (RegisterRequest) gson.fromJson()
    }
}
