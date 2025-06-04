package ui;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        return null;
    }
}
