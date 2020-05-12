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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public void initializeGame() {
        setSize(400,400);
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
                    break;
                case 's':
                    //outputStream.writeUTF("move_down");
                    System.out.println("down");
                    break;
                case 'a':
                    //outputStream.writeUTF("move_left");
                    System.out.println("left");
                    break;
                case 'd':
                    //outputStream.writeUTF("move_right");
                    System.out.println("right");
                    break;
                }
            /*} catch (IOException ioe) {
                ioe.printStackTrace();
            }*/
        }
    }
}