import javax.swing.*;
import java.awt.*;

public class Zombie {
    private int x, y;
    private double speed;
    private int health;
    private int damage;
    private String state;
    

    public Zombie(int x, int y, double speed, int health, int damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.damage = damage;
        state = "walking";
    }

    public void move() {
        x -= speed;
        if (x < 10) {
            x = 10;
        }
    }

    public boolean hasReachedEnd(int boundaryX) {
        return x <= boundaryX;//trả về true nếu zombie đến vị trí cuối
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
        }
    }

    public void attack(Plant plant) {
        plant.takeDamage(damage);
    }

    public void draw(Graphics g, JComponent component) {

    }

    public boolean isDead() {
        return health <= 0;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}