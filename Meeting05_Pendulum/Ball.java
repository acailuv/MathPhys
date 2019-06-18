package Meeting05_Pendulum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball {
	// (positionX,positionY) the position of the ball in screen coordinate
	private double positionX;
	private double positionY;

	// The radius of the ball in screen coordinate 
	private double radius;

	// (vx,vy) The velocity of the ball on screen coordinate */
	private Vector velocity;

	// The ball's color and mass 
	private Color ballColor;
	private double mass;
	
	// The ball's deceleration or fraction
	private final static double DECELERATION = 0.0005;

	// Elasticity coefficient for collision between balls
	private final static double e = 1.0;
	
	// The ball's state whether it is attached to a rope or not
	private boolean isAttached;
	
	// The rope on whom the ball is attached to
	private Rope rope;
	
	public Ball(double positionX, double positionY, double radius, double vx, double vy, Color ballColor, double mass)
	{
		this.radius = radius;
		this.positionX = positionX;
		this.positionY = positionY;
		this.mass = mass;
		this.velocity = new Vector(vx, vy);
		this.ballColor = ballColor;
		this.isAttached = false;
	}
	
	// funtion to draw the ball on the given graphics 
	public void draw(Graphics g)
	{		
		Color tempColor = g.getColor();
		g.setColor(ballColor);
		g.fillOval((int)((positionX - radius)), (int)((positionY - radius)), (int)(radius * 2), (int)(radius * 2));
		g.setColor(tempColor);
	}
	
	public void move(double x, double y)
	{
		this.positionX = x;
		this.positionY = y;
		if(isAttached)
		{
			rope.setX2(this.positionX);
			rope.setY2(this.positionY);
		}
	}
	
	// function to move the ball's position in screen coordinate based on its velocity while considering fraction in the movement
	public void move()
	{		
		//if the ball is not attached to a rope, move on its own
		if(!isAttached)
		{
			//move the ball first
			positionX += velocity.getX();
			positionY += velocity.getY();

			//reduce the speed of the ball 
			if(velocity.getLength() > 0)
			{
				velocity.multiply((velocity.getLength() - DECELERATION)/(velocity.getLength()));
			}	
		}
		//else, the movement depends on the rope
		else
		{	
			//calculate new positionX and new positionY
			double theta = rope.getAngle();						
			this.velocity.add(Pendulum.GRAVITY *Math.sin(theta)*Math.cos(theta), 0);
			double newX = positionX - this.velocity.getX();
			double newY = rope.getY1() + Math.sqrt(rope.getLength()*rope.getLength() - (newX-rope.getX1())*(newX-rope.getX1()));;
			
			positionX = newX;
			positionY = newY;
			
			rope.setX2(positionX);
			rope.setY2(positionY);
		}
			
	}

	/**
	 * get the state of the ball, that is whether it's attached to a rope or not
	 * @return true if this ball is attached to a rope, false otherwise
	 */
	public boolean isAttached()
	{
		return isAttached;
	}
	
	/**
	 * set the isAttached attribute of this ball
	 * @param isAttached a boolean value which is true if this ball is currently being attached, false otherwise
	 */
	public void setIsAttached(boolean isAttached)
	{
		this.isAttached = isAttached;
	}
	
	/**
	 * set the rope on which this ball is attached to
	 * @param rope the rope on which this ball is attached to
	 */
	public void setRope(Rope rope)
	{
		this.rope = rope;
	}
	
	public Rope getRope()
	{
		return this.rope;
	}
	
	
	/**
	 * check whether the given point (positionX,positionY) is inside this ball
	 * @param x the positionX-coordinate of the point
	 * @param y the positionY-coordinate of the point
	 * @return true if the given point is inside this ball, false otherwise
	 */
	public boolean isInside(double x, double y)
	{
		if((x-this.positionX)*(x-this.positionX) + (y-this.positionY)*(y-this.positionY) <= radius * radius)
			return true;
		return false;
	}
	
	// function to return the ball's radius
	public double getRadius()
	{
		return radius;
	}
	
	// function to return the real positionY position of the circle
	public double getPositionY()
	{
		return positionY;
	}
		
	// function to return the real positionX position of the circle
	public double getPositionX()
	{
		return positionX;
	}
	
	// function to set the velocity of the circle in positionY-axis
	public void setVy(double vy)
	{
		this.velocity.y = vy;
	}

	// function to set the velocity of the circle in positionX-axis
	public void setVx(double vx)
	{
		this.velocity.x = vx;
	}	

	public boolean equals(Object otherObj)
	{
		if(otherObj == null)
			return false;
		else if(!(otherObj instanceof Ball))
			return false;
		else
		{
			Ball otherBall = (Ball)otherObj;
			return (getPositionX() == otherBall.getPositionX() &&
					getPositionY() == otherBall.getPositionY() &&
					getRadius() == otherBall.getRadius());
		}
				
			
	}

	public void ballCollide(ArrayList<Ball> balls) {
        for (Ball b: balls) {
            if (b != this && this.distance(b) <= this.radius + b.getRadius()) {
				double v1;
				double v2;

				v1 = (0.5*(1-e)*this.velocity.getX());
				v2 = (0.5*(1+e)*this.velocity.getX());
				
				this.velocity.setX(v1);
				b.velocity.setX(v2);
            }
        }
    }
	
	public double distance(Ball other) {
        double distanceX = this.positionX - other.getPositionX();
        double distanceY = this.positionY - other.getPositionY();
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}