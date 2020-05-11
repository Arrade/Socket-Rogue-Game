public class Player {

    private int xPos;
    private int yPos;
    private int speed;

    public Player(int xPos, int yPos, int speed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
    }

    public void move_up() {
        this.yPos = yPos + speed;
    }

    public void move_down() {

    }

    public void move_left() {

    }

    public void move_right() {

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
}
