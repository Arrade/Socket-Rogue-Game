import java.io.IOException;
import java.util.*;

public class Game {
    private HashMap<String, Player> players;
    private ArrayList<ArrayList<Integer>> items;

    public Game() {
        this.players = new HashMap<String, Player>();
        this.items = new ArrayList<ArrayList<Integer>>();

        //Timer for item spawns below
        Timer timer = new Timer();
        timer.schedule(new ItemSpawn(), 0, 800);
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

    public String getItems() {
        String allItems = "";
        for (ArrayList<Integer> item : items) {
            allItems += item.get(0) + "-" + item.get(1) + "-" + item.get(2) + ">";
        }

        //TODO: Maybe make this more clean?
        return allItems.substring(0, allItems.length() - 1);
    }

    public HashMap<String, Player> getAllPlayerObj() {
        return this.players;
    }

    public ArrayList<ArrayList<Integer>> getAllItemObj() { return this.items; }

    public void addItem(ArrayList<Integer> item) {
        items.add(item);
    }

    public void removeItem(int index) {
        items.remove(index);
    }
}

class ItemSpawn extends TimerTask {
    public void run() {

        if (Server.game.getAllItemObj().size() < 300) {
            ArrayList<Integer> item = new ArrayList<Integer>();

            //Randomize spawn of items
            Random randomizer = new Random();
            int xPos = 100 + randomizer.nextInt(16) * 50;
            int yPos = 50 + randomizer.nextInt(13) * 50;
            int shape = randomizer.nextInt(2);

            item.add(xPos);
            item.add(yPos);
            item.add(shape);

            Server.game.addItem(item);
            System.out.println("Item spawned at " + xPos + ":" + yPos);
            String command = "item_spawn," + xPos + "," + yPos + "," + shape;

            try{
                Server.sendAll(command);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
