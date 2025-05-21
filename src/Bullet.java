import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Bullet {
    private int x, y;
    private int speed = 5;
    private ImageIcon img;
    private boolean active = true;
    private CollisionBox collisionBox;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        img = new ImageIcon("image-gif/image/peabullet.png");
        // Tạo collision box nhỏ hơn hình ảnh để va chạm chính xác hơn
        collisionBox = new CollisionBox(x, y, 15, 15);
    }

    public void move() {
        x += speed;
        collisionBox.updatePosition(x, y);
        if (x > 800) {
            active = false;
        }
    }

    public void draw(Graphics g, JComponent c) {
        img.paintIcon(c, g, x, y);
    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
