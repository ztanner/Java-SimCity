package restaurant_vk.gui;

import restaurant_vk.VkHostRole;
import java.awt.*;

/*
 * Don't look at this thing. I just left it as it was in V1.
 */
public class HostGui implements Gui {

    public VkHostRole host = null;
    private int jobPosX = 20;
    private int jobPosY = 130;
    private int entranceX = 20;
    private int entranceY = -20;
    private int xPos = entranceX, yPos = entranceY;
    private int xDestination = entranceX, yDestination = entranceY;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private final int HOST_WIDTH = 20;
    private final int HOST_HEIGHT = 20;
	private State state = State.None;
	
	enum State {None, Entering, Exiting, OnDuty};

    public HostGui(VkHostRole agent) {
        this.host = agent;
    }

    public void updatePosition() {
    	if (xPos < xDestination && Math.abs(xDestination - xPos) > 1)
			xPos += 2;
		else if (xPos > xDestination && Math.abs(xDestination - xPos) > 1)
			xPos -= 2;
		
		if (yPos < yDestination && Math.abs(yDestination - yPos) > 1)
			yPos += 2;
		else if (yPos > yDestination && Math.abs(yDestination - yPos) > 1)
			yPos -= 2;

        if (Math.abs(xPos - xDestination) < 2 && Math.abs(yPos - yDestination) < 2) {
        	xPos = xDestination;
			yPos = yDestination;
			
        	if (state == State.Entering) {
				state = State.OnDuty;
				host.activityDone();
			}
			else if (state == State.Exiting) {
				state = State.None;
				host.activityDone();
			}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HOST_WIDTH, HOST_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString("H", xPos + 4, yPos + 14);
    }

    public boolean isPresent() {
    	if (state != State.None)
    		return true;
    	return false;
    }
    
    public void DoEnterRestaurant() {
    	xDestination = jobPosX;
		yDestination = jobPosY;
		state = State.Entering;
    }
    
    public void DoLeaveRestaurant() {
    	xDestination = entranceX;
		yDestination = entranceY;
		state = State.Exiting;
    }
}
