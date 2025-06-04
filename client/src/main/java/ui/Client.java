package ui;

import server.ServerFacade;

public class Client {
    protected final ServerFacade server;
    protected final String serverUrl;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        return null;
    }
}
