package ui;

import server.ServerFacade;

public class Client {
    protected final ServerFacade server;
    protected final String serverUrl;
    protected String authToken;
    protected Integer gameID;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        authToken = null;
    }

    public String eval(String input) {
        return null;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
