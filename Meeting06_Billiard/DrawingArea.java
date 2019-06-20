package Meeting06_Billiard;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel {
    private double time = 0;
    private final static double TIME_INCREASE = 1.;
    private boolean press = false;
    private int height;
    private int width;
    private ArrayList<Ball> balls;
    private ArrayList<Wall> walls;
    private Thread animator;
    private BufferedImage drawingArea;
    private Line2D guideline;
    private final static int HITTER_INDEX = 0;
    private Ball hitter;
    private Vector destination;

    public DrawingArea(int width, int height, ArrayList<Ball> balls, ArrayList<Wall> walls, Vector destination) {
        super(null);
        this.height = height;
        this.width = width;
        setBounds(0, 0, width, height);
        this.balls = balls;
        this.walls = walls;
        this.hitter = balls.get(HITTER_INDEX);
        this.destination = destination;
        guideline = new Line2D.Double(hitter.getPositionX(), hitter.getPositionY(), destination.getX(), destination.getY());
        animator = new Thread(this::eventLoop);
    }

    public void start() {
        animator.start();
    }

    public boolean isPressed() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
        if (!press) {
            time = 0;
        }
    }

    public double getTime() {
        return time;
    }

    private void eventLoop() {
        drawingArea = (BufferedImage) createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update()
    {
        if(press && time < 25) {
            time += TIME_INCREASE;
        }
        for(Ball b : balls)
        {
            b.move();
        }
        guideline.setLine(hitter.getPositionX(), hitter.getPositionY(), destination.getX(), destination.getY());
    }

    private void render()
    {
        if(drawingArea != null)
        {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            //clear screen
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            for(Wall w : walls) {
                w.draw(g);
            }
            
            for(Ball b : balls) {
                b.draw(g);
            }

            if (guideline != null) {
                g.setColor(Color.red);
                g.drawLine((int) guideline.getX1(), (int) guideline.getY1(), (int) guideline.getX2(), (int) guideline.getY2()-(int)Ball.RADIUS);
            }

            if (press) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Consolas", Font.PLAIN, 14));
                g.drawString("Ball Power: " + Double.toString(time), getWidth()-150, 14);
            }
        }
    }

    private void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(drawingArea != null && g != null)
            {
                g.drawImage(drawingArea, 0, 0, null);
            }

            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception ex)
        {
            System.out.println("Graphics error: " + ex);
        }
    }
}
