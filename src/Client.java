import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client extends JFrame {

    DataInputStream inputStream;
    static DataOutputStream outputStream;

    GameScreen gameScreen;
    Player player;// = new Player("testName",400,400, 50);
    HashMap<String, Player> players = new HashMap<String, Player>();

    public Client() {
        //TODO: Add Super with game name
        try {
            System.out.println("Client Started");
            final Socket s = new Socket("localhost", 4999);

            inputStream = new DataInputStream(s.getInputStream());

            //gameScreen = new GameScreen(player, players);

            initializeGame();

            outputStream = new DataOutputStream(s.getOutputStream());

            addKeyListener(new KeyInput());

            String input;
            while (true) {
                input = inputStream.readUTF();

                // TODO: handle events from inputStream
                eventHandler(input);

                // TODO: reDraw the gameScreen
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
        //System.out.println(playerID);

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

        //TODO: Setup readUTF for spawnable items
        String stringOfItems = inputStream.readUTF();
        String[] listOfItems = stringOfItems.split(">");
        for (String s : listOfItems) {
            String[] location = s.split("-");
            gameScreen.itemSpawn(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
        }

        gameScreen.repaint();

        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //TODO: Center the gameScreen on startup
        add(gameScreen);

    }

    //TODO: change to outputStreams from prints
    private class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            try {
            switch (e.getKeyChar()) {
                case 'w':
                    try {outputStream.writeUTF("move_up"); } catch (IOException ioe) { ioe.printStackTrace(); }
                    //outputStream.writeUTF("move_up");
                    //System.out.println("up");
                    /*if(!(gameScreen.y - 50 <= 0)) {
                        gameScreen.y = gameScreen.y - 50;
                        gameScreen.repaint();
                    }*/
                    break;
                case 's':
                    try {outputStream.writeUTF("move_down"); } catch (IOException ioe) { ioe.printStackTrace(); }
                    //outputStream.writeUTF("move_down");
                    //System.out.println("down");
                    /*if(!(gameScreen.y + 50 >= 700)) {
                        gameScreen.y = gameScreen.y + 50;
                        gameScreen.repaint();
                    }*/
                    break;
                case 'a':
                    try {outputStream.writeUTF("move_left"); } catch (IOException ioe) { ioe.printStackTrace(); }
                    //outputStream.writeUTF("move_left");
                    //System.out.println("left");
                    /*if(!(gameScreen.x - 50 <= 50)) {
                        gameScreen.x = gameScreen.x - 50;
                        gameScreen.repaint();
                    }*/
                    break;
                case 'd':
                    try {outputStream.writeUTF("move_right"); } catch (IOException ioe) { ioe.printStackTrace(); }
                    //System.out.println("right");
                    /*if(!(gameScreen.x + 50 >= 900)) {
                        gameScreen.x = gameScreen.x + 50;
                        gameScreen.repaint();
                    }*/
                    break;
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void eventHandler(String param) {
        String[] k = param.split(",");
        String command = k[0];
        String playerName = k[1];
        System.out.println(command);

        int xPos = 400;
        int yPos = 400;

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
                //player.move_up();
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
                System.out.println(player.getxPos());
                System.out.println("RIGHT");
                System.out.println(player.getxPos());
            break;
            case "pickup_item":
                int index = Integer.parseInt(k[2]);
                System.out.println(playerName + " picked up an item");
                //TODO: effectFunc on payer on ItemPickup
                gameScreen.removeItem(index);
            break;
            case "item_spawn":
                xPos = Integer.parseInt(k[1]);
                yPos = Integer.parseInt(k[2]);
                gameScreen.itemSpawn(xPos, yPos);
            break;

            //TODO: Add GAME OVER / GAME WON

            default:
                System.out.println("END REACHED");
                System.out.println(param);
        }
    }
}