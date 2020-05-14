import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Waiting for clients...");
            ServerSocket ss = new ServerSocket(4999);
            Socket s = ss.accept();
            System.out.println("Connection Established");

            DataInputStream ips = new DataInputStream(s.getInputStream());
            DataOutputStream ops = new DataOutputStream(s.getOutputStream());

            System.out.println(ips.readUTF());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
