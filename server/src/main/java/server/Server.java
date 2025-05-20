package server;

import dataaccess.*;
import handlers.RegisterHandler;
import spark.*;

public class Server {
    private final DAORecord daoRecord;

    public Server(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        this.daoRecord = new DAORecord(userDAO,authDAO,gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        RegisterHandler handler = new RegisterHandler();
        return handler.register(req, res, daoRecord);
    }
}
