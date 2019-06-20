package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Font;
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
	public final static double MASS = 200.0;
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

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
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
		if(ballNumber > 0) {
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, 16));
			g.drawString(Integer.toString(ballNumber), (int)positionX-2, (int)positionY+8);
		}
		g.setColor(tempColor);
		g.setFont(tempFont);
	}

	// move the ball by modifying current position, with assumption that time = 1
	public void move() {
		positionX += velocityX;
		positionY += velocityY;

		double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		if (this == Billiard.balls.get(0)) {
			// System.out.println("X Vel: " + Double.toString(velocityX) + " || "+ "Y Vel: " + Double. toString(velocityY));
			// System.out.println("Vel: " + Double.toString(velocity));
		}

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

	public void ballCollide(ArrayList<Ball> balls) {
        for (Ball b: balls) {
            if (b != this && this.distance(b) <= 2*Ball.RADIUS) {
                //calculate the unit centre vector, that is the vector between the centre of the two balls colliding
                double CVectorX = b.getPositionX() - this.positionX;
                double CVectorY = b.getPositionY() - this.positionY;
                double CVectorLength = Math.sqrt(CVectorX*CVectorX + CVectorY*CVectorY);
                CVectorX = CVectorX/CVectorLength;
                CVectorY = CVectorY/CVectorLength;

                //calculate the normal vector of the centre vector (by rotating 90 degrees)
                double NVectorX = -CVectorY;
                double NVectorY =  CVectorX;

                //projecting the ball's velocity to the centre and normal vector of the two centre balls
                double V1x  = CVectorX * this.velocityX + CVectorY * this.velocityY;
                double V1y  = NVectorX * this.velocityX + NVectorY * this.velocityY;

                double V2x  = CVectorX * b.getVelocityX() + CVectorY * b.getVelocityY();
                double V2y = NVectorX * b.getVelocityX() + NVectorY * b.getVelocityY();

                //calculate the new velocity after collision
                double NewV1x = ((Ball.MASS * V1x) + (Ball.MASS * V2x) + Ball.MASS * e *(V2x - V1x))/(2*Ball.MASS);
                double NewV1y = V1y ;

                double NewV2x = ((Ball.MASS * V1x) + (Ball.MASS * V2x) - Ball.MASS * e * (V2x - V1x))/(2*Ball.MASS);
                double NewV2y = V2y ;

                //set the new velocity for the two balls colliding

                this.velocityX = NewV1x * CVectorX + NewV1y * NVectorX;
                this.velocityY = NewV1x * CVectorY + NewV1y * NVectorY;

                b.setVelocityX(NewV2x * CVectorX + NewV2y * NVectorX);
                b.setVelocityY(NewV2x * CVectorY + NewV2y * NVectorY);

                // while(this.distance(b) < (2*Ball.RADIUS))
                // {
                //     this.positionX += velocityX;
                //     this.positionY -= velocityY;
                //     b.setPositionX(b.getPositionX() + b.getVelocityX());
                //     b.setPositionY(b.getPositionY() - b.getVelocityY());
				// }
				
				// if (this.distance(b) < 2*Ball.RADIUS) {
				// 	double arc = Math.atan(this.velocityY/this.velocityX);
				// 	double intersectValue = 2*Ball.RADIUS - this.distance(b);

				// 	double offsetX = intersectValue * Math.cos(arc);
				// 	double offsetY = intersectValue * Math.sin(arc);

				// 	this.positionX += offsetX;
				// 	this.positionY -= offsetY;
				// 	b.setPositionX(b.getPositionX() - offsetX);
				// 	b.setPositionY(b.getPositionY() + offsetY);
				// }
            }
		}
	}

	public void wallCollide(ArrayList<Wall> walls) {
        for (Wall w : walls) {
            if(w.distanceFromPoint(positionX, positionY) <= Ball.RADIUS) {
                double error = Ball.RADIUS - w.distanceFromPoint(positionX, positionY);
				positionX += error * w.normalLine().getX();
				positionY -= error * w.normalLine().getY();

                //if the ball collided with a vertical wall
                if(w.getWidth() == 0) {
                    velocityX *= -1;
                }

                //if the ball collided with a horizontal wall
                else if(w.getHeight() == 0) {
					velocityY *= -1;
                }
            }
        }
    }
}
