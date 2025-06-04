package ui;

import chess.ChessGame;
import server.ResponseException;

import java.util.Arrays;

public class GameClient extends Client {
    public GameClient(String serverURL){
        super(serverURL);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() throws ResponseException {
        return """
                quit - quit application
                help - see possible commands
                """;
    }

    public String printGame(ChessGame game) {
        return null;
    }
}
