import javax.swing.*;
import java.awt.*;

public class NormalZombie extends Zombie {
    private ImageIcon zombieAttack;
    private ImageIcon zombieWalk;
    private ImageIcon zombieDead;
    private String state;

    public NormalZombie(int x, int y) {
        super(x, y, 10, 100, 10);

        zombieAttack = new ImageIcon("image-gif/image/zombieAttack.gif");
        zombieWalk = new ImageIcon("image-gif/image/zombieWalk.gif");
        zombieDead = new ImageIcon("image-gif/image/zombieDead.gif");
        state = "walking";
    }
    
    public void draw(Graphics g, JComponent component) {
        if (state.equals("walking")) {
            zombieWalk.paintIcon(component, g, getX(), getY());
        }
        if (state.equals("attacking")) {
            zombieAttack.paintIcon(component, g, getX(), getY());
        }
        if (state.equals("dead")) {
            zombieDead.paintIcon(component, g, getX(), getY());
        }
    }
    
    public void move() {
        if (state.equals("walking")) {
            super.move(); // Zombie chỉ di chuyển khi đang ở trạng thái "walking"
        }
    }

    public void updateState(/*thêm list plant */) {
        if (state.equals("dead")) {
            return; // Zombie đã chết, không làm gì
        }
        if (getX() <= plant.getX()) { // Nếu zombie đến vị trí của cây
            state = "attacking";
            attack(plant); // Gọi phương thức tấn công với đối tượng cây
        } else {
            state = "walking";
        }
    }

    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getHealth() <= 0) {
            state = "dead";
        }
    }
}
