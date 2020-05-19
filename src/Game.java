import java.util.HashMap;
import java.util.Map;

public class Game {
    private HashMap<String, Player> players;

    public Game() {
        this.players = new HashMap<String, Player>();

        //TODO: Timer?
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

    public HashMap<String, Player> getAllPlayerObj() {
        return this.players;
    }
}
