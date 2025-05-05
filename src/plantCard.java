import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class plantCard extends JPanel implements MouseListener{
    private Image img;
    private ActionListener al;

    public plantCard(Image img)
    {
        setSize(64,90);
        this.img = img;
        addMouseListener(this);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
    public void setActionListener(ActionListener al) {
        this.al = al;
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {}
    @Override
    public void mousePressed(MouseEvent e)
    {}
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(al != null)
        {
            al.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {}

    @Override
    public void mouseExited(MouseEvent e)
    {}
}
