import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScreen extends JPanel {

    HashMap<String, Player> players;
    ArrayList<ArrayList<Integer>> items;
    Player player;
    Boolean gameOver = false;

    public GameScreen(Player player, HashMap<String, Player> players) {
        this.players = players;
        this.player = player;
        this.items = new ArrayList<ArrayList<Integer>>(); //TODO: Make this into a GameScreen parameter instead
    }

    public void paintComponent(Graphics g) {
        Player cPlayer = players.get(player.getName());
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Borders
        g2d.drawRect(100,50,800, 650);

        //draw all items
        for (ArrayList<Integer> item : items) {
            if (item.get(2) == 0) {
                g2d.drawRect(item.get(0), item.get(1), 25, 25);
            } else {
                g2d.drawOval(item.get(0), item.get(1), 25, 25);
            }
        }

        //draw all players
        for (Map.Entry<String, Player> playerEntry : players.entrySet()) {
            g2d.drawRect(playerEntry.getValue().getxPos(),playerEntry.getValue().getyPos(), 50,50);
        }

        //All players are done
        if (cPlayer.gameWon) {
            g2d.drawString("GAME OVER, GOOD JOB", 250, 250);
            try {
                Client.gameWon();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            //When All - x amount of players are done where x > 0
        } else if (cPlayer.gameOver) {
            g2d.drawString("Task Completed, Waiting for ally to finish", 400, 400);
            try {
                Client.gameOver();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void addPlayer(Player player) {
        this.players.put(player.getName(), player);
    }

    public void updatePlayer(HashMap<String, Player> players) {
        this.players = players;
    }

    public void itemSpawn(int xPos, int yPos, int shape) {
        ArrayList<Integer> item = new ArrayList<Integer>();
        item.add(xPos);
        item.add(yPos);
        item.add(shape);
        items.add(item);
    }

    public void removeItem(int index) {
        items.remove(index);
    }
}
