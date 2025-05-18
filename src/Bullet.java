import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Bullet {
private int x, y;
    private int speed = 5;
    private ImageIcon img;
    private boolean active = true;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        img = new ImageIcon("image-gif/image/peabullet.png");
    }

    public void move() {
        x += speed;
        if (x > 800) {
            active = false;
        }
    }

    public void draw(Graphics g, JComponent c) {
        img.paintIcon(c, g, x, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
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
