package Meeting03_Dribble;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Wall {
    private Color wallColor;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double height;
    private double width;
    private int index;

    public Wall (int startX, int startY, int endX, int endY, Color wallColor, int index) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.wallColor = wallColor;
        this.height = this.endY - this.startY;
        this.width = this.endX - this.startX;
        this.index = index+1;
    }

    // parameter g is graphics object where the wall should be drawn
    public void draw(Graphics g) {
        Color tempColor = g.getColor();
        Font tempFont = g.getFont();
        g.setColor(wallColor);
        g.drawLine((int) startX, (int) startY, (int) endX, (int) endY);
        g.setColor(Color.gray);
        g.setFont(new Font("Consolas", Font.PLAIN, 24));
        if(height == 0) { //horizontal
            g.drawOval((int)(endX-width/2), (int)startY-25, 50, 50);
            g.setColor(Color.black);
            g.drawString(Integer.toString(index), (int)(endX-width/2)+18, (int)startY+6);
        } else if (width == 0) { //vertical
            g.drawOval((int)startX-25, (int)(endY-height/2), 50, 50);
            g.setColor(Color.black);
            g.drawString(Integer.toString(index), (int)startX-6, (int)(endY-height/2)+30);
        } else {
            g.drawOval((int)(endX-width/2)-18, (int)(endY-height/2)-30, 50, 50);
            g.setColor(Color.black);
            g.drawString(Integer.toString(index), (int)(endX-width/2), (int)(endY-height/2));
        }
        g.setColor(tempColor);
        g.setFont(tempFont);
    }

    // finding the unit normal vector line of a wall
    public Vector normalLine() {
        //normal vector to be returned
        Vector normVector = new Vector();
        double normVectorX, normVectorY;
        //wall vector
        Vector wallVector = new Vector(width, height);

        //calculate the normal vector, then calculate the unit vector
        normVectorY = wallVector.getX();
        normVectorX = (-1) * wallVector.getY();

        double vectorLength = Math.sqrt(wallVector.getX() * wallVector.getX() + wallVector.getY() * wallVector.getY());
        normVector.setX(normVectorX / vectorLength);
        normVector.setY(normVectorY / vectorLength);
        return normVector;
    }

    // Euclidean distance between a point and the wall
    public double distanceFromPoint(double xPoint, double yPoint) {
        double distance = Math.abs(height * xPoint + (-width) * yPoint + width * startY - height * startX);
        distance /= Math.sqrt(height * height + width * width);
        return distance;
    }

    // function to find a point in the line ax - by + (by1 - ax1) = 0 that is the nearest point from the given point (xPoint,yPoint)
    public Vector nearestPoint(double xPoint, double yPoint) {
        //point to be returned
        Vector nearestPoint = new Vector();
        //the x and y coordinate in the line which is nearest from the point given
        double nearestX, nearestY;

        //calculating the nearest x and y coordinate
        nearestX = (-width * (-width * xPoint - height * yPoint) - height * (width * startY - height * startX));
        nearestX /= ( height * height + width * width);
        nearestY = (height * (width * xPoint + height * yPoint) - (-width) * (width * startY - height * startX));
        nearestY /= (height * height + width * width);

        //return the point after setting it up
        nearestPoint.setX(nearestX);
        nearestPoint.setY(nearestY);
        return nearestPoint;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public Color getColor() {
        return wallColor;
    }
}
