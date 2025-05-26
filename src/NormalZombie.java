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

            boolean isColliding = getCollisionBox().intersects(plant.getCollisionBox());
            if (isColliding) {
                if (!getState().equals("attacking")) {
                    setState("attacking");
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
                    attack(plant);
                    lastAttackTime = currentTime;
                }
            } else {
                // If no collision, revert to walking
                if (!getState().equals("walking")) {
                    setState("walking");
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