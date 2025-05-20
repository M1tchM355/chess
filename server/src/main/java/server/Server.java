package server;

import dataaccess.*;
import handlers.RegisterHandler;
import spark.*;

public class Server {
    private final DAORecord daoRecord;

    public Server(){
        this.daoRecord = new DAORecord(new MemoryUserDAO(),new MemoryAuthDAO(),new MemoryGameDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);

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
}
