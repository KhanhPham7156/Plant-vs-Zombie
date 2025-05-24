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
    private String plantType;
    private PlayPanel playPanel;


    public plantCard(Image img, String plantType, PlayPanel playPanel)
    {
        setSize(64,90);
        this.img = img;
        this.plantType = plantType;
        this.playPanel = playPanel;
        addMouseListener(this);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
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
        if (playPanel.getSelectedPlantType() != null && playPanel.getSelectedPlantType().equals(plantType)) {
            // Đã chọn thẻ này rồi → nhấn lại để hủy
            playPanel.clearSelectedPlant();
        } else {
            // Chọn mới
            playPanel.setSelectedPlant(plantType, img);
        }
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
