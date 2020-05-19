public class Player {

    private int xPos;
    private int yPos;
    private int speed;
    private String name;

    public Player(String name, int xPos, int yPos, int speed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        this.name = name;
    }

    public void move_up() {
        this.yPos = yPos - speed;
    }

    public void move_down() {
        this.yPos = yPos + speed;
    }

    public void move_left() {
        this.xPos = xPos - speed;
    }

    public void move_right() {
        this.xPos = xPos + speed;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() { return name; }
}
