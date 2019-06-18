package Meeting05_Pendulum;

import java.awt.Color;
import java.awt.Font;
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
            g.setFont(new Font("Consolas", Font.PLAIN, 24));
            g.setColor(Color.BLACK);
            drawBulletString(g);
            g.drawString("Score: " + Integer.toString(Pendulum.SCORE), 0, 24);
            drawInstructions(g);
            if(Pendulum.BULLETS <= 0) {
                g.drawString("GAME OVER!", getWidth()/2, getHeight()/2);
                g.drawString("Final Score: " + Integer.toString(Pendulum.SCORE), getWidth()/2, getHeight()/2+26);
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

    public void drawBulletString(Graphics g) {
        g.drawString("Bullets: " + Integer.toString(Pendulum.BULLETS), 0, 56);
    }

    public void drawInstructions(Graphics g) {
        Font bold = new Font("Consolas", Font.BOLD, 16);
        Font plain = new Font("Consolas", Font.PLAIN, 16);

        String ln1 = "How to Play:";
        String ln2 = "> Swing the pendulum.";
        String ln3 = "> Target the Pendulum's Ball with the mouse.";
        String ln4 = "> Press SPACE to shoot!";
        String ln5 = "> Shoot moving target to get extra points~";
        String ln6 = "> Make the best out of 5 bullets.";
        String ln7 = "> Game will end if you ran out of bullets.";
        String ln8 = "> Shoot all pendulums to get even more points!";

        int currentY = 100;
        int padding = 18;

        g.setFont(bold);
        g.drawString(ln1, 0, currentY);
        currentY += padding;
        g.setFont(plain);
        g.drawString(ln2, 0, currentY);
        currentY += padding;
        g.drawString(ln3, 0, currentY);
        currentY += padding;
        g.drawString(ln4, 0, currentY);
        currentY += 2*padding;
        g.drawString(ln5, 0, currentY);
        currentY += padding;
        g.drawString(ln6, 0, currentY);
        currentY += padding;
        g.drawString(ln7, 0, currentY);
        currentY += padding;
        g.drawString(ln8, 0, currentY);

        g.setFont(new Font("Consolas", Font.PLAIN, 24));
    }
}