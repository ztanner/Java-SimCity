package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	//static final int?
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private final int TABLENUM = 3;
    //private final int TABLEX = 200;
    //private final int TABLEY = 250;
    
    
    private final int TABLEWIDTH = 50;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        for (int i=1; i<4; i++){
            g2.fillRect(50+100*(i-1), 100, TABLEWIDTH, TABLEWIDTH);//200 and 250 need to be table params

        }

        g2.setColor(Color.MAGENTA);
        g2.fillRect(50, 250, 60, 20);//cooking
        g2.fillRect(250, 250, 20, 20);//cashier
        
        for (int i=1; i<4; i++) {
        	g2.fillRect(50+45*(i-1), 210, 40, 20);
        }
        
        g2.setColor(Color.BLACK);
        g2.drawString("Grilling", 0, 265);
        g2.drawString("Plating",0,225);
        g2.drawString("Cashier", 250, 280);
        
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
}
