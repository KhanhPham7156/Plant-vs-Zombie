import javax.swing.ImageIcon;
import java.awt.Graphics;

public class Sunflower extends Plant {

    private int sunProductionRate;
    private long lastProductionTime;
    private ImageIcon sunflowerIcon; 

    public Sunflower(GamePanel parent, int x, int y) {
        super(parent, x, y); 
        setHealth(100); // set initial health for the sunflower
        this.sunProductionRate = 5000; // produces sun every 5 seconds (adjust as needed)
        this.lastProductionTime = System.currentTimeMillis();
        this.sunflowerIcon = new ImageIcon("image-gif/gif/sunflower-pvz.gif"); 
    }

    public int produceSun() {
        if (System.currentTimeMillis() - lastProductionTime >= sunProductionRate) {
            lastProductionTime = System.currentTimeMillis();
            return 25; // amount of sun produced
        }
        return 0;
    }

    // draw the sunflower
    public void paint(Graphics g) {
        sunflowerIcon.paintIcon(getGp(), g, getX(), getY());
    }
}
