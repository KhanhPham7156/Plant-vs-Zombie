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
    }
    
    @Override
    public void draw(Graphics g, JComponent component) {
        String currentState = getState();     
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
        
        
        // Chỉ xử lý nếu ở cùng hàng
        if (sameRow) {
            double distance = getX() - plant.getX(); // Khoảng cách từ zombie đến plant
            
            // Zombie phải ở bên phải plant và trong phạm vi tấn công
            if (distance > 0 && distance <= ATTACK_RANGE) {
                if (!getState().equals("attacking")) {
                    setState("attacking");
                }
                
                // Kiểm tra cooldown trước khi tấn công
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
                    attack(plant);
                    lastAttackTime = currentTime;
                }
        } else {
                if (!getState().equals("walking")) {
                    setState("walking");
        }
    }
        }
    }

    @Override
    public void attack(Plant plant) {
        plant.takeDamage(getDamage());
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getHealth() <= 0) {
            setState("dead");
        }
    }
}