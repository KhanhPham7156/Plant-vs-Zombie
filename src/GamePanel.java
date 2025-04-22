import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import java.awt.Image;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.Image;

public class GamePanel extends JPanel implements ActionListener, MouseInputListener {
    private Image background;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        background = new ImageIcon("image/background.jpg").getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(getGraphics());
        g.drawImage(background, 0, 0, 800, 600, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Click event logic
    }
}
