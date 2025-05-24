import java.awt.Graphics;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet {
    private int x, y;
    private int speed = 5;
    private BufferedImage img;
    private boolean active = true;
    private CollisionBox collisionBox;
    private static final int BULLET_SIZE = 20; // Increased from 15 to 20

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        
        // Create a more attractive bullet image
        img = new BufferedImage(BULLET_SIZE, BULLET_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient from medium green to dark green
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0, 180, 0),      // Medium green
            BULLET_SIZE, BULLET_SIZE, new Color(0, 100, 0),  // Dark green
            true
        );
        g2d.setPaint(gradient);
        
        // Draw main bullet body
        g2d.fillOval(0, 0, BULLET_SIZE-1, BULLET_SIZE-1);
        
        // Add highlight
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillOval(4, 4, 7, 7);
        
        // Add border
        g2d.setColor(new Color(0, 80, 0));
        g2d.drawOval(0, 0, BULLET_SIZE-1, BULLET_SIZE-1);
        
        g2d.dispose();
        
        collisionBox = new CollisionBox(x, y, BULLET_SIZE, BULLET_SIZE);
    }

    public void move() {
        x += speed;
        collisionBox.updatePosition(x, y);
        if (x > 800) {
            active = false;
        }
    }

    public void draw(Graphics g, JComponent c) {
        if (img != null) {
            g.drawImage(img, x, y, null);
        }
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
