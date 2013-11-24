package housing.gui;

import housing.Item;

import java.awt.Color;
import java.awt.Graphics2D;

public class ItemGui implements HGui{
	public Item i;
	int x, y, width, height, d, wArc, hArc;
	Color c;
	boolean isBroken = false;
	Shape shape;
	
	enum Shape {Circle, Rectangle, RoundRectangle};

	public ItemGui(Item i, int x, int y, int width, int height, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.c = c;
		shape = Shape.Rectangle;
	}
	
	public ItemGui(Item i, int x, int y, int d, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.d = d;
		this.c = c;
		shape = Shape.Circle;
	}
	
	public ItemGui(Item i, int x, int y, int width, int height, int wArc, int hArc, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.wArc = wArc;
		this.hArc = hArc;
		this.c = c;
		shape = Shape.RoundRectangle;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(c);
		if (shape == Shape.Rectangle)
			g.fill3DRect(x, y, width, height, true);
		else if (shape == Shape.Circle)
			g.fillOval(x, y, d, d);
		else if (shape == Shape.RoundRectangle)
			g.fillRoundRect(x, y, width, height, wArc, hArc);
		if (isBroken()) {
			g.setColor(Color.WHITE);
			if (shape == Shape.Rectangle || shape == Shape.RoundRectangle) {
				g.drawLine(x, y, x + width, y + height);
				g.drawLine(x + width, y, x, y + height);
			}
			else if (shape == Shape.Circle) {
				g.drawLine(x, y, x + d, y + d);
				g.drawLine(x + d, y, x, y + d);
			}
		}
		if (this.i.name.equals("CookingGrill")) {
			g.setColor(Color.BLACK);
			g.drawLine(x, y + (height / 2), x + width, y + (height / 2));
			g.drawLine(x + (width / 2), y, x + (width / 2), y + height);
			g.drawOval(x + 4, y + 7, 10, 10);
			g.drawOval(x + 25, y + 7, 10, 10);
			g.drawOval(x + 4, y + 32, 10, 10);
			g.drawOval(x + 25, y + 32, 10, 10);
		}
		if (this.i.name.equals("Bed")) {
			g.setColor(Color.BLACK);
			g.drawRoundRect(x + 10, y + 15, 20, 30, 10, 15);
		}
		
		if (this.i.name.equals("FussballTable")) {
			g.setColor(Color.BLACK);
			g.drawLine(x + 10, y, x + 10, y + 40);
			g.drawLine(x + 20, y, x + 20, y + 40);
			g.drawLine(x + 30, y, x + 30, y + 40);
			g.drawLine(x + 40, y, x + 40, y + 40);
			g.drawLine(x + 50, y, x + 50, y + 40);
			g.fillRect(x, y + 15, 5, 10);
			g.fillRect(x + 55, y + 15, 5, 10);
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void breakIt() {
		isBroken = true;
	}
	
	public void repair() {
		isBroken = false;
	}
	
	public boolean isBroken() {
		return isBroken;
	}
}