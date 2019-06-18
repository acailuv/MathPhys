package Meeting05_Pendulum;

/*
	Matfis pertemuan 5
	Circular motion and pendulum

	TODO:
	 1. Separate drawing area from the frame
	 2. Add more pendulums
	 3. Take care of the collision among pendulum balls. Remember, the ball moves before detecting any collisions
	 4. Minigame: hitting target with pendulum(s)
 */

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;

public class Pendulum extends JFrame {
		//the ball to be drawn
		ArrayList<Ball> balls = new ArrayList<>();
		
		// the line to be drawn
		ArrayList<Rope> ropes = new ArrayList<>();
		
		//Image where functions are drawn, which then drawn to the canvas
		DrawingArea drawingArea;
		
		private int canvasHeight;
		
		//Variables
		private Ball selectedBall;
		private Rope selectedRope;
				
		private double mousePressedX;
		private double mousePressedY;

		private int maxBalls = 1;
		private double ropeLength = 400;
		private double ballSize = 40;
		private double firstRopeX = 600;
		private double incX = 0.1;
		
		public static final double GRAVITY = 0.098;
		
		public Pendulum()
		{
			//configure the main canvas
			setExtendedState(MAXIMIZED_BOTH);
			setBackground(Color.WHITE);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			canvasHeight = getHeight() - getInsets().top;
			
			//create the pendulum
			for(int i=0; i<maxBalls; i++)
				addPendulum();

			drawingArea = new DrawingArea(getWidth(), canvasHeight, ropes, balls);
			this.add(drawingArea);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					//get the coordinate where the mouse is clicked
					mousePressedX = (double)(e.getX());
					mousePressedY = (double)(e.getY());

					//get the ball at that coordinate
					for(Ball b: balls)
						if(b.isInside(mousePressedX, mousePressedY))
						{
							selectedBall = b;
							selectedRope = b.getRope();
						}

					//get which ball is pressed
					if(selectedBall != null)
					{
						drawingArea.setPressed(true);
						selectedBall.setVx(0);
						selectedBall.setVy(0);
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					super.mouseReleased(e);
					//indicating that the mouse is not pressed anymore
					drawingArea.setPressed(false);
					selectedBall = null;
					selectedRope = null;
				}
			});

			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					super.mouseDragged(e);
					//get the coordinate where the mouse is located
					int mouseMovedX = e.getX();
					int mouseMovedY = e.getY();

					if(selectedBall != null)
					{
						Vector v = new Vector(mouseMovedX - selectedRope.getX1(), mouseMovedY - selectedRope.getY1());
						v = v.unitVector();
						v.multiply(selectedRope.getLength());

						if((selectedRope.getY1() + v.getY() - selectedBall.getRadius()) > 0)
						{
							selectedBall.move(selectedRope.getX1() + v.getX(), selectedRope.getY1() + v.getY());
						}
					}
				}
			});

			//start the thread to draw functions to canvas
			drawingArea.start();
		}
		
		public void addPendulum()
		{
			if(balls.size() < maxBalls)
			{				
				//create the ropes
				ropes.add(new Rope(firstRopeX + 2*balls.size()*(ballSize + incX), 0,
						firstRopeX + 2*balls.size()*(ballSize + incX), ropeLength, Color.blue));
				//create the balls
				balls.add(new Ball(0, 0, ballSize, 0, 0, Color.RED, 4));
				//attach the ball to the rope
				ropes.get(ropes.size()-1).attach(balls.get(balls.size()-1));				
			}
		}

		public static void main(String[] args)
		{
			EventQueue.invokeLater(Pendulum::new);
		}
}
