import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Wallnut extends Plant{
private ImageIcon img;

    public Wallnut(PlayPanel parent, int x, int y) {
        super(parent, x, y);
        setHealth(400); // Máu trâu hơn
        img = new ImageIcon("image-gif/gif/wall-nut.gif");
    }

    @Override
    public void update() {
        // Không cần làm gì mỗi frame
    }

    @Override
    public void paint(Graphics g) {
        img.paintIcon(getGp(), g, getX(), getY());
    }
}
