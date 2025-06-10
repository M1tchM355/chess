package websocket.commands;

public class MakeMoveCommand extends UserGameCommand {
    private String move;
    public MakeMoveCommand(String authToken, Integer gameID, String move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
