import javax.swing.*; 
import javax.swing.event.*; 
import java.awt.*; 
import java.awt.event.*; 


public class GamePanel extends JPanel implements ActionListener 
{
    private Image background;
    private JButton startButton;
    private GameFrame gameFrame;

    public GamePanel(GameFrame gameFrame) 
    {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(1000, 752));
        setBackground(Color.WHITE);
        background = new ImageIcon("image-gif/image/menuBackground.png").getImage();
        // Create start button
        startButton = new JButton("Start Game");
        startButton.setBounds(400, 300, 200, 60);
        startButton.setIcon(new ImageIcon("image-gif/image/startButton.png"));
        startButton.addActionListener(this);
        
        setLayout(null); 
        add(startButton);        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1000, 752, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        // Handle start button click events here
        if (e.getSource() == startButton) {
            gameFrame.switchToPlayPanel();
        }
    }

}