package ui;

import request.LogoutRequest;
import server.ResponseException;

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
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
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
        return "";
    }

    public String list() throws ResponseException {
        return "";
    }

    public String join(String... params) throws ResponseException {
        return "";
    }

    public String observe(String... params) throws ResponseException {
        return "";
    }

    public String logout() throws ResponseException {
        LogoutRequest request = new LogoutRequest(null);
        return "Bye!";
    }
}
