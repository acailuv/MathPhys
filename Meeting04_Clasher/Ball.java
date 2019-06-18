package Meeting04_Clasher;

import java.awt.*;
import java.util.ArrayList;

public class Ball {
    private double positionX;               // center of ball's position
    private double positionY;
    private double radius;
    private double velocityX = 0;           // ball's velocity
    private double velocityY = 0;
    private double mass;
    private final static double e = 0.5;    // ball's coefficient of resistution
    private final static double a = 0.0005; // ball's deceleration/fraction
    private Color ballColor;

    public Ball(double positionX, double positionY, double radius, Color ballColor, double mass) {
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
        this.ballColor = ballColor;
        this.mass = mass;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
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

    // drawing function
    public void draw(Graphics g) {
        Color tempColor = g.getColor();
        g.setColor(ballColor);
        g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius), (int) (2 * radius));
        g.setColor(tempColor);
    }

    // move the ball by modifying current position, with assumption that time = 1
    public void move() {
        positionX += velocityX;
        positionY += velocityY;

        double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        if (velocity > 0){
            velocityX -= a;
            velocityY -= a;
        }
    }

    public double distance(Ball other) {
        double distanceX = this.positionX - other.getPositionX();
        double distanceY = this.positionY - other.getPositionY();
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    public double distance(Hole other) {
        double distanceX = this.positionX - other.getX();
        double distanceY = this.positionY - other.getY();
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    public void ballCollide(ArrayList<Ball> balls) {
        for (Ball b: balls) {
            if (b != this && this.distance(b) <= this.radius + b.getRadius()) {
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
                double NewV1x = ((this.mass * V1x) + (b.getMass() * V2x) + b.getMass() * e *(V2x - V1x))/(this.mass + b.getMass());
                double NewV1y = V1y ;

                double NewV2x = ((this.mass * V1x) + (b.getMass() * V2x) - this.mass * e * (V2x - V1x))/(this.mass + b.getMass());
                double NewV2y = V2y ;

                //set the new velocity for the two balls colliding

                this.velocityX = NewV1x * CVectorX + NewV1y * NVectorX;
                this.velocityY = NewV1x * CVectorY + NewV1y * NVectorY;

                b.setVelocityX(NewV2x * CVectorX + NewV2y * NVectorX);
                b.setVelocityY(NewV2x * CVectorY + NewV2y * NVectorY);

                while(this.distance(b) <= (this.radius + b.getRadius()))
                {
                    this.positionX += velocityX;
                    this.positionY -= velocityY;
                    b.setPositionX(b.getPositionX() + b.getVelocityX());
                    b.setPositionX(b.getPositionX() - b.getVelocityY());
                }
            }
        }
    }

    // check collision between walls and the ball
    public void wallCollide(ArrayList<Wall> walls) {
        for (Wall w : walls) {
            if(w.distanceFromPoint(positionX, positionY) <= radius) {
                double error = radius - w.distanceFromPoint(positionX, positionY);

                //if the ball collided with a vertical wall
                if(w.getWidth() == 0) {
                    positionX += error * w.normalLine().getX();
                    positionY -= error * w.normalLine().getY();
                    velocityX *= -1;
                }

                //if the ball collided with a horizontal wall
                else if(w.getHeight() == 0) {
                    if (positionX <= w.getEndX() && positionX >= w.getStartX()) {
                        positionX += error * w.normalLine().getX();
                        positionY -= error * w.normalLine().getY();
                        velocityY *= -1;
                    }
                }

                //if diagonal
                else {
                    positionX += error * w.normalLine().getX();
                    positionY -= error * w.normalLine().getY();
                    Vector normal = w.normalLine();
                    double c = velocityX*normal.getX() + velocityY*normal.getY();
                    Vector proj = new Vector(c*normal.getX(), c*normal.getY());
                    velocityX = -2*proj.getX() + velocityX;
                    velocityY = -2*proj.getY() + velocityY;
                }
            }
        }
    }

    public void holeCollide(ArrayList<Hole> holes, Ball hitter) {
        for (Hole h : holes) {
            if(this.distance(h) <= h.getRadius() && this != hitter) {
                Clasher.balls.remove(this);
            }
        }
    }
}
