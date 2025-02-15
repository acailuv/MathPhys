package Meeting01_GraphingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.lang.Math;

class DrawingArea extends JPanel {
    private boolean draw = false;
    private int width;
    private int height;
    private int originX;        // the origin points (0, 0)
    private int originY;
    private double scaleX;      //scaling the canvas according to lengthX & lengthY
    private double scaleY;
    private double lengthX;     // how many numbers shown along absis and ordinate
    private double lengthY;
    private double currentX;    // current X-point, the Y is retrieved from
    private double increment;   // controlling detail
    private final static int MAX_POINTS = 1000;    // in case the function is a loop, or the thread runs for far too long
    private ArrayList<Point2D.Double> points1 = new ArrayList<>();
    private ArrayList<Point2D.Double> points2 = new ArrayList<>();
    private Image drawingArea;
    private Thread animator;    // thread to draw the graph

    // setup the drawing area
    public DrawingArea(int width, int height, int cpWidth) {
        super(null);
        this.width = width - cpWidth;
        this.height = height;
        setBounds(cpWidth, 0, this.width, this.height);
        originX = this.width/2;
        originY = this.height/2;
        setBackground(Color.white);
        drawingArea = createImage(this.width, this.height);
    }

    // coordinate converter
    private double convertX(double r, double theta) {
        return r * Math.cos(theta);
    }

    private double convertY(double r, double theta) {
        return r * Math.sin(theta);
    }

    // functions to draw on the screen
    private double function1(double x) {
        return Math.sin(x)*10;
    }

    private double function2(double x) {
        return Math.cos(x)*10;
    }

    // parametric functions
    private double parametric1x(double t) {
        return 2*Math.cos(t)+Math.sin(2*t)*Math.cos(60*t);
    }

    private double parametric1y(double t) {
        return Math.sin(2*t)+Math.sin(60*t);
    }

    // polar coordinate functions
    private double polar1(double theta) {
        return Math.cos(4.4*theta)*10;
    }

    // start drawing graph
    public void beginDrawing(double lengthX, double lengthY, double startX, double increment) {
        // retrieve data
        this.lengthX = lengthX + 1;
        this.lengthY = lengthY + 1;
        this.scaleX = (double) (width - originX) / lengthX;
        this.scaleY = (double) (height - originY) / lengthY;
        this.currentX = startX;
        this.increment = increment;

        // trigger drawing process
        draw = true;
        drawingArea = createImage(width, height);
        animator = new Thread(this::eventLoop);
        animator.start();
    }

    private void eventLoop() {
        while(draw) {
            update();
            render();
            printScreen();
            try {
                animator.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // clean up the thread
        System.out.println("stopping");
        draw = false;
        points1.clear();
        points2.clear();
        try {
            animator.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // add points to be drawn
    private void update()
    {
        // while there is still a need to draw
        if(currentX < lengthX && currentX > -lengthX)
        {
            System.out.println("checking");
            currentX = currentX + increment;
            double r = polar1(currentX);
            double newX = convertX(r, currentX);
            double newY = convertY(r, currentX);
            points1.add(new Point2D.Double(newX, newY));
            points2.add(new Point2D.Double(parametric1x(currentX), parametric1y(currentX)));
        }
        else {      // cleanup for the next thread
            draw = false;
        }
    }

    // draw the buffered area before showing to screen
    private void render() {
        if (drawingArea != null) {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            //draw the x-axis and y-axis
            g.drawLine(0, originY, width, originY);
            g.drawLine(originX, 0, originX, height);

            //print numbers on the x-axis and y-axis, based on the scale
            for (int i = 0; i < lengthX; i++) {
                g.drawString(Integer.toString(i), (int) (originX + (i * scaleX)), originY);
                g.drawString(Integer.toString(-1 * i), (int) (originX + (-i * scaleX)), originY);
            }
            for (int i = 0; i < lengthY; i++) {
                g.drawString(Integer.toString(-1 * i), originX, (int) (originY + (i * scaleY)));
                g.drawString(Integer.toString(i), originX, (int) (originY + (-i * scaleY)));
            }

            // draw the lines
            for (int i = 0; i < points1.size() - 1; i++) {
                g.setColor(Color.BLACK);
                g.drawLine((int) (originX + points1.get(i).x * scaleX), (int) (originY - points1.get(i).y * scaleY),
                        (int) (originX + points1.get(i + 1).x * scaleX), (int) (originY - points1.get(i + 1).y * scaleY));
                g.setColor(Color.RED);
                g.drawLine((int) (originX + points2.get(i).x * scaleX), (int) (originY - points2.get(i).y * scaleY),
                        (int) (originX + points2.get(i + 1).x * scaleX), (int) (originY - points2.get(i + 1).y * scaleY));
            }
        }
    }

    // print the previously buffered area to screen
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