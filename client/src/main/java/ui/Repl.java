package ui;

import websocket.ServerMessageObserver;
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
                if (firstWord.equals("Welcome")) {
                    user = words[1];
                    activeClient = postloginClient;
                    postloginClient.setAuthToken(authToken);
                    gameClient.setAuthToken(authToken);
                } else if (firstWord.equals("Bye!")) {
                    user = "NOT LOGGED IN";
                    activeClient = preloginClient;
                    postloginClient.setAuthToken(null);
                    gameClient.setAuthToken(null);
                } else if (firstWord.equals("Observing")) {

                } else if (firstWord.equals("Joined")) {
                    activeClient = gameClient;
                } else if (firstWord.equals("Left")) {
                    activeClient = postloginClient;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    @Override
    public void notify(ServerMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
        printPrompt();
    }

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n " + user + " >>> " + SET_TEXT_COLOR_GREEN);
    }

}
