package Meeting03_Dribble;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*
    MatFis pertemuan 3
    Collision between parabolically moving object against wall

    TODO:
     0. Review about elastic and inelastic collisions. What happened when you change the coefficient of resistution (COR)?
        > If affects the bounciness of the balls.
        > 0.9 means every bounce the balls loses 10% of its force (remaning 90% thus 0.9)
     1. Add more balls with different colors, sizes, and velocities
        > OK! It's done.
     2. Create UI to add new balls and delete some instances
        > Completed!
     3. Add COR field to the UI, so user can choose between using different COR than the default or not
        > OKOK!
     4. Turn all balls into linearly moving ones (apply Newton's first law here).
        > Done.
        > Did some fine tuning.
     5. Create diagonal walls and modify the calculation to adjust with diagonal walls
        > Roger!
     6. Create UI to customize the walls
        > Ready to Deploy!
 */

public class Dribble {
    private JFrame frame;
    private DrawingArea drawingArea;

    // ball creation window
    private JFrame ballWindow;

    private JLabel ballPosX_Label;
    private JLabel ballPosY_Label;
    private JLabel ballRadius_Label;
    private JLabel ballVelocityX_Label;
    private JLabel ballVelocityY_Label;
    private JLabel ballColor_Label;
    private JLabel ballIndexDelete_Label;
    private JLabel ballCOR_Label;
    private JLabel ballLinearMove_Label;

    private JTextField ballPosX_Input;
    private JTextField ballPosY_Input;
    private JTextField ballRadius_Input;
    private JTextField ballVelocityX_Input;
    private JTextField ballVelocityY_Input;
    private JPanel ballColor_Panel;
    private JTextField ballColorRed_Input;
    private JTextField ballColorGreen_Input;
    private JTextField ballColorBlue_Input;
    private JTextField ballIndexDelete_Input;
    private JTextField ballCOR_Input;
    private JCheckBox ballLinearMove_Check;

    private JButton addBall;
    private JButton delBall;

    // wall creation window
    // private JFrame wallWindow;

    // private JLabel wallStartX_Label;
    // private JLabel wallStartY_Label;
    // private JLabel wallEndX_Label;
    // private JLabel wallEndY_Label;
    // private JLabel wallColor_Label;

    // private JTextField wallStartX_Input;
    // private JTextField wallStartY_Input;
    // private JTextField wallEndX_Input;
    // private JTextField wallEndY_Input;
    // private JPanel wallColor_Panel;
    // private JTextField wallColorRed_Input;
    // private JTextField wallColorGreen_Input;
    // private JTextField wallColorBlue_Input;

    // private JButton addWall;
    // private JButton delWall;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public Dribble() {
        //configure the main canvas
        frame = new JFrame("Dribbling Balls");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // ball creation window
        ballWindow = new JFrame("Create Ball");
        ballWindow.setSize(300,300);
        ballWindow.setLayout(new GridLayout(10, 2));
        ballWindow.setResizable(false);
        ballWindow.setAlwaysOnTop(true);

        ballPosX_Label = new JLabel("X Position:");
        ballPosY_Label = new JLabel("Y Position:");
        ballRadius_Label = new JLabel("Radius:");
        ballVelocityX_Label = new JLabel("X Velocity:");
        ballVelocityY_Label = new JLabel("Y Velocity:");
        ballColor_Label = new JLabel("Color (R, G, B):");
        ballCOR_Label = new JLabel("Coefficient of Resistution:");
        ballLinearMove_Label = new JLabel("Linear Move?");
        ballIndexDelete_Label = new JLabel("Delete Ball Number:");

        ballPosX_Input = new JTextField("150");
        ballPosY_Input = new JTextField("150");
        ballRadius_Input = new JTextField("50");
        ballVelocityX_Input = new JTextField("3");
        ballVelocityY_Input = new JTextField("-5");
        ballColor_Panel = new JPanel(new GridLayout(1, 3));
        ballColorRed_Input = new JTextField("0");
        ballColorGreen_Input = new JTextField("255");
        ballColorBlue_Input = new JTextField("0");
        ballColor_Panel.add(ballColorRed_Input);
        ballColor_Panel.add(ballColorGreen_Input);
        ballColor_Panel.add(ballColorBlue_Input);
        ballCOR_Input = new JTextField("0.9");
        ballLinearMove_Check = new JCheckBox("Apply to All");
        ballLinearMove_Check.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ballLinearMove_Check.isSelected()) {
                    Ball.setGravity(0.0);
                } else {
                    Ball.setGravity(0.9);
                }
            }
        });
        ballIndexDelete_Input = new JTextField();

        addBall = new JButton("Add");
        addBall.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                balls.add(new Ball(            
                    Double.parseDouble(ballPosX_Input.getText()),
                    Double.parseDouble(ballPosY_Input.getText()),
                    Double.parseDouble(ballRadius_Input.getText()),
                    Double.parseDouble(ballVelocityX_Input.getText()),
                    Double.parseDouble(ballVelocityY_Input.getText()),
                    new Color(Integer.parseInt(ballColorRed_Input.getText()), Integer.parseInt(ballColorGreen_Input.getText()), Integer.parseInt(ballColorBlue_Input.getText())),
                    balls.size(),
                    Double.parseDouble(ballCOR_Input.getText())
                ));
            }
        });

        delBall = new JButton("Delete Ball");
        delBall.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ballIndexDelete_Input.getText().equals("")) {
                    try {
                        balls.remove(Integer.parseInt(ballIndexDelete_Input.getText())-1);
                        for(int i=0; i<balls.size(); i++) {
                            balls.get(i).setIndex(i+1);
                        }
                    } catch(Exception ex) {
                        //do nothing
                    }
                }
            }
        });

        ballWindow.add(ballPosX_Label);
        ballWindow.add(ballPosX_Input);
        ballWindow.add(ballPosY_Label);
        ballWindow.add(ballPosY_Input);
        ballWindow.add(ballRadius_Label);
        ballWindow.add(ballRadius_Input);
        ballWindow.add(ballVelocityX_Label);
        ballWindow.add(ballVelocityX_Input);
        ballWindow.add(ballVelocityY_Label);
        ballWindow.add(ballVelocityY_Input);
        ballWindow.add(ballColor_Label);
        ballWindow.add(ballColor_Panel);
        ballWindow.add(ballCOR_Label);
        ballWindow.add(ballCOR_Input);
        ballWindow.add(ballLinearMove_Label);
        ballWindow.add(ballLinearMove_Check);
        ballWindow.add(ballIndexDelete_Label);
        ballWindow.add(ballIndexDelete_Input);
        ballWindow.add(addBall);
        ballWindow.add(delBall);
        ballWindow.setLocation(1500, 300);
        ballWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // wall creation window
        // wallWindow = new JFrame("Create Wall");
        // wallWindow.setSize(300,300);
        // wallWindow.setLayout(new GridLayout(6, 2));
        // wallWindow.setResizable(false);
        // wallWindow.setAlwaysOnTop(true);

        // wallStartX_Label = new JLabel("Start X:");
        // wallStartY_Label = new JLabel("Start Y:");
        // wallEndX_Label = new JLabel("End X:");
        // wallEndY_Label = new JLabel("End Y:");
        // wallColor_Label = new JLabel("Color (R, G, B):");

        // wallStartX_Input = new JTextField("50");
        // wallStartY_Input = new JTextField("600");
        // wallEndX_Input = new JTextField("500");
        // wallEndY_Input = new JTextField("100");
        // wallColor_Panel = new JPanel(new GridLayout(1, 3));
        // wallColorRed_Input = new JTextField("255");
        // wallColorGreen_Input = new JTextField("0");
        // wallColorBlue_Input = new JTextField("0");
        // wallColor_Panel.add(wallColorRed_Input);
        // wallColor_Panel.add(wallColorGreen_Input);
        // wallColor_Panel.add(wallColorBlue_Input);

        // addWall = new JButton("Add");
        // addWall.addActionListener(new ActionListener(){
        
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         walls.add(new Wall(
        //             Integer.parseInt(wallStartX_Input.getText()),
        //             Integer.parseInt(wallStartY_Input.getText()),
        //             Integer.parseInt(wallEndX_Input.getText()),
        //             Integer.parseInt(wallEndY_Input.getText()),
        //             new Color(Integer.parseInt(wallColorRed_Input.getText()), Integer.parseInt(wallColorGreen_Input.getText()), Integer.parseInt(wallColorBlue_Input.getText()))
        //         ));
        //     }
        // });
        // delWall = new JButton("Delete Last Wall");
        // delWall.addActionListener(new ActionListener(){
        
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         walls.remove(walls.size()-1);
        //     }
        // });

        // wallWindow.add(wallStartX_Label);
        // wallWindow.add(wallStartX_Input);
        // wallWindow.add(wallStartY_Label);
        // wallWindow.add(wallStartY_Input);
        // wallWindow.add(wallEndX_Label);
        // wallWindow.add(wallEndX_Input);
        // wallWindow.add(wallEndY_Label);
        // wallWindow.add(wallEndY_Input);
        // wallWindow.add(wallColor_Label);
        // wallWindow.add(wallColor_Panel);
        // wallWindow.add(addWall);
        // wallWindow.add(delWall);
        // wallWindow.setLocation(1500, 600);
        // wallWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // create the walls
        createWalls();

        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_F12) {
                    ballWindow.setVisible(true);
                }
                // if(e.getKeyCode() == KeyEvent.VK_F11) {
                //     wallWindow.setVisible(true);
                // }
            }
        });
        frame.add(drawingArea);
        drawingArea.start();
    }

    private void createWalls() {
        // vertical wall must be defined in clockwise direction
        // horizontal wall must be defined in counter clockwise direction

        walls.add(new Wall(1300, 100, 50, 100, Color.black));	// horizontal top
        walls.add(new Wall(50, 600, 1300, 600, Color.black));  // horizontal bottom
        walls.add(new Wall(1300, 100, 1300, 600, Color.black));  // vertical right
        walls.add(new Wall(50, 600, 500, 100, Color.black));  // vertical left
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Dribble::new);
    }
}