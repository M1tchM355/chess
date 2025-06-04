package ui;

import server.ServerFacade;

public class Client {
    protected final ServerFacade server;
    protected final String serverUrl;
    protected String authToken;

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
}
