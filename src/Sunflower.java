import javax.swing.ImageIcon;
import java.awt.Graphics;

public class Sunflower extends Plant {

    private int sunProductionRate;
    private long lastProductionTime;
    private ImageIcon sunflowerIcon; 
    private PlayPanel parent;

    public Sunflower(PlayPanel parent, int x, int y) {
        super(parent, x, y); 
        this.parent = parent;
        setHealth(100); // set initial health for the sunflower
        this.sunProductionRate = 7000; // produces sun every 7 seconds (adjust as needed)
        this.lastProductionTime = System.currentTimeMillis();
        this.sunflowerIcon = new ImageIcon("image-gif/gif/sunflower-pvz.gif"); 
    }

    public void produceSun() {
        if (System.currentTimeMillis() - lastProductionTime >= sunProductionRate) {
        lastProductionTime = System.currentTimeMillis();
        int sunX = getX() + 30;
        int sunY = getY() + 60;
        parent.addSun(sunX, sunY); // Tạo và thêm Sun thật sự vào game
    }
    }

    // draw the sunflower
    public void paint(Graphics g) {
        if (sunflowerIcon != null) {
            sunflowerIcon.paintIcon(parent, g, getX(), getY());
        }
    }
    public void update()
    {
        if (!isDead()) {
            produceSun();
        }
    }
}
