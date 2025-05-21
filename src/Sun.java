import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.event.*;
public class Sun 
{
    private int x,y;
    private boolean collected = false;
    private ImageIcon sunIcon;
    private Timer timer;
    

    public Sun(int x, int y) 
    {
        this.x = x;
        this.y = y;
        this.collected = false;
        this.sunIcon = new ImageIcon("image-gif/gif/sun.gif");
         
        
        // Tạo timer, mỗi 100ms sẽ gọi phương thức moving
        timer = new Timer(100, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                moving();  // Cập nhật vị trí mặt trời
            }
        });
        timer.start();  // Bắt đầu timer
    }
    //Spawn 
    public void moving()
    {
        if(!collected)
        {
            if (y < 570) 
            {
                y +=1;
                //System.out.println("Sun position: x=" + x + ", y=" + y);
            }
        }
    }
    // Vẽ mặt trời tại vị trí hiện tại
    public void draw(Graphics g, JComponent component) 
    {
        if (!collected) 
        {
            //System.out.println("Drawing Sun at (" + x + ", " + y + ")");
            sunIcon.paintIcon(component, g, x, y); 
        }
    }

    public boolean isClicked(int mx, int my) 
    {
        return !collected &&
               mx >= x && mx <= x + sunIcon.getIconWidth() + 10&&
               my >= y && my <= y + sunIcon.getIconHeight() + 10;
    }
    //Check đã lượm hay chựa
    public void collect() 
    {
        collected = true;
        timer.stop();
    }

    public boolean isCollected() 
    {
        return collected;
    }

    public int getX() 
    {
        return x;
    }

    public int getY() 
    {
        return y;
    }
}