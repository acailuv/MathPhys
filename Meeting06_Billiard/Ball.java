package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball {
	private double positionX;              // center of ball's position
	private double positionY;
	public final static double RADIUS = 30;
	private double velocityX = 0;          // ball's velocity
	private double velocityY = 0;
	private final static double e = 1;     // ball's coefficient of resistution
	private final static double a = 0.005; // ball's deceleration/fraction
	private Color ballColor;

	public Ball(double positionX, double positionY, Color ballColor) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.ballColor = ballColor;
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	// drawing function
	public void draw(Graphics g) {
		Color tempColor = g.getColor();
		g.setColor(ballColor);
		g.fillOval((int) (positionX - RADIUS), (int) (positionY - RADIUS), (int) (2 * RADIUS), (int) (2 * RADIUS));
		g.setColor(tempColor);
	}

	// move the ball by modifying current position, with assumption that time = 1
	public void move() {
		positionX += velocityX;
		positionY += velocityY;

		double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

		if (velocity > 0) {
			velocityX -= a;
			velocityY -= a;
		}
	}

	public double distance(Ball other) {
		double distanceX = this.positionX - other.getPositionX();
		double distanceY = this.positionY - other.getPositionY();
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
}
