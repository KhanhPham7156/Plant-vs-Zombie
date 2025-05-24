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
    private boolean sunJustCollected = false;
    private long deathTime;

    // Kích thước grid để đặt cây
    public static final int GRID_COLS = 9;
    public static final int GRID_ROWS = 5;
    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 120;
    public static final int GRID_START_X = 50;
    public static final int GRID_START_Y = 90;
    
    public PlayPanel() 
    {
        setPreferredSize(new Dimension(1000, 752));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        setLayout(null);

        suns = new ArrayList<>();
        zombies = new ArrayList<>();
        timer = new Timer(30, this);
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

            addPlantCard(pea, 110,"Peashooter");
            addPlantCard(sun, 190, "Sunflower");
            addPlantCard(wall, 270,"Wallnut");
        } catch (Exception e) {
            System.out.println("Loading: " + e.getMessage());
        }
    }
    //thêm card
    private void addPlantCard(Image img, int x,String type) {
        plantCard card = new plantCard(img,type,this);
        card.setBounds(x, 10, 64, 90);
        plantCards.add(card);
        add(card);
    }
     // Getter & Setter cho lựa chọn
    public String getSelectedPlantType() {
        return selectedPlantType;
    }

    public void setSelectedPlant(String type, Image img) {
        selectedPlantType = type;
        selectedPlantImage = img;
        repaint();
    }

    public void clearSelectedPlant() {
        selectedPlantType = null;
        selectedPlantImage = null;
        repaint();
    }
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1000, 752, null);
        //g.drawImage(plantHolder, 0, 0, 520, 100, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(""+sunEnergy, 40, 100);

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
            g.drawImage(selectedPlantImage, mousePos.x , mousePos.y, 64, 72, null);
        }
        // vẽ đạn
        for (Bullet bullet : bullets) {
            bullet.draw(g, this);
        }
    }
    
    private void updateGame() {
        // Zombie
        if (System.currentTimeMillis() - lastZombieSpawnTime > 5000) {
            int [] rows = {90, 210, 330, 450,570}; // Điều chỉnh vị trí Y để khớp với vị trí plant
            int row = rows[(int) (Math.random() * rows.length)];
            zombies.add(new NormalZombie(800, row));
            lastZombieSpawnTime = System.currentTimeMillis();
        }

        for (Zombie zombie : zombies) {
            if (!zombie.isDead()) {
                Plant nearestPlant = null;
                double minDistance = Double.MAX_VALUE;
                
                for (Plant plant : plants) {
                    // Kiểm tra xem zombie và plant có ở cùng hàng không
                    int yDiff = Math.abs(zombie.getY() - plant.getY());             
                    // Sử dụng khoảng cách cố định cho tất cả các hàng
                    if (yDiff <= 50) { // Cho phép sai số 50 pixels theo trục Y
                        double distance = zombie.getX() - plant.getX();
                        if (distance > 0 && distance <= 100) { // Nếu zombie cách plant không quá 100 pixels
                            if (distance < minDistance) {
                                minDistance = distance;
                                nearestPlant = plant;
                            }
                        }
                    }
                }

                if (nearestPlant != null) {
                    ((NormalZombie) zombie).updateState(nearestPlant);
                } else {
                    if (!zombie.getState().equals("walking")) {
                        zombie.setState("walking");
                    }
                    if (zombie.getHealth() == 0) {
                        zombie.setState("dead");
                    }
                    zombie.move();
                }
            }
        }
        
        zombies.removeIf(Zombie::isReadyToRemove);
        plants.removeIf(plant -> plant.isDead());

        for (Zombie zombie : zombies) {
            if (zombie.hasReachedEnd(10)) {
                JOptionPane.showMessageDialog(this, "Game Over! Zombies reached the house!");
                System.exit(0);
            }
        }

        // Sun
        if (System.currentTimeMillis() - lastSpawnTime > 5000) {
            suns.add(new Sun((int)(Math.random() * 600), 0));
            lastSpawnTime = System.currentTimeMillis();
        }

        for (Sun sun : suns) {
            sun.moving();
        }

        suns.removeIf(Sun::isCollected);
        
        // Bullet collision detection
        for (Bullet bullet : bullets) {
            bullet.move();
            for (Zombie zombie : zombies) {
                if (bullet.getCollisionBox().intersects(zombie.getCollisionBox())) {
                    zombie.takeDamage(20);
                    bullet.deactivate();
                    break;
                }
            }
        }
        
        for (Plant plant : plants) {
            plant.update();
        }
        
        bullets.removeIf(b -> !b.isActive());
        repaint();
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
        sunJustCollected = false;
        for (Sun sun : suns) {
        if (sun.isClicked(e.getX(), e.getY())) {
            sun.collect();
            sunEnergy += 25;
            sunJustCollected = true;
            repaint(); // Cập nhật giao diện ngay
            return;
        }
    }
    }
    private int getPlantCost(String type) {
    switch (type) {
        case "Peashooter": return 100;
        case "Sunflower": return 50;
        case "Wallnut": return 50;
        default: return 0;
    }
}

    @Override
    public void mouseReleased(MouseEvent e) 
    {
       if (sunJustCollected||selectedPlantType == null || selectedPlantImage == null) return;

    int x = e.getX();
    int y = e.getY();

    // Tính toán vị trí trên grid
    int col = (x - GRID_START_X) / CELL_WIDTH;
    int row = (y - GRID_START_Y) / CELL_HEIGHT;

    if (col >= 0 && col < 9 && row >= 0 && row < 5) {
        int plantX = GRID_START_X + col * CELL_WIDTH ;
        int plantY = GRID_START_Y + row * CELL_HEIGHT + 40;


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

                // Reset sau khi trồng nếu bạn muốn
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