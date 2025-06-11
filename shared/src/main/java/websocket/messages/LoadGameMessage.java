package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    String game;
    public LoadGameMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        game = message;
    }

    public String getGame() {
        return game;
    }
}
