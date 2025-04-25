import java.awt.CardLayout;
import javax.swing.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GameFrame() {
        setTitle("Plants Vs Zombies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //Set logo icon
        ImageIcon icon = new ImageIcon("image-gif/image/icon.jpg");
        setIconImage(icon.getImage());

        cardLayout = new CardLayout();//Tạo CardLayout
        mainPanel = new JPanel(cardLayout);//Tạo JPanel dụng cardLayout để quản lý các màn hình con

        GamePanel gamePanel = new GamePanel(this);//Tạo gamePanel(menu)
        PlayPanel playPanel = new PlayPanel();//Tạo playPanel(màn chơi)
        //add gamePanel và playPanel vào mainPanel
        mainPanel.add(gamePanel, "Menu");
        mainPanel.add(playPanel, "Play");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void switchToPlayPanel(){
        cardLayout.show(mainPanel, "Play");
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
