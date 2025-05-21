import java.awt.Rectangle;

public class CollisionBox {
    private int x, y, width, height;
    private Rectangle bounds;
    
    public CollisionBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds.setLocation(x, y);
    }
    
    public boolean intersects(CollisionBox other) {
        return this.bounds.intersects(other.bounds);
    }
    
    public boolean contains(int x, int y) {
        return this.bounds.contains(x, y);
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
} 