import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ThreadedClient extends Thread{
    final Socket s;
    final DataInputStream inputStream;
    final DataOutputStream outputStream;
    String playerID;
    int xPos;
    int yPos;
    Game game;

    public ThreadedClient(
            Socket s,
            DataInputStream inputStream,
            DataOutputStream outputStream,
            String playerID, int xPos, int yPos) {

        this.s = s;
        this.game = Server.game;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.playerID = playerID;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public void run() {

        String recieved;
        game.addPlayer(new Player(playerID, xPos, yPos, 50));
        String allPlayers = game.getPlayers();
        try {
            outputStream.writeUTF(playerID);
            outputStream.writeUTF(allPlayers);
            eventHandler("player_joined");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (true) {
            try {

                //outputStream.writeUTF("move_right");
                recieved = inputStream.readUTF();

                if (recieved.equals("exit")) {
                    break;
                }

                eventHandler(recieved);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        try {
            this.inputStream.close();
            this.outputStream.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void eventHandler(String event) throws IOException {

        Player player;

        switch (event) {
            case "player_joined":
                System.out.println("Player joined: " + playerID);
                String command = "player_joined," + playerID + "," + xPos + "," + yPos;
                Server.sendAll(command);
            break;
            case "move_up":
                player = game.getPlayer(playerID);
                if (!(player.getyPos() - 50 <= 0) && moveBlock(player)) {
                    player.move_up();
                    extraFunc("move_up,", player);
                }
            break;
            case "move_down":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getyPos() + 50 >= 700)) {
                    player.move_down();
                    extraFunc("move_down,", player);
                }
            break;
            case "move_left":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getxPos() - 50 <= 50)) {
                    player.move_left();
                    extraFunc("move_left,", player);
                }
            break;
            case "move_right":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getxPos() + 50 >= 900)) {
                    player.move_right();
                    extraFunc("move_right,", player);
                }
            break;
            default:
                System.out.println("Invalid Move");
        }
    }

    public void extraFunc(String cmdWord, Player player) throws IOException {
        game.updatePlayer(player);
        String command = cmdWord + playerID;
        //send(command); //temporary
        Server.sendAll(command);
    }

    //TODO: Fix this for all directions
    public boolean moveBlock(Player player) {
        HashMap<String, Player> playerList = game.getAllPlayerObj();
        for (Map.Entry<String, Player> obj : playerList.entrySet()) {
            if (obj.getValue().getyPos() == player.getyPos() - 50 && obj.getValue().getxPos() == player.getxPos()) {
                return false;
            }
        }
        return true;
    }

    public void send(String event) throws IOException {
        outputStream.writeUTF(event);
    }
}
