package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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

    public void connect() throws ResponseException {
        ws = new WebsocketFacade(serverUrl, observer);
        ws.connect(authToken, gameID, role);
    }

    public String draw() {
        if (currentGame == null) {
            return "Not in a game";
        }
        if (role.equals("BLACK")) {
            return printGame("black");
        } else {
            return printGame("white");
        }
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

    private String printGame(String color) {
        int start;
        int finish;
        int add;
        StringBuilder res = new StringBuilder("\n" + setBorder());
        if (color.equals("white")) {
            start = 8;
            finish = 1;
            add = -1;
            res.append("    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
        } else {
            start = 1;
            finish = 8;
            add = 1;
            res.append("    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
        }
        for (int i = start; i >= finish; i += add) {
            res.append("\n").append(setBorder()).append(" ").append(i).append(" ").append(setLightBG())
                    .append(getPiece(i, 1)).append(setDarkBG()).append(getPiece(i, 2)).append(setLightBG())
                    .append(getPiece(i, 3)).append(setDarkBG()).append(getPiece(i, 4)).append(setLightBG())
                    .append(getPiece(i, 5)).append(setDarkBG()).append(getPiece(i, 6)).append(setLightBG())
                    .append(getPiece(i, 7)).append(setDarkBG()).append(getPiece(i, 8)).append(setBorder())
                    .append(" ").append(i).append(" ").append(RESET_BG_COLOR);
        }
        res.append("\n").append(setBorder());
        if (color.equals("white")) {
            res.append("    a  b  c  d  e  f  g  h    ");
        } else {
            res.append("\n").append(setBorder()).append("    h  g  f  e  d  c  b  a    ");
        }
        res.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        return res.toString();
    }

    private String getPiece(int row, int col) {
        String res;
        ChessPiece piece = currentGame.getBoard().getPiece(new ChessPosition(row, col));
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            res = setWhite();
        } else {
            res = setBlack();
        }
        switch (piece.getPieceType()) {
            case KING -> {
                res += " K ";
            }
            case QUEEN -> {
                res += " Q ";
            }
            case BISHOP -> {
                res += " B ";
            }
            case ROOK -> {
                res += " R ";
            }
            case KNIGHT -> {
                res += " N ";
            }
            case PAWN -> {
                res += " P ";
            }
            case null, default -> {
                res += "   ";
            }
        }
        return res;
    }

    private String setLightBG() {
        return SET_BG_COLOR_WHITE;
    }

    private String setWhite() {
        return SET_TEXT_COLOR_RED;
    }

    private String setDarkBG() {
        return SET_BG_COLOR_DARK_GREY;
    }

    private String setBlack() {
        return SET_TEXT_COLOR_BLUE;
    }

    private String setBorder() {
        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK;
    }
}
