import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


//All game logic/features will be implemented in this class
public class PlayPanel extends JPanel implements ActionListener,MouseListener 
{
    private Image background;
    private Image plantHolder;
    private Timer timer;
    private ArrayList<Sun> suns;
    private Timer timer;
    private int sunPoints = 0;
    private JTextField textField;

    
    public PlayPanel() 
    {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        setLayout(null);

        suns = new ArrayList<>();
        timer = new Timer(5, this);
        timer.start();

        suns.add(new Sun(300, 60));
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1012, 785, null);
        g.drawImage(plantHolder,0,0,520,100,null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(""+sunEnergy, 40, 90);

        for (Sun sun : suns) 
        {
            sun.draw(g, this);// tạo ssun
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Iterator<Sun> iterator = suns.iterator();
        while (iterator.hasNext()) {
            Sun sun = iterator.next();
            if (sun.isCollected()) {
                sun.collect();
                iterator.remove();
            }
        }

        if (Math.random() < 0.005) {// Muôn tăng giảm thời gian spawn sun, có thể tăng hoặc giảm 0.005
            int randomX = (int) (Math.random() * 700); // Vị trí X ngẫu nhiên
            suns.add(new Sun(randomX, 60)); // Thêm Sun mới
        }

        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        // Kiểm tra nếu Sun được nhấp
        for (Sun sun : suns) {
            if (sun.isClicked(e.getX(), e.getY())) {
                sun.collect(); // Thu thập Sun
                sunPoints++;
                textField.setText(String.valueOf(sunPoints));
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) 
    {

    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
        
    }
}   