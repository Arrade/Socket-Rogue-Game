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
        String allItems = game.getItems();

        try {
            //Send information to corresponding Client
            outputStream.writeUTF(playerID);
            outputStream.writeUTF(allPlayers);
            outputStream.writeUTF(allItems);
            eventHandler("player_joined");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (true) {
            try {

                recieved = inputStream.readUTF();

                if (recieved.equals("exit")) {
                    System.out.println("Client " + this.playerID + " has left");
                    this.s.close();
                    Server.removeThread(playerID);
                    Server.sendAll("game_over," + this.playerID);
                    System.out.println("Connection closed");
                    break;
                }

                //checking if all players have completed their game tasks
                if (recieved.equals("game_over")) {
                    System.out.println("Game over for " + this.playerID);
                    HashMap<String, Player> playerObj = game.getAllPlayerObj();
                    int i = 0;
                    int k = 0;
                    for (Map.Entry<String, Player> p : playerObj.entrySet()) {
                        k++;
                        if(p.getValue().gameOver){
                            i++;
                        }
                    }
                    if (i == k){ Server.sendAll("game_won," + playerID); }
                }

                //As long as the player isn't done we continue to recieve events
                if (!game.getPlayer(playerID).gameOver) {
                    eventHandler(recieved);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        try {
            //close I/O streams
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
                //check border restrictions
                if (!(player.getyPos() - 50 <= 0) && moveBlock(player, "move_up")) {
                    checkItem(player);
                    player = game.getPlayer(playerID);
                    player.move_up();
                    extraFunc("move_up,", player);
                }
            break;
            case "move_down":
                player = game.getPlayer(playerID);
                //check border restrictions
                if (!(player.getyPos() + 50 >= 700) && moveBlock(player, "move_down")) {
                    checkItem(player);
                    player = game.getPlayer(playerID);
                    player.move_down();
                    extraFunc("move_down,", player);
                }
            break;
            case "move_left":
                player = game.getPlayer(playerID);
                //check border restrictions
                if (!(player.getxPos() - 50 <= 50) && moveBlock(player, "move_left")) {
                    checkItem(player);
                    player = game.getPlayer(playerID);
                    player.move_left();
                    extraFunc("move_left,", player);
                }
            break;
            case "move_right":
                player = game.getPlayer(playerID);
                //check border restrictions
                if (!(player.getxPos() + 50 >= 900) && moveBlock(player, "move_right")) {
                    checkItem(player);
                    player = game.getPlayer(playerID);
                    player.move_right();
                    extraFunc("move_right,", player);
                }
            break;

            default:
                System.out.println("Invalid Move");
        }
    }

    //Help sending out the command to all ThreadedClients
    public void extraFunc(String cmdWord, Player player) throws IOException {
        game.updatePlayer(player);
        String command = cmdWord + playerID;
        Server.sendAll(command);
    }

    //Checks if a player stepped on an item, as well as if it's the correct shape for the player
    public void checkItem(Player player) throws IOException {
        ArrayList<ArrayList<Integer>> items = game.getAllItemObj();
        int index = 0;
        for (ArrayList<Integer> item : items) {
            System.out.println(playerID);
            if (player.getxPos() == item.get(0) && player.getyPos() == item.get(1) && Integer.parseInt(playerID.substring(playerID.length()-1)) == item.get(2)+1) {
                pickupItem(index, player);
                break;
            }
            index++;
        }
        game.updatePlayer(player);
    }

    //Check if the player moves into another player
    public boolean moveBlock(Player player, String move) {
        HashMap<String, Player> playerList = game.getAllPlayerObj();
        for (Map.Entry<String, Player> obj : playerList.entrySet()) {
            switch (move) {
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

    //Handles pickup of points
    public void pickupItem(int index, Player player) throws IOException {
        game.removeItem(index);
        player.gainPoint();
        System.out.println(player.getName() + " picked up an item");
        String command = "pickup_item," + player.getName() + "," + index;
        Server.sendAll(command);
    }

    //sends event to outputstream -> corresponding Client
    public void send(String event) throws IOException {
        outputStream.writeUTF(event);
    }
}
