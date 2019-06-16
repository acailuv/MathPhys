package Meeting01_GraphingApp;

/*
    Matfis pertemuan 01
    Simple graphing app
    All UI-elements are measured in pixels, while the functions are drawn in units and angle is in radian
    Please write every decimals with dot (.) not comma (,)

    Explain how the graphing app works, which controls the coordinates shown, what functions are drawn.
    Generally, in screen with double-buffer system, there would be one layer to be displayed and another layer to be drawn on
    Thus in event loop, there are three steps:
    - Update the module (NPC's movement, player's action, etc)
    - Render the new updates in the drawing layer
    - Change current display layer with the previously drawn layer.

    TODO:
     1. Play with the app. What does each field do? What's the difference if you change one field? What if you use smaller/bigger increment factor?
        > I don't know how I can 'modify' the code to do this bit of the assignment so I just comment here to prove that I have played around with the app :)
        > I'm asumming the 'field' we are talking about is the JTextField in the program. In that case:
          'Length of X axis' field is to determine the length of x axis (duh). X ranges from -x to x.
          'Length of Y axis' field is to determine the length of y axis (duh). y ranges from -y to y.
          'Start point from X' field is to determing the start of the drawing sequence. If this field is set to 1 then the drawing will begin from x=1.
          'Incremental factor' is to determine how much is the offset of X per tick. Finer lines require smaller increment. This is the reason behind why Anti Aliasing is so costly.
        > Bigger Increment -> Rougher, less detailed lines. Smaller Increment -> Smoother, more detailed lines.
     2. Change the function into another continuous linear/polynomial function
        > Changed: (function1) 4*x*x-16 -> 3*x+1
        > Changed: (function2) 2*x+1 -> x-3
     3. Change the function into other functions with discontinuity, such as 1/x. What happened?
        > Will do.
        > Changed: (function2) x-3 -> 1/x
        > Seems like the lines are 'discontinued' m'lady.
     4. Change the function into trigonometric functions or any function that needs java.lang.Math library
        > OK.
        > Changed: (function1) 3*x+1 -> sin(x)*10
        > Changed: (function2) 1/x -> cos(x)*10
        > Changed function2's line color to RED for better visibility.
     5. Change the function and fields into parametric functions
        > OK.
        > Paramtric Equation to be used: x(t) = 2*cos(t)+sin(2*t)*cos(60*t) and y=sin(2*t)+sin(60*t)
        > I ditched the MAX_POINTS so that my graphs will draw itself until completion. Or until the black line (points1) touch the edge of the screen to be exact.
        > The parametric function is applied to the red line (points2)
     6. Change the coordinates into polar coordinates
        > Alrighty!
        > Added convertX and convertY functions to convert polar coordinates to cartesian ones.
        > The polar function is applied to the black line (points1)
*/

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.GridLayout;

class GraphingApp {
    private JFrame frame;

    // members of control panel
    private JPanel controlPanel;
    private DrawingArea drawingArea;
    private JTextField fieldLengthX;
    private JTextField fieldLengthY;
    private JTextField fieldBegin;
    private JTextField fieldIncrement;

    private int cpWidth = 270;      // set control panel's width

    public GraphingApp() {
        // setup the frame
        frame = new JFrame("Graphing App");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

        // setup control panel itself
        controlPanel = new JPanel(new GridLayout(5, 2, 5, 0));
        createControlPanel();
        controlPanel.setBounds(0, 0, cpWidth, 300);
        frame.add(controlPanel);

        // setup drawing area
        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), cpWidth);
        frame.add(drawingArea);
    }

    // add members to control panel
    private void createControlPanel() {
        // the x-axis would be shown from -lengthX to lengthX
        JLabel labelLengthX = new JLabel("Length of X axis");
        controlPanel.add(labelLengthX);
        fieldLengthX = new JTextField(2);
        controlPanel.add(fieldLengthX);

        // the y-axis would be shown from -lengthY to lengthY
        JLabel labelLengthY = new JLabel("Length of Y axis");
        controlPanel.add(labelLengthY);
        fieldLengthY = new JTextField(2);
        controlPanel.add(fieldLengthY);

        // function calculation will start from startX
        JLabel labelBegin = new JLabel("Start point from X");
        controlPanel.add(labelBegin);
        fieldBegin = new JTextField(2);
        controlPanel.add(fieldBegin);

        // the lesser the number, the more detailed it will become
        JLabel labelIncrement = new JLabel("Incremental factor");
        controlPanel.add(labelIncrement);
        fieldIncrement = new JTextField(2);
        controlPanel.add(fieldIncrement);

        // clicking the button will start the animation
        JButton button = new JButton("Begin graphing");
        button.addActionListener(e -> sendValuesToDrawer());
        controlPanel.add(button);

        //setting debug values to test things more efficiently
        fieldLengthX.setText("30");
        fieldLengthY.setText("30");
        fieldBegin.setText("-30");
        fieldIncrement.setText("0.1");
    }

    // get values from text fields
    private void sendValuesToDrawer() {
        double lengthX = Double.parseDouble(fieldLengthX.getText());
        double lengthY = Double.parseDouble(fieldLengthY.getText());
        double begin = Double.parseDouble(fieldBegin.getText());
        double increment = Double.parseDouble(fieldIncrement.getText());
        drawingArea.beginDrawing(lengthX, lengthY, begin, increment);
        drawingArea.beginDrawing(lengthX, lengthY, begin, increment);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GraphingApp::new);
    }
}