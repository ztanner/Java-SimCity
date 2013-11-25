package market.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JPanel implements ActionListener {
    
	AnimationPanel animationPanel = new AnimationPanel();

    public MarketGui() {
        int WINDOWX = 550;
        int WINDOWY = 400;
        
        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	setBounds(50, 50, WINDOWX, WINDOWY);
        add(animationPanel);
    	
  
    }
   
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
}
