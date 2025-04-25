import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
//All game logic/features will be implemented in this class
public class PlayPanel extends JPanel implements ActionListener {
    private Image background;
    private Image plantHolder;

    public PlayPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 800, 600, null);
        g.drawImage(plantHolder,0,0,520,80,null);
    }
}
