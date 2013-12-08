package restaurant_wc.gui;


import restaurant_wc.HostRoleWc;
import restaurant_wc.RestaurantCustomerRoleWc;

import java.awt.*;

public class HostGui implements Gui {

    private HostRoleWc agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int currentTableX, currentTableY; // position of current table
    
    private int xDestination = -20, yDestination = -20;//default start position

    //public static final int xTable = 200;
    //public static final int yTable = 250;

    public HostGui(HostRoleWc agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(RestaurantCustomerRoleWc customer, int tableX, int tableY) {
        xDestination = tableX + 20;
        yDestination = tableY - 20;
        currentTableX = tableX;
        currentTableY = tableY;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
