package ui;

import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.LogoutRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import result.LogoutResult;
import server.ResponseException;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class PostloginClient extends Client {
    public HashMap<Integer, Integer> gameMap;

    public PostloginClient(String serverURL){
        super(serverURL);
        gameMap = new HashMap<>();
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help(){
        return """
                create <NAME> - create a chess game with a name
                list - list all games
                join <ID> [WHITE|BLACK] - join game as specified color
                observe <ID> - observe game
                logout - logs you out
                quit - quit application
                help - see possible commands
                """;
    }

    public String create(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                String name = params[0];
                CreateGameRequest request = new CreateGameRequest(authToken, name);
                this.server.createGame(request);
                return "You created a new game called " + name + ".";
            } catch (Exception e) {
                throw new ResponseException(500, "Couldn't create the game");
            }
        }
        throw new ResponseException(400, "Expected create <NAME>");
    }

    public String list(String... params) throws ResponseException {
        try {
            ListGamesRequest request = new ListGamesRequest(authToken);
            ListGamesResult result = this.server.listGames(request);
            Collection<GameData> games = result.games();
            StringBuilder ret = new StringBuilder();
            int counter = 1;
            for (GameData game : games) {
                gameMap.put(counter, game.gameID());
                ret.append("\n").append(counter)
                        .append("\nName: ").append(game.gameName())
                        .append("\nWhite player: ").append(game.whiteUsername())
                        .append("\nBlack player: ").append(game.blackUsername());
                counter++;
            }

            return ret.toString();
        } catch (Exception e) {
            throw new ResponseException(500, "Couldn't list the games");
        }
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                if (gameMap.isEmpty()) {
                    throw new ResponseException(400, "You must first list the games");
                }
                int num = Integer.parseInt(params[0]);
                Integer id = gameMap.get(num);
                if (id == null) {
                    throw new ResponseException(400, "Invalid ID");
                }
                String color = params[1].toUpperCase();
                JoinGameRequest request = new JoinGameRequest(color, id, authToken);
                this.server.joinGame(request);
                if (color.equals("WHITE")) {
                    return printGameWhite();
                } else {
                    return printGameBlack();
                }
            } catch (Exception e) {
                throw new ResponseException(500, "Couldn't join.");
            }
        }
        throw new ResponseException(400, "Expected join <ID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        if (params.length == 1) {
            if (gameMap.isEmpty()) {
                throw new ResponseException(400, "You must first list the games");
            }
            int num = Integer.parseInt(params[0]);
            Integer id = gameMap.get(num);
            if (id == null) {
                throw new ResponseException(400, "Invalid ID");
            }
            return printGameWhite();
        }
        throw new ResponseException(400, "Expected observe <ID>");
    }

    public String logout(String... params) throws ResponseException {
        try {
            LogoutRequest request = new LogoutRequest(this.authToken);
            LogoutResult result = this.server.logout(request);
            authToken = null;
            return "Bye!";
        } catch (Exception e) {
            throw new ResponseException(500, "Couldn't log you out");
        }
    }

    private String printGameWhite() {
        return
                setBorder() + "    a  b  c  d  e  f  g  h    " +
                        "\n 8 " + setLightBG() + setBlack() + " R " + setDarkBG() + " N " + setLightBG() + " B " +
                        setDarkBG() + " Q " + setLightBG() + " K " + setDarkBG() + " B " + setLightBG() + " N " +
                        setDarkBG() + " R " + setBorder() + " 8 " +
                        "\n 7 " + setDarkBG() + setBlack() + " P " + setLightBG() + " P " + setDarkBG() + " P " +
                        setLightBG() + " P " +setDarkBG() + " P " + setLightBG() + " P " +setDarkBG() + " P " +
                        setLightBG() + " P " + setBorder() + " 7 " +
                        "\n 6 " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setBorder() + " 6 " +
                        "\n 5 " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setBorder() + " 5 " +
                        "\n 4 " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setBorder() + " 4 " +
                        "\n 3 " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setBorder() + " 3 " +
                        "\n 2 " + setLightBG() + setWhite() + " P " + setDarkBG() + " P " + setLightBG() + " P " +
                        setDarkBG() + " P " + setLightBG() + " P " +setDarkBG() + " P " + setLightBG() + " P " +
                        setDarkBG() + " P " + setBorder() + " 2 " +
                        "\n 1 " + setDarkBG() + setWhite() + " R " + setLightBG() + " N " + setDarkBG() + " B " +
                        setLightBG() + " Q " + setDarkBG() + " K " + setLightBG() + " B " + setDarkBG() + " N " +
                        setLightBG() + " R " + setBorder() + " 1 " +
                        "\n    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + RESET_TEXT_COLOR;
    }

    private String printGameBlack() {
        return
                setBorder() + "    a  b  c  d  e  f  g  h    " +
                        "\n 8 " + setLightBG() + setWhite() + " R " + setDarkBG() + " N " + setLightBG() + " B " +
                        setDarkBG() + " Q " + setLightBG() + " K " + setDarkBG() + " B " + setLightBG() + " N " +
                        setDarkBG() + " R " + setBorder() + " 8 " +
                        "\n 7 " + setDarkBG() + setWhite() + " P " + setLightBG() + " P " + setDarkBG() + " P " +
                        setLightBG() + " P " +setDarkBG() + " P " + setLightBG() + " P " +setDarkBG() + " P " +
                        setLightBG() + " P " + setBorder() + " 7 " +
                        "\n 6 " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setBorder() + " 6 " +
                        "\n 5 " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setBorder() + " 5 " +
                        "\n 4 " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " +
                        setBorder() + " 4 " +
                        "\n 3 " + setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setDarkBG() + "   " + setLightBG() + "   " + setDarkBG() + "   " + setLightBG() + "   " +
                        setBorder() + " 3 " +
                        "\n 2 " + setLightBG() + setBlack() + " P " + setDarkBG() + " P " + setLightBG() + " P " +
                        setDarkBG() + " P " + setLightBG() + " P " +setDarkBG() + " P " + setLightBG() + " P " +
                        setDarkBG() + " P " + setBorder() + " 2 " +
                        "\n 1 " + setDarkBG() + setBlack() + " R " + setLightBG() + " N " + setDarkBG() + " B " +
                        setLightBG() + " Q " + setDarkBG() + " K " + setLightBG() + " B " + setDarkBG() + " N " +
                        setLightBG() + " R " + setBorder() + " 1 " +
                        "\n    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + RESET_TEXT_COLOR;
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
