import javax.swing.*;
import java.awt.*;

public class Zombie {
    private int x, y;
    private double speed;
    private int health;
    private int damage;
    protected String state;
    private CollisionBox collisionBox;
    private ImageIcon img;
    private long deathTime;
    

    public Zombie(int x, int y, double speed, int health, int damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.damage = damage;
        this.state = "walking";
        // Tạo collision box cho zombie
        this.collisionBox = new CollisionBox(x, y, 60, 100);
        this.img = new ImageIcon("image-gif/image/zombie.png");
    }

    public void move() {
        if (getState().equals("walking")) {
        x -= speed;
            collisionBox.updatePosition(x, y);
        if (x < 10) {
            x = 10;
            }
        }
    }

    public boolean hasReachedEnd(int boundaryX) {
        return x <= boundaryX;//trả về true nếu zombie đến vị trí cuối
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            setState("dead");
        }
    }

    public void attack(Plant plant) {
        if (collisionBox.intersects(plant.getCollisionBox())) {
            plant.takeDamage(damage);
        } else {
            System.out.println("Missed plant!");
        }
    }
    
    public void setState(String state) {
        if (state.equals("dead") && !this.state.equals("dead")) {
            this.deathTime = System.currentTimeMillis(); // Lưu thời điểm chuyển sang trạng thái "dead"
        }
        this.state = state;
    }

    public boolean isReadyToRemove() {
        // Chỉ xóa zombie sau 2 giây kể từ khi chuyển sang trạng thái "dead"
        return state.equals("dead") && (System.currentTimeMillis() - deathTime > 2000);
    }

    public void draw(Graphics g, JComponent component) {
        img.paintIcon(component, g, x, y);
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

    public int getDamage() {
        return damage;
    }


    public CollisionBox getCollisionBox() {
        return collisionBox;
    }
}