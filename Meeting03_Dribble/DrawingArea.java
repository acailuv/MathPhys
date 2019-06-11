package Meeting03_Dribble;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel {
    private int height;
    private int width;
    private ArrayList<Ball> balls;
    private ArrayList<Wall> walls;
    private Thread animator;
    private BufferedImage drawingArea;

    public DrawingArea(int width, int height, ArrayList<Ball> balls, ArrayList<Wall> walls) {
        super(null);
        this.height = height;
        this.width = width;
        setBounds(0, 0, width, height);
        this.balls = balls;
        this.walls = walls;
        animator = new Thread(this::eventLoop);
    }

    public void start() {
        animator.start();
    }

    private void eventLoop() {
        drawingArea = (BufferedImage) createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update()
    {
        for(Ball b : balls)
        {
            b.move();
            b.detectCollision(walls);
        }
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

            for(Ball b : balls) {
                b.draw(g);
            }

            for(Wall w : walls) {
                w.draw(g);
            }

            // help menu on how to open add ball window
            g.setColor(Color.black);
            g.drawString("Press F12 to Open Create Ball Window", 0, 12);
            g.drawString("Press F11 to Open Customize Wall Window", 0, 28);
            g.drawString("Press F10 to Open Create Wall Window", 0, 44);
            g.drawString("Make sure to gain focus to this window before pressing these shortcuts.", 0, 60);
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
