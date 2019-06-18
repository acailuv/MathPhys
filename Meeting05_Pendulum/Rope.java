package Meeting05_Pendulum;

import java.awt.Color;
import java.awt.Graphics;

public class Rope {
	private Color ropeColor;	//the color of the rope
	private double positionX;	//the position of the rope
	private double positionY;
	private Vector vector;		//the rope's vector
	private Ball attachment;	//the object attached at the end of the rope (x2, y2)
	
	public Rope(double x1, double y1, double x2, double y2, Color ropeColor)
	{
		this.positionX = x1;
		this.positionY = y1;
		vector = new Vector(x2-x1, y2-y1);
		this.ropeColor = ropeColor;
	}
	
	public void attach(Ball attachment)
	{
		this.attachment = attachment;
		this.attachment.setIsAttached(true);
		this.attachment.setRope(this);
		this.attachment.setVx(0);
		this.attachment.setVy(0);
		this.attachment.move(getX2(), getY2());
	}
	
	/**
	 * get the object (the ball) attached to this rope
	 * @return a shallow copy of the object (the ball) attached to this rope
	 */
	public Ball getAttachment()
	{
		return attachment;
	}

	public void setX2(double newX)
	{
		this.vector.setX(newX - getX1());
	}
	public void setY2(double newY)
	{
		this.vector.setY(newY - getY1());
	}
	
	/**
	 * get the angle created between the rope and its equilibrium position (the position where the rope is hanging straight down) 
	 * @return the angle between the rope and its equilibrium position
	 */
	public double getAngle()
	{
		return Math.PI/2 - vector.getAngle();
	}
	
	/**
	 * get the length of the rope
	 * @return the length of the rope
	 */	
	public double getLength()
	{
		return vector.getLength();
	}
	
	// function to draw the line on the given graphics 
	public void draw(Graphics g)
	{		
		Color tempColor = g.getColor();
		g.setColor(ropeColor);
		g.drawLine((int) getX1(),(int) getY1(),(int) getX2(),(int) getY2());
		g.setColor(tempColor);
	}
	
	// function to return the rope's x1 (starting point)
	public double getX1()
	{
		return positionX;
	}
	
	// function to return the rope's y1 (starting point)
	public double getY1()
	{
		return positionY;
	}

	// function to return the rope's x2 (end point)
	public double getX2()
	{
		return positionX + vector.getX();
	}
		
	// function to return the rope's x2 (end point)
	public double getY2()
	{
		return positionY + vector.getY();
	}
}