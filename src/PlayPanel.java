import javax.swing.*;
import java.awt.*;

public class PlayPanel extends JPanel {
    private Image background;

    public PlayPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        background = new ImageIcon("image/map1.png").getImage();
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 800, 600, null);
    }
}
