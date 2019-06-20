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
    private ArrayList<Hole> holes;
    private Thread animator;
    private BufferedImage drawingArea;
    private Line2D guideline;
    private final static int HITTER_INDEX = 0;
    private Ball hitter;
    private Vector destination;

    public DrawingArea(int width, int height, ArrayList<Ball> balls, ArrayList<Wall> walls, ArrayList<Hole> holes, Vector destination) {
        super(null);
        this.height = height;
        this.width = width;
        setBounds(0, 0, width, height);
        this.balls = balls;
        this.walls = walls;
        this.holes = holes;
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
            b.wallCollide(walls);
            b.ballCollide(balls);
            b.holeCollideCheck(holes, hitter);
            holeCollideDeletion();
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

            for(Hole w : holes) {
                w.draw(g);
            }

            for(Wall w : walls) {
                w.draw(g);
            }
            
            for(Ball b : balls) {
                b.draw(g);
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.drawString("Score: " + Integer.toString(Billiard.SCORE), 0, 20);
            g.drawString("How to Play: Score the 8 ball last to get maximum score and win!", 0, 42);

            if (guideline != null) {
                g.setColor(Color.red);
                g.drawLine((int) guideline.getX1(), (int) guideline.getY1(), (int) guideline.getX2(), (int) guideline.getY2()-(int)Ball.RADIUS);
            }

            if (press) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Consolas", Font.PLAIN, 14));
                g.drawString("Ball Power: " + Double.toString(time), getWidth()-150, 14);
            }

            if (balls.size() == 1) {
                g.setFont(new Font("Consolas", Font.PLAIN, 24));
                g.setColor(Color.black);
                g.drawString("GAME OVER!", getWidth()/2, getHeight()/2);
                g.setFont(new Font("Consolas", Font.PLAIN, 16));
                g.drawString("Final Score: " + Integer.toString(Billiard.SCORE), getWidth()/2, getHeight()/2+26);
                if(Billiard.fail == true) {
                    g.drawString("EPIC FAIL! You have to score the 8 ball after you score others first!", getWidth()/2, getHeight()/2+26+18);
                }
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

    public void holeCollideDeletion() {
        ArrayList<Ball> newBalls = new ArrayList<>();
        for(Ball b: balls) {
            if (b.toBeDeleted == false) {
                newBalls.add(b);
            }
        }
        balls = newBalls;
    }
}
