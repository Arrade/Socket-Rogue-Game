import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client extends JFrame {

    DataInputStream inputStream;
    DataOutputStream outputStream;

    GameScreen gameScreen;

    public Client() {
        try {
            System.out.println("Client Started");
            final Socket s = new Socket("localhost", 4999);

            inputStream = new DataInputStream(s.getInputStream());

            gameScreen = new GameScreen();

            initializeGame();


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
}