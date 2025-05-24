import javax.swing.ImageIcon;
import javax.swing.JComponent;

import java.awt.Graphics;

public abstract class Plant 
{
    private int health;
    private int x;
    private int y;

    private PlayPanel parent;
    protected CollisionBox collisionBox;
    protected ImageIcon img;


    public Plant(PlayPanel parent, int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        // Tạo collision box cho plant
        this.collisionBox = new CollisionBox(x, y, 64, 90);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void stop() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        collisionBox.updatePosition(x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        collisionBox.updatePosition(x, y);
    }

    public PlayPanel getGp() {
        return parent;
    }

    public void setGp(PlayPanel gp) {
        this.parent = gp;
    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    // Phương thức abstract để các lớp con triển khai
    public abstract void update();
    public abstract void paint(Graphics g);
}
