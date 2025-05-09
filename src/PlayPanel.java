import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

//All game logic/features will be implemented in this class
public class PlayPanel extends JPanel implements ActionListener, MouseListener {
    private Image background;
    private Image plantHolder;
    private ArrayList<Sun> suns;
    private Timer timer;
    private int sunPoints = 0;
    private JTextField textField;
    private ArrayList<Zombie> zombies;

    public PlayPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        
        textField = new JTextField(String.valueOf(sunPoints));
        textField.setBounds(38, 50, 100, 30);
        textField.setEditable(false);
        textField.setOpaque(false); // Làm nền trong suốt
        textField.setBorder(null); // Xóa viền
        textField.setForeground(Color.BLACK); // Màu chữ (chọn màu tương phản với nền)
        textField.setFont(new Font("Arial", Font.BOLD, 20)); // Phông chữ rõ ràng
        add(textField);
        //Danh sách chứa sun
        suns = new ArrayList<>();
        addMouseListener(this);
        timer = new Timer(16, this);
        timer.start();

        //Danh sách chứa zombie
        zombies = new ArrayList<>();
        timer = new Timer(16, this);
        timer.start();

        suns.add(new Sun(300, 60));
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 800, 600, null);
        g.drawImage(plantHolder, 0, 0, 520, 80, null);

        for (Sun sun : suns) {
            sun.draw(g, this);
        }

        for (Zombie zombie : zombies) {
            zombie.draw(g, this);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        // Xử lý suns
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

        // Xử lý zombies
        Iterator<Zombie> zombieIterator = zombies.iterator();
        

        if (Math.random() < 0.01) { // Xác suất spawn zombie
            int randomY = (int) (Math.random() * 400) + 100; // Vị trí Y ngẫu nhiên
            zombies.add(new NormalZombie(800, randomY)); // Thêm zombie mới
        }
    
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        // Kiểm tra nếu Sun được nhấp
        for (Sun sun : suns) {
            if (sun.isClicked(e.getX(), e.getY())) {
                sun.collect(); // Thu thập Sun
                sunPoints += 25;// Tăng điểm
                textField.setText(String.valueOf(sunPoints));//Hiển thị lại điểm mới
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {}


    public void mouseReleased(MouseEvent e) {}


    public void mouseEntered(MouseEvent e) {}


    public void mouseExited(MouseEvent e) {}
}
