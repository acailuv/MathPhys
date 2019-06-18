package Meeting05_Pendulum;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawingArea extends JPanel {

    private ArrayList<Rope> ropes = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private int height;
    private int width;
    private Thread animator;
    private BufferedImage drawingArea;
    private boolean isPressed;

    public DrawingArea(int width, int height, ArrayList<Rope> ropes, ArrayList<Ball> balls) {
        super(null);
        this.width = width;
        this.height = height;
        setBounds(0, 0, width, height);
        this.ropes = ropes;
        this.balls = balls;
        animator = new Thread(this::eventLoop);
    }

    public void start() {
        animator.start();
    }

    public void setPressed(boolean press) {
        this.isPressed = press;
    }

    private void eventLoop() {
        drawingArea = (BufferedImage) createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
        }
    }

    public void update()
    {
        //update the rope if no mouse is pressed
        if(!isPressed)
        {
            for(Ball b: balls)
            {					
                b.move();
                b.ballCollide(balls);
            }
        }
    }
		
    public void render()
    {
        if(drawingArea != null)
        {				
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();
        
            //clear screen
            g.setColor(new Color(200,200,150));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            //draw the balls
            for(Ball b: balls)
            {
                if(b != null)
                    b.draw(g);
            }
            
            //draw the ropes				
            for(Rope r: ropes)
            {				
                r.draw(g);
            }							
        }
    }
    
    public void printScreen()
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
        } catch(Exception ex)
        {
            System.out.println("Graphics error: " + ex);  
        }		
    }
}