import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class GameScreen extends JPanel {

    HashMap<String, Player> players;
    Player player;

    public GameScreen(Player player, HashMap<String, Player> players) {
        this.players = players;
        this.player = player;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //g2d.drawRect(player.getxPos(),player.getyPos(), 50,50);
        g2d.drawRect(100,50,800, 650);

        for (Map.Entry<String, Player> playerEntry : players.entrySet()) {
            g2d.drawRect(playerEntry.getValue().getxPos(),playerEntry.getValue().getyPos(), 50,50);
        }
    }

    public void addPlayer(Player player) {
        this.players.put(player.getName(), player);
    }

    public void updatePlayer(HashMap<String, Player> players) {
        this.players = players;
    }
}
