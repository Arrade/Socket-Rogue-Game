import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client extends JFrame {

    DataInputStream inputStream;
    static DataOutputStream outputStream;

    GameScreen gameScreen;
    Player player;
    HashMap<String, Player> players = new HashMap<String, Player>();

    public Client() {

        super("Collecting Frenzy");

        //Attempt to connect or throw exception
        try {
            System.out.println("Client Started");
            //Setup socket
            final Socket s = new Socket("localhost", 4999);

            //Setup inputstream
            inputStream = new DataInputStream(s.getInputStream());

            //steps to prepare game
            initializeGame();

            //Setup inputstream
            outputStream = new DataOutputStream(s.getOutputStream());

            //listens to keyInputs
            addKeyListener(new KeyInput());

            String input;
            //Continously listens to inputstreams
            while (true) {
                input = inputStream.readUTF();

                eventHandler(input);

                gameScreen.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public void initializeGame() throws IOException {
        String playerID = inputStream.readUTF();

        //initialize players
        String stringOfPlayers = inputStream.readUTF();
        String[] listOfPlayers = stringOfPlayers.split(">");
        for (String s : listOfPlayers) {
            String[] playerInfo = s.split("-");
            String name = playerInfo[0];
            int x = Integer.parseInt(playerInfo[1]);
            int y = Integer.parseInt(playerInfo[2]);
            Player player = new Player(name, x, y, 50);
            players.put(name, player);
        }
        player = players.get(playerID);
        gameScreen = new GameScreen(player, players);

        //initialize items
        String stringOfItems = inputStream.readUTF();
        String[] listOfItems = stringOfItems.split(">");
        for (String s : listOfItems) {
            String[] location = s.split("-");
            gameScreen.itemSpawn(Integer.parseInt(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]));
        }

        gameScreen.repaint();

        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(gameScreen, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    outputStream.writeUTF("exit");
                } catch (IOException eIO) {
                    eIO.printStackTrace();
                }
            }
        });

    }

    //Key input listener
    private class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            try {
            switch (e.getKeyChar()) {
                case 'w':
                    try {outputStream.writeUTF("move_up"); }
                    catch (IOException ioe) { ioe.printStackTrace(); }
                    break;
                case 's':
                    try {outputStream.writeUTF("move_down"); }
                    catch (IOException ioe) { ioe.printStackTrace(); }
                    break;
                case 'a':
                    try {outputStream.writeUTF("move_left"); }
                    catch (IOException ioe) { ioe.printStackTrace(); }
                    break;
                case 'd':
                    try {outputStream.writeUTF("move_right"); }
                    catch (IOException ioe) { ioe.printStackTrace(); }
                    break;
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void gameOver() throws IOException {
        outputStream.writeUTF("game_over");
    }

    public static void gameWon() throws IOException {
        outputStream.writeUTF("game_won");
    }

    //handles incoming events/commands
    public void eventHandler(String param) {
        String[] k = param.split(",");
        String command = k[0];
        String playerName = k[1];
        System.out.println(command);

        int xPos;
        int yPos;

        switch(command) {
            case "player_joined":
                System.out.println("Player joined: " + playerName);
                xPos = Integer.parseInt(k[2]);
                yPos = Integer.parseInt(k[3]);
                Player p = new Player(playerName, xPos, yPos, 50);
                players.put(playerName, p);
                gameScreen.addPlayer(p);
            break;
            case "move_up":
                players.get(playerName).move_up();
                System.out.println("UP");
            break;
            case "move_down":
                players.get(playerName).move_down();
                System.out.println("DOWN");
            break;
            case "move_left":
                players.get(playerName).move_left();
                System.out.println("LEFT");
            break;
            case "move_right":
                players.get(playerName).move_right();
                System.out.println("RIGHT");
            break;
            case "pickup_item":
                int index = Integer.parseInt(k[2]);
                System.out.println(playerName + " picked up an item");
                players.get(playerName).gainPoint();
                System.out.println(players.get(playerName).getPoints());
                gameScreen.removeItem(index);
            break;
            case "item_spawn":
                xPos = Integer.parseInt(k[1]);
                yPos = Integer.parseInt(k[2]);
                gameScreen.itemSpawn(xPos, yPos, Integer.parseInt(k[3]));
            break;

            case "game_won":
                p = players.get(playerName);
                p.gameWin();
                break;
            case "game_over":
                System.out.println("game completed by: " + playerName);
            break;

            default:
                System.out.println("Invalid command: " + param);
        }
    }
}