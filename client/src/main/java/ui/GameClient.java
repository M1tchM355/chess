package ui;

import chess.ChessGame;
import request.JoinGameRequest;
import server.ResponseException;
import websocket.ServerMessageObserver;
import websocket.WebsocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class GameClient extends Client {
    private final ServerMessageObserver observer;
    private WebsocketFacade ws;

    public GameClient(String serverURL, ServerMessageObserver observer) {
        super(serverURL);
        this.observer = observer;
    }

    @Override
    public String eval(String input) {
        try {
            ws = new WebsocketFacade(serverUrl, observer);
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> draw();
                case "move" -> move(params);
                case "highlight" -> highlight(params);
                case "resign" -> resign();
                case "leave" -> leave();
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() throws ResponseException {
        return """
                redraw - redraw the chess board
                move <START> <END> - move the piece at start position to the end position. Valid positions are a letter and number (like a7)
                highlight <POSITION> - highlight all legal moves for the piece at position. Valid positions are a letter and number (like a7)
                resign - resign the game
                leave - leave the chess game
                quit - quit application
                help - see possible commands
                """;
    }

    public String leave() {
        return "Left game";
    }

    public String draw() {
        return null;
    }

    public String move(String... params) {
        return null;
    }

    public String highlight(String... params) {
        return null;
    }

    public String resign() {
        return null;
    }

    public String quit() {
        return "quit";
    }
}
