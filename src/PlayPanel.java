import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PlayPanel extends JPanel implements ActionListener, MouseListener {
    private Image background;
    private Image plantHolder;
    private Timer timer;
    private ArrayList<Sun> suns;
    private ArrayList<Zombie> zombies;
    private int sunEnergy = 100;
    private long lastSpawnTime = 0;
    private long lastZombieSpawnTime = 0;
    private List<Plant> plants = new ArrayList<>();
    private List<plantCard> plantCards;
    private Image selectedPlantImage;
    private String selectedPlantType;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private Point mousePos;
    private boolean sunJustCollected = false;

    // Countdown variables
    private long countdownDuration = 20000; // 20 seconds in milliseconds
    private long countdownStartTime;
    private boolean isCountdownActive = true;

    // Zombie spawn control
    private int zombieSpawnInterval = 10000; // Start with 10 seconds
    private int minZombieSpawnInterval = 1500; // Minimum 1.5 seconds
    private int zombieSpawnDecreaseRate = 200; // Decrease by 200ms
    private int zombieSpawnDecreaseInterval = 20000; // Decrease every 20 seconds
    private long lastZombieSpawnDecreaseTime = 0;

    // Shovel variables
    private boolean isShovelSelected = false;
    private Image shovelImage;
    // Grid constants
    public static final int GRID_COLS = 9;
    public static final int GRID_ROWS = 5;
    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 120;
    public static final int GRID_START_X = 50;
    public static final int GRID_START_Y = 90;

    public PlayPanel() {
        setPreferredSize(new Dimension(1000, 752));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/map1.png").getImage();
        plantHolder = new ImageIcon("image-gif/image/plantHolder.png").getImage();
        setLayout(null);

        suns = new ArrayList<>();
        zombies = new ArrayList<>();
        timer = new Timer(30, this);
        countdownStartTime = System.currentTimeMillis(); // Start countdown
        timer.start();

        plantCards = new ArrayList<>();
        loadPlantCards();

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
        addMouseListener(this);
    }

    public void loadPlantCards() {
        try {
            Image pea = new ImageIcon("image-gif/image/card_peashooter.png").getImage();
            Image sun = new ImageIcon("image-gif/image/card_sunflower.png").getImage();
            Image wall = new ImageIcon("image-gif/image/card_nut .png").getImage();
            shovelImage = new ImageIcon("image-gif/image/card_shovel .png").getImage(); // Assume a shovel image exists

            addPlantCard(pea, 110, "Peashooter");
            addPlantCard(sun, 190, "Sunflower");
            addPlantCard(wall, 270, "Wallnut");
            addPlantCard(shovelImage, 700, "Shovel"); // Add shovel card
        } catch (Exception e) {
            System.out.println("Loading: " + e.getMessage());
        }
    }

    private void addPlantCard(Image img, int x, String type) {
        plantCard card = new plantCard(img, type, this);
        
        if(type.equals("Shovel"))
        {
            card.setBounds(x,10,50,50);
            plantCards.add(card);
            add(card);
        }
        else{
            card.setBounds(x, 10, 64, 90);
            plantCards.add(card);
            add(card);
        }
    }

    public String getSelectedPlantType() {
        return selectedPlantType;
    }

    public void setSelectedPlant(String type, Image img) {
        if (type.equals("Shovel")) {
            isShovelSelected = true;
            selectedPlantType = null;
            selectedPlantImage = null;
        } else {
            isShovelSelected = false;
            selectedPlantType = type;
            selectedPlantImage = img;
        }
        repaint();
    }

    public void clearSelectedPlant() {
        selectedPlantType = null;
        selectedPlantImage = null;
        isShovelSelected = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1000, 752, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("" + sunEnergy, 40, 100);

        // Display countdown
        if (isCountdownActive) {
            long timeElapsed = System.currentTimeMillis() - countdownStartTime;
            long timeRemaining = Math.max(0, countdownDuration - timeElapsed);
            int secondsRemaining = (int) (timeRemaining / 1000);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Zombies incoming in: " + secondsRemaining + "s", 350, 350);
        }

        for (Sun sun : suns) {
            sun.draw(g, this);
        }
        for (Zombie zombie : zombies) {
            zombie.draw(g, this);
        }
        for (Plant plant : plants) {
            plant.paint(g);
        }
        if (selectedPlantImage != null && mousePos != null) {
            g.drawImage(selectedPlantImage, mousePos.x, mousePos.y, 64, 72, null);
        } else if (isShovelSelected && mousePos != null) {
            g.drawImage(shovelImage, mousePos.x, mousePos.y, 64, 72, null); // Show shovel cursor
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g, this);
        }
    }

    private void updateGame() {
        long currentTime = System.currentTimeMillis();

        // Check countdown
        if (isCountdownActive) {
            if (currentTime - countdownStartTime >= countdownDuration) {
                isCountdownActive = false; // Countdown finished, allow zombie spawning
            }
        }

        // Zombie spawn logic
        if (!isCountdownActive) {
            if (currentTime - lastZombieSpawnDecreaseTime > zombieSpawnDecreaseInterval) {
                zombieSpawnInterval = Math.max(minZombieSpawnInterval, 
                                               zombieSpawnInterval - zombieSpawnDecreaseRate);
                lastZombieSpawnDecreaseTime = currentTime;
                System.out.println("Zombie spawn interval decreased to: " + zombieSpawnInterval + "ms");
            }

            if (currentTime - lastZombieSpawnTime > zombieSpawnInterval) {
                int[] rows = {90, 210, 330, 450, 570};
                int row = rows[(int) (Math.random() * rows.length)];
                zombies.add(new NormalZombie(800, row));
                lastZombieSpawnTime = currentTime;
            }
        }

        // Update zombies
        for (Zombie zombie : zombies) {
            if (!zombie.isDead()) {
                Plant nearestPlant = null;
                for (Plant plant : plants) {
                    int yDiff = Math.abs(zombie.getY() - (plant.getY() - 40));
                    if (yDiff <= CELL_HEIGHT / 2) {
                        if (zombie.getCollisionBox().intersects(plant.getCollisionBox())) {
                            nearestPlant = plant;
                            break;
                        }
                    }
                }

                if (nearestPlant != null) {
                    ((NormalZombie) zombie).updateState(nearestPlant);
                } else {
                    if (!zombie.getState().equals("walking")) {
                        zombie.setState("walking");
                    }
                    zombie.move();
                }

                if (zombie.getHealth() <= 0 && !zombie.getState().equals("dead")) {
                    zombie.setState("dead");
                }
            }
        }

        zombies.removeIf(Zombie::isReadyToRemove);
        plants.removeIf(Plant::isDead);

        for (Zombie zombie : zombies) {
            if (zombie.hasReachedEnd(10)) {
                JOptionPane.showMessageDialog(this, "Game Over! Zombies reached the house!");
                System.exit(0);
            }
        }

        if (currentTime - lastSpawnTime > 10000) {
            suns.add(new Sun((int)(Math.random() * 600), 0));
            lastSpawnTime = currentTime;
        }

        for (Sun sun : suns) {
            sun.moving();
        }

        suns.removeIf(Sun::isCollected);

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

    public void addBullet(Bullet b) {
        bullets.add(b);
    }

    public List<Sun> getSuns() {
        return suns;
    }

    public void addSun(int sunX, int sunY) {
        suns.add(new Sun(sunX, sunY));
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
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (Sun sun : suns) {
            if (sun.isClicked(x, y)) {
                sun.collect();
                sunEnergy += 25;
                repaint();
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        sunJustCollected = false;
        for (Sun sun : suns) {
            if (sun.isClicked(e.getX(), e.getY())) {
                sun.collect();
                sunEnergy += 25;
                sunJustCollected = true;
                repaint();
                return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (sunJustCollected) return;

        int x = e.getX();
        int y = e.getY();

        // Calculate grid position
        int col = (x - GRID_START_X) / CELL_WIDTH;
        int row = (y - GRID_START_Y) / CELL_HEIGHT;

        if (col >= 0 && col < GRID_COLS && row >= 0 && row < GRID_ROWS) {
            int plantX = GRID_START_X + col * CELL_WIDTH;
            int plantY = GRID_START_Y + row * CELL_HEIGHT + 40; // Keep +40 offset

            if (isShovelSelected) {
                // Remove plant at the clicked grid cell
                Plant plantToRemove = null;
                for (Plant plant : plants) {
                    if (plant.getX() == plantX && plant.getY() == plantY) {
                        plantToRemove = plant;
                        break;
                    }
                }

                if (plantToRemove != null) {
                    plants.remove(plantToRemove);
                    isShovelSelected = false; // Clear shovel selection
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "No plant to remove in this cell!");
                }
                return;
            }

            if (selectedPlantType == null || selectedPlantImage == null) return;

            // Check if a plant already exists at this grid cell
            boolean cellOccupied = false;
            for (Plant plant : plants) {
                if (plant.getX() == plantX && plant.getY() == plantY) {
                    cellOccupied = true;
                    break;
                }
            }

            if (cellOccupied) {
                JOptionPane.showMessageDialog(this, "A plant is already placed in this cell!");
                return;
            }

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
                    selectedPlantImage = null;
                    selectedPlantType = null;
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}