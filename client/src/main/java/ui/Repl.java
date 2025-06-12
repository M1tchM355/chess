package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import websocket.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final GameClient gameClient;
    private Client activeClient;
    private String authToken;
    private String user;

    public Repl(String serverUrl) {
        preloginClient = new PreloginClient(serverUrl);
        postloginClient = new PostloginClient(serverUrl);
        gameClient = new GameClient(serverUrl, this);
        activeClient = preloginClient;
        authToken = null;
        user = "NOT LOGGED IN";
    }

    public void run() {
        System.out.println(WHITE_KING + "Welcome to chess! Sign in to start." + WHITE_KING);
        System.out.print(preloginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = activeClient.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
                String[] words = result.split(" ");
                String firstWord = words[0];
                authToken = activeClient.getAuthToken();
                switch (firstWord) {
                    case "Welcome" -> {
                        user = words[1];
                        activeClient = postloginClient;
                        postloginClient.setAuthToken(authToken);
                        gameClient.setAuthToken(authToken);
                    }
                    case "Bye!" -> {
                        user = "NOT LOGGED IN";
                        activeClient = preloginClient;
                        postloginClient.setAuthToken(null);
                        gameClient.setAuthToken(null);
                    }
                    case "Observing", "Joined" -> {
                        activeClient = gameClient;
                        gameClient.setGameID(postloginClient.getGameID());
                        gameClient.setRole(postloginClient.getRole());
                        gameClient.connect();
                    }
                    case "Left" -> {
                        activeClient = postloginClient;
                        gameClient.leave();
                    }
                }
                result = "";
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    @Override
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(new Gson().fromJson(message, NotificationMessage.class).getMessage());
            case ERROR -> displayError(new Gson().fromJson(message, ErrorMessage.class).getErrorMessage());
            case LOAD_GAME -> loadGame(new Gson().fromJson(message, LoadGameMessage.class).getGame());
        }
    }

    private void displayNotification(String message) {
        System.out.println(SET_TEXT_COLOR_GREEN + message);
        printPrompt();
    }

    private void displayError(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
        printPrompt();
    }

    private void loadGame(String message) {
        gameClient.currentGame = new Gson().fromJson(message, ChessGame.class);
        System.out.println(gameClient.draw());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n " + user + " >>> " + SET_TEXT_COLOR_GREEN);
    }

}
