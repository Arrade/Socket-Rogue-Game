import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Waiting for clients...");
            ServerSocket ss = new ServerSocket(4999);
            Socket s = ss.accept();
            System.out.println("Connection Established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
