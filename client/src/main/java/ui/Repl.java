package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final GameClient gameClient;
    private Client activeClient;
    private boolean loggedIn;
    private boolean inGame;
    private String authToken;

    public Repl(String serverUrl) {
        preloginClient = new PreloginClient(serverUrl);
        postloginClient = new PostloginClient(serverUrl);
        gameClient = new GameClient(serverUrl);
        activeClient = preloginClient;
        loggedIn = false;
        inGame = false;
        authToken = null;
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
                String firstWord = result.split(" ")[0];
                if (firstWord.equals("Welcome")) {
                    loggedIn = true;
                    activeClient = postloginClient;
                } else if (firstWord.equals("Bye")) {
                    loggedIn = false;
                    activeClient = preloginClient;
                }
                authToken = activeClient.getAuthToken();
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        if (!loggedIn) {
            System.out.print("\n NOT LOGGED IN >>> " + SET_TEXT_COLOR_GREEN);
        } else {
            System.out.print("\n LOGGED IN >>> " + SET_TEXT_COLOR_GREEN);
        }
    }

}
