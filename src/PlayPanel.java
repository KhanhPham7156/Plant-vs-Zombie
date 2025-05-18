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
    private ArrayList<Zombie> zombies;
    private int sunEnergy = 100;
    private long lastSpawnTime = 0;
    private long lastZombieSpawnTime = 0;
    private List<Plant> plants = new ArrayList<>();
    private List<plantCard> plantCards;//vùng chọn cây
    private Image selectedPlantImage;
    private String selectedPlantType;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private Plant selectedPlant;
    private Point mousePos;
    // Kích thước grid để đặt cây
    public static final int GRID_START_X = 230; // điểm bắt đầu X của grid (ước lượng từ map)
    public static final int GRID_START_Y = 70; // điểm bắt đầu Y của grid
    public static final int CELL_WIDTH = 147;    // chiều rộng mỗi ô
    public static final int CELL_HEIGHT = 100;  // chiều cao mỗi ô
    public static final int NUM_COLS = 9;       // số cột (ô ngang)
    public static final int NUM_ROWS = 5;       // số hàng (ô dọc)
    
    public PlayPanel() 
    {
        setPreferredSize(new Dimension(1326, 570));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        setLayout(null);

        suns = new ArrayList<>();
        zombies = new ArrayList<>();
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

            addPlantCard(pea, 80,"Peashooter");
            addPlantCard(sun, 160, "Sunflower");
            addPlantCard(wall, 240,"Wallnut");
        } catch (Exception e) {
            System.out.println("Loading: " + e.getMessage());
        }
    }
    //thêm card
    private void addPlantCard(Image img, int x,String type) {
        plantCard card = new plantCard(img);
        card.setBounds(x, 0, 64, 90);
        card.setActionListener(e -> {
            selectedPlantImage = img;
            selectedPlantType = type;
            repaint();
        });
        plantCards.add(card);
        add(card);
    }
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1326, 570, null);
        g.drawImage(plantHolder, 0, 0, 520, 100, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(""+sunEnergy, 30, 90);

        for (Sun sun : suns) {
            sun.draw(g, this);// tạo sun
        }

        for (Zombie zombie : zombies) {
            zombie.draw(g, this);// tạo zombie
        }
        for (Plant plant : plants) {
            plant.paint(g);
        }

        // Vẽ cây được chọn theo con trỏ chuột
        if (selectedPlantImage != null && mousePos != null) {
            g.drawImage(selectedPlantImage, mousePos.x - 32, mousePos.y - 45, 64, 90, null);
        }
        // vẽ đạn
        for (Bullet bullet : bullets) {
            bullet.draw(g, this);
}

    }
    
    private void updateGame() {
        // Zombie
        if (System.currentTimeMillis() - lastZombieSpawnTime > 5000) {
            int [] rows = {100, 200, 350, 450};
            int row = rows[(int) (Math.random() * rows.length)];
            zombies.add(new NormalZombie(800, row));
            lastZombieSpawnTime = System.currentTimeMillis();
        }

        for (Zombie zombie : zombies) {
            if (!zombie.isDead()) {
                Plant nearestPlant = null;
                for (Plant plant : plants) {
                    if (Math.abs(plant.getY() - zombie.getY()) < 50) {
                        if (nearestPlant == null || plant.getX() > nearestPlant.getX()) {
                            nearestPlant = plant;
                        }
                    }
                }

                if (nearestPlant != null) {
                    ((NormalZombie) zombie).updateState(nearestPlant);
                } else {
                    zombie.setState("walking");
                    zombie.move();
                }
            }
        }
        
        zombies.removeIf(Zombie::isDead);
        plants.removeIf(plant -> plant.isDead());

        for (Zombie zombie : zombies) {
            if (zombie.hasReachedEnd(50)) {
                JOptionPane.showMessageDialog(this, "Game Over! Zombies reached the house!");
                System.exit(0);
            }
        }

        // Sun
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
        //bullet
        for (Bullet bullet : bullets) {
        bullet.move();
        for (Zombie zombie : zombies) {
            if (bullet.getBounds().intersects(new Rectangle(zombie.getX(), zombie.getY(), 60, 100))) {
                zombie.takeDamage(20);
                bullet.deactivate();
                break;
            }
        }
        for (Plant plant: plants)
        {
            plant.update();
            System.out.println("Updating plant at (" + plant.getX() + ", " + plant.getY() + ")");
        }
    }
        bullets.removeIf(b -> !b.isActive());
}
//thêm sun và bullet
    public void addBullet(Bullet b) {
        bullets.add(b);
}
    public List<Sun> getSuns() {
        return suns;
}
    public void addSun(int sunX, int sunY) {
        suns.add(new Sun(sunX, sunY));
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
        int x = e.getX();
        int y = e.getY();

        for (Sun sun : suns) {
            if (sun.isClicked(x, y)) {
                sun.collect();
                sunEnergy += 25;
                repaint();
                return; // Ngừng xử lý sau khi nhặt
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) 
    {
        for (Sun sun : suns) {
        if (sun.isClicked(e.getX(), e.getY())) {
            sun.collect();
            sunEnergy += 25;
            repaint(); // Cập nhật giao diện ngay
        }
    }
    }
    private int getPlantCost(String type) {
    switch (type) {
        case "Peashooter": return 100;
        case "Sunflower": return 50;
        case "Wallnut": return 75;
        default: return 0;
    }
}

    @Override
    public void mouseReleased(MouseEvent e) 
    {
       if (selectedPlantType == null || selectedPlantImage == null) return;

    int x = e.getX();
    int y = e.getY();

    // Tính toán vị trí trên grid
    int col = (x - GRID_START_X) / CELL_WIDTH;
    int row = (y - GRID_START_Y) / CELL_HEIGHT;

    if (col >= 0 && col < NUM_COLS && row >= 0 && row < NUM_ROWS) {
        int plantX = GRID_START_X + col * CELL_WIDTH + (CELL_WIDTH - 64) / 2;
        int plantY = GRID_START_Y + row * CELL_HEIGHT + (CELL_HEIGHT - 90) / 2;

        int cost = getPlantCost(selectedPlantType);
        if (sunEnergy >= cost) {
            Plant newPlant = null;

            switch (selectedPlantType) {
                case "Peashooter":
                    newPlant = new Peashooter(this, plantX, plantY);
                    break;
                case "Sunflower":
                    newPlant = new Sunflower(this, plantX, plantY);
                    break;
                case "Wallnut":
                    newPlant = new Wallnut(this, plantX, plantY);
                    break;
            }

            if (newPlant != null) {
                plants.add(newPlant);
                sunEnergy -= cost;

                // ✅ Reset sau khi trồng nếu bạn muốn
                selectedPlantImage = null;
                selectedPlantType = null;

                repaint();
            }
        }
    }
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