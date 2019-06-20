package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Graphics;

public class Wall {
	private double x1, y1, x2, y2;
	
	/**
	 * 
	 * @param x1 the x position of the first point in the cartesian coordinate
	 * @param y1 the y position of the first point in the cartesian coordinate
	 * @param x2 the x position of the second point in the cartesian coordinate
	 * @param y2 the y position of the second point in the cartesian coordinate
	 */
	public Wall(double x1, double  y1, double  x2, double  y2)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public void draw(Graphics g)
	{
		Color tempColor = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		g.setColor(tempColor);
	}

	/**
	 * function to calculate the normal line of the wall in unit vector
	 * the direction of the normal line depends on the x1, x2, y1, and y2
	 * to change its direction, times the resultant vector with -1
	 * @return a point representing a vector with its x and y component
	 */
	public Vector normalLine()
	{
		//normal vector to be returned
		Vector normVector = new Vector();
		double normVectorX, normVectorY;
		//wall vector
		Vector wallVector = new Vector();
		wallVector.setX(x2-x1);
		wallVector.setY(y2-y1);
		
		//calculate the normal vector, then calculate the unit vector
		normVectorY = 1;
		normVectorX = (-1)*(wallVector.getY()/wallVector.getX());
		
		//System.out.println(normVector.getX() + " - " + normVector.getY());	
		double normVectorLength = Math.sqrt(normVectorX*normVectorX + normVectorY*normVectorY);		
		normVector.setX(normVectorX/normVectorLength);
		normVector.setY(normVectorY/normVectorLength);
		return normVector;
	}
	
	/**
	 * function to calculate the minimal distance between this wall and a point		 
	 * Calculate the distance using this formula:
	 * 		|(a*xPoint) + (b*yPoint) + (b*y1 - a*x1)| / (sqrt(a^2 + b^2))
	 * @param xPoint: the x coordinate of the point whose distance want to be calculated
	 * @param yPoint: the y coordinate of the point whose distance want to be calculated
	 * @return the distance between the point and the line
	 */
	public double distanceFromPoint(double xPoint, double yPoint)
	{
		double distance, b, a;
		b = (x2-x1);
		a = (y2-y1);
		distance = Math.abs(a*xPoint + (-b)*yPoint + (b*y1 - a*x1));
		distance = distance / Math.sqrt(a*a + b*b);
		return distance;		
	}

	/**
	 * function to calculate the point in the line which is the nearest from the given point
	 * @param xPoint the x coordinate of the point given
	 * @param yPoint the y coordinate of the point given
	 * @return a point in the line which is the nearest from the (xPoint,yPoint)
	 */
	public Vector nearestPoint(double xPoint, double yPoint)
	{
		//point to be returned
		Vector nearestPoint = new Vector();
		//the x and y coordinate in the line which is nearest from the point given 
		double nearestX, nearestY;
		
		//calculating the nearest x and y coordinate
		double a = y2-y1, b = x2-x1;
		nearestX = (-b*(-b*xPoint - a*yPoint) - a*(b*y1 - a*x1));
		nearestX = nearestX/(a*a + b*b);
		nearestY = (a*(b*xPoint + a*yPoint) - (-b)*(b*y1 - a*x1));
		nearestY = nearestY/(a*a + b*b);
		
		//return the point after setting it up
		nearestPoint.setX(nearestX);
		nearestPoint.setY(nearestY);
		return nearestPoint;
	}
	
	public double getHeight()
	{
		return y2-y1;
	}
	
	public double getWidth()
	{
		return x2-x1;
	}
}

