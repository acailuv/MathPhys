package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Graphics;

public class Hole {
    private double x;
    private double y;
    private double radius;

    public Hole(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Hole(double x, double y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = (double)radius;
    }

    public Hole(int x, int y, int radius) {
        this.x = (double)x;
        this.y = (double)y;
        this.radius = (double)radius;
    }

    // drawing function
    public void draw(Graphics g) {
        Color tempColor = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
        g.setColor(tempColor);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

}