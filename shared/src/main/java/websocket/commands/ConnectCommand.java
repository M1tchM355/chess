package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private String role;
    public ConnectCommand(String authToken, Integer gameID, String role) {
        super(CommandType.CONNECT, authToken, gameID);
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
