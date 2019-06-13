package Meeting04_Clasher;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
    MatFis pertemuan 4
    Collision between linearly moving object against objects
    For minigame tasks, you may add more mechanics to support your minigame. Explain them under the TO-DO.

    TODO:
     1. Create an arena consists of polygon walls that does not shape rectangular.
     2. Add HUD to show velocity increase while pressing the mouse button.
     3. Minigame: there's a hole in the arena, player must move target into the hole using hitter.
     4. Minigame: there's a gap between walls, player must move target into the gap using hitter.
     5. Create multiple target.
 */

public class Clasher {
    private JFrame frame;
    private DrawingArea drawingArea;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private Ball hitter;
    private Vector destination;

    public Clasher() {
        //configure the main canvas
        frame = new JFrame("Clashing Balls");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setVisible(true);

        // create the walls
        createWalls();

        // create the ball
        balls.add(new Ball(300, 200, 50, Color.blue, 10));
        hitter = new Ball(400, 400, 20, Color.green, 4);
        balls.add(hitter);
        destination = new Vector(hitter.getPositionX(), hitter.getPositionY());

        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls, balls.size() - 1, destination);
        frame.add(drawingArea);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                drawingArea.setPress(true);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                double distanceX = e.getX() - hitter.getPositionX();
                double distanceY = e.getY() - hitter.getPositionY();
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                hitter.setVelocityX(drawingArea.getTime() * distanceX / distance);
                hitter.setVelocityY(drawingArea.getTime() * distanceY / distance);

                drawingArea.setPress(false);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }
        });

        drawingArea.start();
    }

    private void createWalls() {
        // vertical wall must be defined in clockwise direction
        // horizontal wall must be defined in counter clockwise direction

        walls.add(new Wall(1200, 100, 100, 100, Color.black));	// horizontal top
        walls.add(new Wall(100, 600, 1200, 100, Color.black));  // horizontal bottom
        walls.add(new Wall(100, 600, 100, 100, Color.black));  // vertical left
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Clasher::new);
    }
}
