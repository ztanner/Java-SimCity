package restaurant_vk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import restaurant_vk.CookAgent;

public class CookGui implements Gui{
	private int xPos;
	private int yPos;
	private int homePosX;
	private int homePosY;
	private int xDestination;
	private int yDestination;
	private CookAgent agent = null;
	private final int cookWidth = 20;
	private final int cookHeight = 20;
	private Map<String, String> symbols = new HashMap<String, String>();
	private String symbol = "";
	private String caption = "";
	private Dimension fridge = new Dimension(220, 60);
	private Dimension plate = new Dimension(140, 90);
	private Dimension grill = new Dimension(140,40);
	private MyState state = MyState.Inactive;
	
	public CookGui(CookAgent c) {
		agent = c;
		homePosX = xPos = xDestination = 140;
		homePosY = yPos = yDestination = 90;
		
		setSymbols();
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
        
        if (xPos == xDestination && yPos == yDestination) {
        	if (state == MyState.Cooking) {
        		if (xDestination == fridge.width && yDestination == fridge.height) {
        			xDestination = grill.width;
        			yDestination = grill.height;
        			caption = symbol;
        		}
        		else {
        			caption = "";
        			state = MyState.Inactive;
        			agent.atDestination();
        		}
        	}
        	else if (state == MyState.Plating) {
        		if (xDestination == grill.width && yDestination == grill.height) {
        			xDestination = plate.width;
        			yDestination = plate.height;
        			caption = symbol;
        		}
        		else {
        			state = MyState.Inactive;
        			caption = "";
        			agent.atDestination();
        		}
        	}
        }
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, cookWidth, cookHeight);
		g.setColor(Color.WHITE);
		g.drawString("CO", xPos + 1, yPos + 15);
		g.setColor(Color.BLACK);
		int carryYPos = 0;
		if (state == MyState.Cooking)
			carryYPos = yPos;
		else if (state == MyState.Plating)
			carryYPos = yPos + 32;
		g.drawString(caption, xPos + 1, carryYPos);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void DoCookIt(String choice) {
		symbol = symbols.get(choice);
		xDestination = fridge.width;
		yDestination = fridge.height;
		state = MyState.Cooking;
	}
	
	public void DoPlateIt(String choice) {
		symbol = symbols.get(choice);
		xDestination = grill.width;
		yDestination = grill.height;
		state = MyState.Plating;
	}
	
	/*
	 * Sets up symbols inside the map.
	 */
	private void setSymbols() {
		symbols.put("Steak", "ST");
		symbols.put("Chicken", "CH");
		symbols.put("Salad", "SA");
		symbols.put("Pizza", "PI");
		symbols.put("", "");
	}
	
	enum MyState {Inactive, Cooking, Plating};
}