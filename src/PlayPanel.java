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
    private int sunEnergy = 100;
    private long lastSpawnTime = 0;
    private List<Plant> plants = new ArrayList<>();//danh sách cây đã trồng
    private List<plantCard> plantCards;//vùng chọn cây
    private Image selectedPlantImage;
    private Plant selectedPlant;
    private Point mousePos;

    
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
        
        plantCards = new ArrayList<>();
        loadPlantCards();
        //bắt chuyển động chuột
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
        addMouseListener(this);
    }
    public void loadPlantCards(){
        try {
            Image pea = new ImageIcon("image-gif/image/card_peashooter.png").getImage();
            Image sun = new ImageIcon("image-gif/image/card_sunflower.png").getImage();
            Image wall = new ImageIcon("image-gif/image/card_nut .png").getImage();

            addPlantCard(pea, 80);
            addPlantCard(sun, 160);
            addPlantCard(wall, 240);
        } catch (Exception e) {
            System.out.println("Loading: " + e.getMessage());
        }
    }
    private void addPlantCard(Image img, int x) {
        plantCard card = new plantCard(img);
        card.setBounds(x, 0, 64, 90);
        card.setActionListener(e -> {
            selectedPlantImage = img;
            repaint();
        });
        plantCards.add(card);
        add(card);
    }
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1012, 785, null);
        g.drawImage(plantHolder,0,0,520,100,null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(""+sunEnergy, 30, 90);

        for (Sun sun : suns) 
        {
            sun.draw(g, this);// tạo ssun
        }
         // Vẽ cây được chọn theo con trỏ chuột
         if (selectedPlantImage != null && mousePos != null) {
            g.drawImage(selectedPlantImage, mousePos.x - 32, mousePos.y - 45, 64, 90, null);
        }
    }

    private void updateGame() 
    {
        if (System.currentTimeMillis() - lastSpawnTime > 5000) 
        {
            suns.add(new Sun((int)(Math.random() * 600), 0));
            lastSpawnTime = System.currentTimeMillis();
        }

        for (Sun sun : suns) 
        {
            sun.moving();
        }

        suns.removeIf(Sun::isCollected);
        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        updateGame();
        repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        for (Sun sun : suns) 
        {
            if (sun.isClicked(e.getX(), e.getY())) 
            {
                sun.collect();
                sunEnergy += 25;
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