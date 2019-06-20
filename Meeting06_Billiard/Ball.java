package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Ball {
	private double positionX;              // center of ball's position
	private double positionY;
	public final static double RADIUS = 30;
	private double velocityX = 0;          // ball's velocity
	private double velocityY = 0;
	private final static double e = 1;     // ball's coefficient of resistution
	private final static double a = 0.005; // ball's deceleration/fraction
	private int ballNumber;

	private Color ballColor;

	public Ball(double positionX, double positionY, Color ballColor, int ballNumber) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.ballColor = ballColor;
		this.ballNumber = ballNumber;
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

	public int getBallNumber() {
		return ballNumber;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public void setBallNumber(int n) {
		this.ballNumber = n;
	}

	public void setBallColor(Color c) {
		this.ballColor = c;
	}

	// drawing function
	public void draw(Graphics g) {
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(ballColor);
		g.fillOval((int) (positionX - RADIUS), (int) (positionY - RADIUS), (int) (2 * RADIUS), (int) (2 * RADIUS));
		g.setColor(Color.black);
		g.drawOval((int) (positionX - RADIUS), (int) (positionY - RADIUS), (int) (2 * RADIUS), (int) (2 * RADIUS));
		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.BOLD, 16));
		g.drawString(Integer.toString(ballNumber), (int)positionX-2, (int)positionY+8);
		g.setColor(tempColor);
		g.setFont(tempFont);
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
