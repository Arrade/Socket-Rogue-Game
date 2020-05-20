import java.io.IOException;
import java.util.*;

public class Game {
    private HashMap<String, Player> players;
    private ArrayList<ArrayList<Integer>> items;

    public Game() {
        this.players = new HashMap<String, Player>();
        //TODO: List of Items
        this.items = new ArrayList<ArrayList<Integer>>();
        //TODO: Timer? -> Yes for Items
        Timer timer = new Timer();
        timer.schedule(new ItemSpawn(), 0, 8000);
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public void removePlayer(String playerID) {
        players.remove(playerID);
    }

    public void updatePlayer(Player player) {
        players.put(player.getName(), player);
    }

    public String getPlayers() {
        String allPlayers = "";
        for (Map.Entry<String, Player> player : players.entrySet()) {
            allPlayers += player.getValue().getName() + "-" + player.getValue().getxPos() + "-" + player.getValue().getyPos() + ">";
        }
        //TODO: change this to something more clean...
        return allPlayers.substring(0, allPlayers.length() - 1);
    }

    //TODO: getFunction for Items
    public String getItems() {
        String allItems = "";
        for (ArrayList<Integer> item : items) {
            allItems += item.get(0) + "-" + item.get(1) + ">";
        }

        //TODO: Maybe make this more clean?
        return allItems.substring(0, allItems.length() - 1);
    }

    public HashMap<String, Player> getAllPlayerObj() {
        return this.players;
    }

    //TODO: getAllItemObj
    public ArrayList<ArrayList<Integer>> getAllItemObj() { return this.items; }

    //TODO: Add Item
    public void addItem(ArrayList<Integer> item) {
        items.add(item);
    }
    //TODO: Remove Item
    public void removeItem(int index) {
        items.remove(index);
    }
    //TODO: Item Spawn with timer
}

class ItemSpawn extends TimerTask {
    public void run() {
        //TODO: Set spawn limit of items
        if (Server.game.getAllItemObj().size() < 3) {
            ArrayList<Integer> item = new ArrayList<Integer>();

            //TODO: randomise this
            int xPos = 550;
            int yPos = 550;

            item.add(xPos);
            item.add(yPos);

            Server.game.addItem(item);
            System.out.println("Item spawned at " + xPos + ":" + yPos);
            String command = "item_spawn," + xPos + "," + yPos;

            try{
                Server.sendAll(command);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
