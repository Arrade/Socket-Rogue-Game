import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
        //TODO: get all items from game
        String allItems = game.getItems();

        try {
            outputStream.writeUTF(playerID);
            outputStream.writeUTF(allPlayers);
            outputStream.writeUTF(allItems);
            //TODO: writeUTF allItems
            eventHandler("player_joined");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (true) {
            try {

                //outputStream.writeUTF("move_right");
                recieved = inputStream.readUTF();

                //TODO: Fix exit
                if (recieved.equals("exit")) {
                    break;
                }

                //TODO: Add if statement for GAME OVER / GAME WON

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
                if (!(player.getyPos() - 50 <= 0) && moveBlock(player, "move_up")) {
                    player.move_up();
                    extraFunc("move_up,", player);
                }
            break;
            case "move_down":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getyPos() + 50 >= 700) && moveBlock(player, "move_down")) {
                    player.move_down();
                    extraFunc("move_down,", player);
                }
            break;
            case "move_left":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getxPos() - 50 <= 50) && moveBlock(player, "move_left")) {
                    player.move_left();
                    extraFunc("move_left,", player);
                }
            break;
            case "move_right":
                player = game.getPlayer(playerID);
                //TODO: add moveBock
                if (!(player.getxPos() + 50 >= 900) && moveBlock(player, "move_right")) {
                    checkItem(player);
                    player = game.getPlayer(playerID);
                    player.move_right();
                    extraFunc("move_right,", player);
                }
            break;

            //TODO: Add case for action if needed...

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

    public void checkItem(Player player) throws IOException {
        ArrayList<ArrayList<Integer>> items = game.getAllItemObj();
        int index = 0;
        for (ArrayList<Integer> item : items) {
            if (player.getxPos() == item.get(0) && player.getyPos() == item.get(1)) {
                pickupItem(index, player);
                break;
            }
            index++;
        } //TODO: game.updatePlayer ?
        game.updatePlayer(player);
    }

    //TODO: Fix this for all directions
    public boolean moveBlock(Player player, String move) {
        HashMap<String, Player> playerList = game.getAllPlayerObj();
        for (Map.Entry<String, Player> obj : playerList.entrySet()) {
            switch (move) {
                //TODO: Change 50 to player speed instead
                case "move_up":
                    if (obj.getValue().getyPos() == player.getyPos() - 50 && obj.getValue().getxPos() == player.getxPos()) {
                        return false;
                    }
                break;
                case "move_down":
                    if (obj.getValue().getyPos() == player.getyPos() + 50 && obj.getValue().getxPos() == player.getxPos()) {
                        return false;
                    }
                break;
                case "move_left":
                    if (obj.getValue().getxPos() == player.getxPos() - 50 && obj.getValue().getyPos() == player.getyPos()) {
                        return false;
                    }
                break;
                case "move_right":
                    if (obj.getValue().getxPos() == player.getxPos() + 50 && obj.getValue().getyPos() == player.getyPos()) {
                        return false;
                    }
                break;
            }
        }
        return true;
    }

    //TODO: Add function for Item pickup, update game/player etc ~
    public void pickupItem(int index, Player player) throws IOException {
        game.removeItem(index);
        //TODO: effectFunc on payer on ItemPickup
        System.out.println(player.getName() + " picked up an item");
        String command = "pickup_item," + player.getName() + "," + index;
        Server.sendAll(command);
    }

    public void send(String event) throws IOException {
        outputStream.writeUTF(event);
    }
}
