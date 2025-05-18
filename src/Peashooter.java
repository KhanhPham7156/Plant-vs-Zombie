import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.util.Timer;

public class Peashooter extends Plant
{
    private long lastShotTime;
    private int shootInterval = 2000; // 2 giây
    private ImageIcon img;

    public Peashooter(PlayPanel parent, int x, int y) {
        super(parent, x, y);
        setHealth(150);
        lastShotTime = System.currentTimeMillis();
        img = new ImageIcon("image-gif/gif/peashooter.gif");
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - lastShotTime >= shootInterval) {
            lastShotTime = System.currentTimeMillis();
            getGp().addBullet(new Bullet(getX() + 40, getY() + 20)); // thêm viên đạn
        }
    }

    @Override
    public void paint(Graphics g) {
        img.paintIcon(getGp(), g, getX(), getY());
    }
}
