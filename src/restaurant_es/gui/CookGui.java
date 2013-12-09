package restaurant_es.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import restaurant_es.CookRole;


public class CookGui implements Gui {

	private CookRole role = null;
	Boolean isCooking;
	boolean goingBack = false;
	boolean leavingWork = false;
	boolean goingToFridge = false;
	String foodBeingCooked = null;
	RestaurantGui gui;
	private List<String> foodPlated = new ArrayList<String>();

	 
    private int xDestination = 70, 
    		xPos = 0;
    private int yDestination = 270,
    		yPos = 0;
    
    private int cookX = 70;
    private int cookY = 270;
    
    private int revolvingStandX = 350, revolvingStandY = 250;
    
    private int exitX = 0, exitY = 0;
    private int fridgeX = 150, fridgeY = 300;
    
    boolean isPresent;
	
	
	public CookGui (CookRole cook) {
		isCooking = false;
		role = cook;
		for (int i=0;i<3;i++) {
			foodPlated.add("");
		}
	}
	

	@Override
	public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == revolvingStandX) && (yDestination == revolvingStandY)) {
        	xDestination = cookX;
        	yDestination = cookY;
           role.msgAtRevolvingStand();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == cookX) && (yDestination == cookY) && goingBack) {
        	goingBack = false;
        	role.msgAtGrill();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == fridgeX) && (yDestination == fridgeY) && goingToFridge) {
        	goingToFridge = false;
        	role.msgAtFridge();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == exitX) && (yDestination == exitY) && leavingWork) {
        	leavingWork = false;
        	role.msgAtExit();
        }
        }

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.blue);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.black);
        g.drawString("Cook", xPos, yPos+20);
        if (isCooking) {
        	g.drawString(foodBeingCooked, 50, 265);
        }
        else g.drawString("", 50, 265);
        for (int i=0; i<3;i++) {
        	g.drawString(foodPlated.get(i), 50+45*i, 225);
        }
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	public void cookFood (String food) {
		isCooking = true;
		foodBeingCooked = food;
	}
	
	public void plateFood (String food, int i){
		foodPlated.set(i-1, food);	
	}
	
	public void foodPickedUp(int table) {
		foodPlated.set((table-1),"");
	}
	
	public void finishCooking () {
		isCooking = false;
	}

	public void DoGoToRevolvingStand() {
		xDestination = revolvingStandX;
		yDestination = revolvingStandY;
		
	}
	
	public void setPresent(boolean b) {
		isPresent = b;
	}

	public void DoGoToCookingPlace() {
		goingBack = true;
		xDestination = cookX;
		yDestination = cookY;
		// TODO Auto-generated method stub
		
	}


	public void DoLeaveWork() {
		leavingWork = true;
		xDestination = exitX;
		yDestination = exitY;
		
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getXDest() {
		return xDestination;
	}
	
	public int getYDest() {
		return yDestination;
	}
	
	public void setXDest(int x) {
		xDestination = x;
	}
	
	public void setYDest(int y) {
		yDestination = y;
	}


	public void setDefaultDestination() {
		// TODO Auto-generated method stub
		goingBack = true;
		xDestination = cookX;
		yDestination = cookY;
	}


	public void goToFridge() {
		// TODO Auto-generated method stub
		xDestination = fridgeX;
		yDestination = fridgeY;
		goingToFridge = true;
	}
}
