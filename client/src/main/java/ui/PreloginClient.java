package ui;

import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import server.ResponseException;

import java.util.Arrays;

public class PreloginClient extends Client {
    public PreloginClient(String serverURL){
        super(serverURL);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help(){
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to login
                quit - quit application
                help - see possible commands
                """;
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                String username = params[0];
                String password = params[1];
                LoginRequest request = new LoginRequest(username, password);
                LoginResult result = this.server.login(request);
                this.authToken = result.authToken();
                return "Welcome " + username;
            } catch (Exception e) {
                throw new ResponseException(500, "Error logging in. Try again");
            }
        }
        throw new ResponseException(400, "Expected login <USERNAME> <PASSWORD>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                String username = params[0];
                String password = params[1];
                String email = params[2];
                RegisterRequest request = new RegisterRequest(username, password, email);
                RegisterResult result = this.server.register(request);
                this.authToken = result.authToken();
                return "Welcome " + username;
            } catch (Exception e) {
                throw new ResponseException(500, "Couldn't register you. Try logging in");
            }
        }
        throw new ResponseException(400, "Expected register <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String quit() {
        return "quit";
    }
}
