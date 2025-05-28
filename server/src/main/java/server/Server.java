package server;

import dataaccess.*;
import handlers.*;
import spark.*;

public class Server {
    private final DAORecord daoRecord;

    public Server(){
        try {
            this.daoRecord = new DAORecord(new SQLUserDAO(), new SQLAuthDAO(), new SQLGameDAO());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session",this::login);
        Spark.delete("/db",this::clear);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        return new RegisterHandler().register(req, res, daoRecord);
    }

    private Object login(Request req, Response res){
        return new LoginHandler().login(req, res, daoRecord);
    }

    private Object clear(Request req, Response res){
        return new ClearHandler().clear(req, res, daoRecord);
    }

    private Object logout(Request req, Response res){
        return new LogoutHandler().logout(req, res, daoRecord);
    }

    private Object listGames(Request req, Response res){
        return new ListGamesHandler().listGames(req, res, daoRecord);
    }

    private Object createGame(Request req, Response res){
        return new CreateGameHandler().createGame(req, res, daoRecord);
    }

    private Object joinGame(Request req, Response res){
        return new JoinGameHandler().joinGame(req, res, daoRecord);
    }
}
