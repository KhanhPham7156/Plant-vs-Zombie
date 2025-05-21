import javax.swing.*;
import java.awt.*;

public class NormalZombie extends Zombie {
    private ImageIcon zombieAttack;
    private ImageIcon zombieWalk;
    private ImageIcon zombieDead;
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 1000; // 1 giây giữa các lần tấn công
    private static final int ATTACK_RANGE = 100; // Phạm vi tấn công
    private static final int Y_RANGE = 30; // Tăng khoảng cách cho phép theo trục Y


    public NormalZombie(int x, int y) {
        super(x, y, 0.5, 100, 20);
        zombieAttack = new ImageIcon("image-gif/gif/zombieAttack.gif");
        zombieWalk = new ImageIcon("image-gif/gif/zombieWalk.gif");
        zombieDead = new ImageIcon("image-gif/gif/zombieDead.gif");
        System.out.println("NormalZombie created at position: " + x + ", " + y);
    }
    
    @Override
    public void draw(Graphics g, JComponent component) {
        String currentState = getState();
        System.out.println("Drawing zombie in state: " + currentState + " at position: " + getX());
        
        switch (currentState) {
            case "walking":
            zombieWalk.paintIcon(component, g, getX(), getY());
                break;
            case "attacking":
            zombieAttack.paintIcon(component, g, getX(), getY());
                break;
            case "dead":
            zombieDead.paintIcon(component, g, getX(), getY());
                break;
            default:
                System.out.println("Unknown state: " + currentState);
                zombieWalk.paintIcon(component, g, getX(), getY());
                break;
        }
    }
    
    @Override
    public void move() {
        if (getState().equals("walking")) {
            super.move();
        }
    }

    public void updateState(Plant plant) {
        if (getState().equals("dead")) {
            return;
        }

        // Kiểm tra xem zombie và plant có ở cùng hàng không
        int yDiff = Math.abs(getY() - plant.getY());
        boolean sameRow = yDiff <= 50; // Tăng khoảng cách cho phép lên 50 pixels
        
        System.out.println("Zombie Y: " + getY() + ", Plant Y: " + plant.getY() + ", Y difference: " + yDiff);
        
        // Chỉ xử lý nếu ở cùng hàng
        if (sameRow) {
            double distance = getX() - plant.getX(); // Khoảng cách từ zombie đến plant
            System.out.println("Zombie X: " + getX() + ", Plant X: " + plant.getX());
            System.out.println("Distance to plant: " + distance + ", Same row: " + sameRow);
            
            // Zombie phải ở bên phải plant và trong phạm vi tấn công
            if (distance > 0 && distance <= ATTACK_RANGE) {
                System.out.println("Zombie is close to plant, current state: " + getState());
                if (!getState().equals("attacking")) {
                    System.out.println("Changing state to attacking");
                    setState("attacking");
                }
                
                // Kiểm tra cooldown trước khi tấn công
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
                    System.out.println("Attacking plant!");
                    attack(plant);
                    lastAttackTime = currentTime;
                }
        } else {
                if (!getState().equals("walking")) {
                    System.out.println("Changing state to walking");
                    setState("walking");
        }
    }
        }
    }

    @Override
    public void attack(Plant plant) {
        System.out.println("Zombie attacking plant at position: " + plant.getX());
        plant.takeDamage(getDamage());
    }

    @Override
    public void takeDamage(int damage) {
        System.out.println("Zombie taking damage: " + damage + ", current health: " + getHealth());
        super.takeDamage(damage);
        if (getHealth() <= 0) {
            System.out.println("Zombie died!");
            setState("dead");
        }
    }
}