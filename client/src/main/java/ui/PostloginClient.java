package ui;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LogoutRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.LogoutResult;
import server.ResponseException;

import java.sql.Array;
import java.util.Arrays;

public class PostloginClient extends Client {
    public PostloginClient(String serverURL){
        super(serverURL);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            //var params = Arrays.copyOf(paramsNoAuth, paramsNoAuth.length + 1);
            //params[params.length - 1] = this.authToken;
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
            String name = params[0];
            CreateGameRequest request = new CreateGameRequest(authToken,name);
            this.server.createGame(request);
            return "You created a new game called " + name + ".";
        }
        throw new ResponseException(400, "Expected create <NAME>");
    }

    public String list(String... params) throws ResponseException {
        return "";
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 2) {
            int ID = Integer.parseInt(params[0]);
            String color = params[1];
            JoinGameRequest request = new JoinGameRequest(color, ID, authToken);
            this.server.joinGame(request);
            return "You joined the game.";
        }
        throw new ResponseException(400, "Expected join <ID>");
    }

    public String observe(String... params) throws ResponseException {
        return "";
    }

    public String logout(String... params) throws ResponseException {
        LogoutRequest request = new LogoutRequest(this.authToken);
        LogoutResult result = this.server.logout(request);
        authToken = null;
        return "Bye!";
    }
}
