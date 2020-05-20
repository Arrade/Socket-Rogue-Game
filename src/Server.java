import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    static HashMap<String, ThreadedClient> allPlayers;
    static Game game = new Game();

    public static void main(String[] args) throws IOException {
        int ID = 0;
        allPlayers = new HashMap<String, ThreadedClient>();
        ServerSocket ss = new ServerSocket(4999);

        while(true) {
            try {
                System.out.println("Waiting for clients...");
                Socket s = ss.accept();
                ID++;
                String pID = "player " + ID;
                System.out.println("Connection Established With : " + s);

                DataInputStream ips = new DataInputStream(s.getInputStream());
                DataOutputStream ops = new DataOutputStream(s.getOutputStream());

                //TODO: maybe randomise player spawn, EDIT: unnecessary garbage, add later

                ThreadedClient t = new ThreadedClient(s, ips, ops, pID, 400, 400);

                allPlayers.put(pID, t);

                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void sendAll(String event) throws IOException {
        if (allPlayers != null && !allPlayers.isEmpty()) {
            for (Map.Entry<String, ThreadedClient> t : allPlayers.entrySet()) {
                t.getValue().send(event);
            }
        } else {
            System.out.println("FAILED");
        }
    }

    public static void removeThread(String pID){
        allPlayers.remove(pID);
    }
}
