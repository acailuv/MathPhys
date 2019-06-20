package Meeting06_Billiard;

/*
	Matfis pertemuan 6
	Half-fledged billiard

	TODO:
	 1. Create billiard balls, use 8-ball rules.
	 2. Assign one ball to be the hitter (preferably not colored white)
	 3. Create guideline to help aiming the hitter (refer to #04 Clasher)
	 4. Add collision for ball against wall and ball against balls.
	 5. Create holes for the balls
	 6. Add additional mechanics for the game and scoring system
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Billiard {
	private JFrame frame;
	private int frameHeight;
	private int frameWidth;

	//The collections of walls to be drawn
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Ball> balls = new ArrayList<>();

	private Billiard() {
		//configure the main canvas
		frame = new JFrame("Billiard");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		frameHeight = frame.getHeight() - frame.getInsets().top;
		frameWidth = frame.getWidth();

		createObjects();

		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frameHeight, balls, walls);
		frame.add(drawingArea);

		drawingArea.start();
	}

	private void createObjects() {
		int wallWidth = (int) (frame.getWidth() * 0.9);
		int wallHeight = (int) (frameHeight * 0.6);
		int wallX = (int) (frame.getWidth() * 0.05);
		int wallY = (int) (frameHeight * 0.2);

		// vertical wall must be defined in clockwise direction
		// horizontal wall must be defined in counter clockwise direction
		walls.add(new Wall(wallWidth + wallX, wallY, wallX, wallY));	// top wall
		walls.add(new Wall(wallX, wallHeight + wallY, wallX, wallY));	// left wall
		walls.add(new Wall(wallWidth + wallX, wallY, wallWidth + wallX, wallHeight + wallY));	// bottom wall
		walls.add(new Wall(wallWidth + wallX, wallHeight + wallY, wallX, wallHeight + wallY));	// right wall

		create8Ball(2*frameWidth/3, frameHeight/2);
	}

	public void create8Ball(double startPositionX, double startPositionY) {
		Random randomGenerator = new Random();
		int ballNumber = 1;
		for(int i=1; i<=5; i++) {
			for(int j=0; j<i; j++) {
				Color c = new Color(randomGenerator.nextInt(255), randomGenerator.nextInt(255), randomGenerator.nextInt(255));
				c.darker();

				balls.add(new Ball(startPositionX, startPositionY, c, ballNumber));

				if(ballNumber == 5) {
					balls.get(balls.size()-1).setBallNumber(8);
					balls.get(balls.size()-1).setBallColor(Color.BLACK);
				} else if(ballNumber == 8) {
					balls.get(balls.size()-1).setBallNumber(5);
				}

				ballNumber++;
				startPositionY += 2*Ball.RADIUS;
			}
			startPositionX += Ball.RADIUS*Math.sqrt(3.0);
			startPositionY -= 2*i*Ball.RADIUS + Ball.RADIUS;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(Billiard::new);
	}
}
