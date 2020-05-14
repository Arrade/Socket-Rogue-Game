import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {

    DataInputStream inputStream;
    DataOutputStream outputStream;

    GameScreen gameScreen;
    Player player;

    public Client() {
        try {
            System.out.println("Client Started");
            final Socket s = new Socket("localhost", 4999);

            inputStream = new DataInputStream(s.getInputStream());

            gameScreen = new GameScreen();

            initializeGame();

            outputStream = new DataOutputStream(s.getOutputStream());

            addKeyListener(new KeyInput());

            String input;
            /*while (true) {
                input = inputStream.readUTF();

                // TODO: handle events from inputStream

                // TODO: reDraw the gameScreen
            }*/


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public void initializeGame() {
        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(gameScreen);

    }

    //TODO: change to outputStreams from prints
    private class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            //try {
            switch (e.getKeyChar()) {
                case 'w':
                    //outputStream.writeUTF("move_up");
                    System.out.println("up");
                    if(!(gameScreen.y - 50 <= 0)) {
                        gameScreen.y = gameScreen.y - 50;
                        gameScreen.repaint();
                    }
                    break;
                case 's':
                    //outputStream.writeUTF("move_down");
                    System.out.println("down");
                    if(!(gameScreen.y + 50 >= 700)) {
                        gameScreen.y = gameScreen.y + 50;
                        gameScreen.repaint();
                    }
                    break;
                case 'a':
                    //outputStream.writeUTF("move_left");
                    System.out.println("left");
                    if(!(gameScreen.x - 50 <= 50)) {
                        gameScreen.x = gameScreen.x - 50;
                        gameScreen.repaint();
                    }
                    break;
                case 'd':
                    try {outputStream.writeUTF("move_right"); } catch (IOException ioe) { ioe.printStackTrace(); }
                    System.out.println("right");
                    if(!(gameScreen.x + 50 >= 900)) {
                        gameScreen.x = gameScreen.x + 50;
                        gameScreen.repaint();
                    }
                    break;
                }
            /*} catch (IOException ioe) {
                ioe.printStackTrace();
            }*/
        }
    }

    public void eventHandler(String param) {
        String k ="";
        switch(k) {
            case "up":
                player.move_up();
            break;
            case "down":
                player.move_down();
            break;
            case "left":
                player.move_left();
            break;
            case "right":
                player.move_right();
            break;
        }
    }
}