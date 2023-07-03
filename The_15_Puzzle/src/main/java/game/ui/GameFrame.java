package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// Set up playing screen using JFrame Class and extends from it
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener, ActionListener, PlayerActions {
	// Set initial tile position with a 4x4 matrix numbering from 0~15 (where 0 represents space)
    static int[][] tilePosition = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    // Set axis of the space tile
    static int row;
    static int column;

    // Set a counter to count how many steps a player has moved
    static int count;

    // Set playing score and best score
    static int score;
    static int myBestScore;
    
    // Set a temporary variable to store score
    static int tempScore;

    // Set a cheat flag
    static boolean cheatFlag = false;
    
    // Instantiate random number for randomizing all tiles
    Random random = new Random();
    
    // Instantiate all the classes used for creating menu bar
    // Set Menu bar of game window
    JMenuBar jMenuBar = new JMenuBar();
    JMenu functionMenu = new JMenu("Menu");
    JMenu aboutMenu = new JMenu("About");
    
    JMenuItem restartItem = new JMenuItem("Restart");
    JMenuItem closeItem = new JMenuItem("Exit");
    JMenuItem aboutItem = new JMenuItem("About Author");
    
    // Constructor to initialize
    public GameFrame() {
        // Randomize tile
        randomize(tilePosition);
        relocateSpace(tilePosition);

        // Key Input Event
        this.addKeyListener(this);

        // Generate playing elements
        generateElements(tilePosition);

        // Generate board
        generateBoard();

        // Generate Arrows & Buttons
        generateArrows();
        generateButton();

        // Generate window
        generateWindow();
        
        // Generate Menu
        generateMenu();

        // Set window visible
        super.setVisible(true);
    }
    
    // method to generate game window
    public void generateWindow() {
        // Setting up playing screen using JFrame Class
        // Set size of screen
        super.setSize(520, 800);
        // Set closing operation of screen
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Set title of screen
        super.setTitle("The 15 Puzzle_version_1.0");
        // Set basic attribute of screen: always on top and center-justified
        super.setAlwaysOnTop(true);
        super.setLocationRelativeTo(null);
        // Set default setting to null
        super.setLayout(null);
    }
    
    // method to generate menu function
    public void generateMenu() {       
        // set menu item into function menu
        functionMenu.add(restartItem);
        functionMenu.add(closeItem);
        
        // set menu item into about menu 
        aboutMenu.add(aboutItem);
        
        // implement action listener to menu item
        restartItem.addActionListener(this);
        closeItem.addActionListener(this);
        
        aboutItem.addActionListener(this);
        
        // set function menu and about menu into menu bar
        jMenuBar.add(functionMenu);
        jMenuBar.add(aboutMenu);
        
        // put menu on the menu bar
        setJMenuBar(jMenuBar);
    }
    
    // method to generate game board
    public void generateBoard() {
        JLabel board = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("board.png")));
        board.setBounds(10, 50, 480, 475);
        super.getContentPane().add(board);

    }
    
    // method to generate picture of arrows
    public void generateArrows() {
        // up arrow
        JLabel up = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("up.png")));
        up.setBounds(105, 580, 57, 57);
        super.getContentPane().add(up);
        
        // left arrow
        JLabel left = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("left.png")));
        left.setBounds(43, 637, 57, 57);
        super.getContentPane().add(left);

        // down arrow
        JLabel down = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("down.png")));
        down.setBounds(105, 637, 57, 57);
        super.getContentPane().add(down);

        // right arrow
        JLabel right = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("right.png")));
        right.setBounds(167, 637, 57, 57);
        super.getContentPane().add(right);
    }
    
    // method to generate other buttons
    public void generateButton() {
    	// reset buttons
        JLabel reset = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("reset.png")));
        reset.setBounds(288, 610, 108, 46);
        super.getContentPane().add(reset);
        
        // R button that can be pressed to initiate reset
        JLabel press = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("press.png")));
        press.setBounds(400, 610, 46, 46);
        super.getContentPane().add(press);

        // add action listener and activate mouse press on the reset button
        reset.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                count = 0;
                score = 0;
                randomize(tilePosition);
                relocateSpace(tilePosition);
                generateElements(tilePosition);

            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

    }
    
    // method to generate all elements related to playing and scores
    public void generateElements(int[][] matrix) {
        /* IMPORTANT!!!
         * in order to complete sliding effect, it is necessary to clean all the tiles
         * after the key event happens (to disclose the new layer generated underneath)
         */
        super.getContentPane().removeAll();
        
        /* check if player win the game and win without using cheat code
         * true  -> store the score and compare to best score
         * false -> do nothing
         */
        if(checkVictory()) {
            JLabel winLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("win.png")));
            winLabel.setBounds(10, 50, 480, 475);
            super.getContentPane().add(winLabel);
            tempScore = score;
            if (myBestScore == 0 && !cheatFlag) {
                myBestScore = tempScore;
            } else {
                if (tempScore <= myBestScore && !cheatFlag) {
                    myBestScore = tempScore;
                }
            }
        }

        // Create step counter on game window
        JLabel totalStep = new JLabel("Steps: " + count);
        totalStep.setBounds(15, 5, 150, 50);
        totalStep.setFont(new Font("Consolas", Font.PLAIN, 20));
        super.getContentPane().add(totalStep);

        // Set up score-related elements on panel
        JLabel totalScore = new JLabel("Scores: " + score);
        totalScore.setBounds(200, 5, 150, 50);
        totalScore.setFont(new Font("Consolas", Font.PLAIN, 20));
        super.getContentPane().add(totalScore);

        JLabel bestScore = new JLabel("Best: " + myBestScore);
        bestScore.setBounds(398, 5, 150, 50);
        bestScore.setFont(new Font("Consolas", Font.PLAIN, 20));
        super.getContentPane().add(bestScore);

        // Instruction of using keyboard arrows
        JLabel instruction = new JLabel("Please use arrow keys to slide");
        instruction.setBounds(15, 525, 400, 50);
        instruction.setFont(new Font("Consolas", Font.PLAIN, 20));
        super.getContentPane().add(instruction);

        // match numbers with its corresponding picture files
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JLabel tileLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource(matrix[i][j] + ".png")));
                tileLabel.setBounds(40 + 106 * j, 80 + 106 * i, 100, 100);
                super.getContentPane().add(tileLabel);
            }
        }
        // Repaint all elements while playing taking any action
        generateBoard();
        generateArrows();
        generateButton();
        super.getContentPane().repaint();
    }
    
    // method to randomize tiles
    public void randomize(int[][] matrix) {

        // iterate all the elements in the matrix...
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int rowRandom = random.nextInt(4);
                int columnRandom = random.nextInt(4);

                //...and swap with the random-designated element
                int temp;
                temp = matrix[i][j];
                matrix[i][j] = matrix[rowRandom][columnRandom];
                matrix[rowRandom][columnRandom] = temp;
            }
        }
    }

    /* method to prevent program lost the location of the space tile.
     * have to relocate the row and column in order to make tile movement method function correctly
     */
    public void relocateSpace(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    row = i;
                    column = j;
                }
            }
        }
    }


    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        move(keyCode);
        generateElements(tilePosition);

    }

    // the tile movement method
    /*
    some limitations:
    if column == 3, tile cannot be allowed to slide left (space tile reach boundary)
    if column == 0, tile cannot be allowed to slide right (space tile reach boundary)
    if row == 3, tile cannot be allowed to slide upwards (space tile reach boundary)
    if row == 0, tile cannot be allowed to slide downwards (space tile reach boundary)
    */

    public void move(int keycode) {
        // move left
        if (keycode == 37) {
            moveLeft();

            // move up
        } else if (keycode == 38) {
            moveUp();

            // move right
        } else if (keycode == 39) {
            moveRight();

            // move down
        } else if (keycode == 40) {
            moveDown();

            // cheat: press c
        } else if (keycode == 67) {
            cheat();
            
            // reset: press R
        } else if (keycode == 82) {
            reset();

        }
    }

    public void keyReleased(KeyEvent e) {

    }
    
    // method to check player wins the game or not
    public boolean checkVictory() {

        int[][] win = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}
        };
        for (int i = 0; i < tilePosition.length; i++) {
            for (int j = 0; j < tilePosition[i].length; j++) {
                if (tilePosition[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Override all the actions method implements form PlayerActions interface
    @Override
    public void moveUp() {
        if (row == 3) {
            return; // do nothing when tile is at the boundary
        }
        // space tile swap with beneath tile
        int temp = tilePosition[row][column];
        tilePosition[row][column] = tilePosition[row + 1][column];
        tilePosition[row + 1][column] = temp;
        row++;
        count++;
        score++;
        
    }
    
    @Override
    public void moveDown() {
        if (row == 0) {
            return; // do nothing when tile is at the boundary
        }
        // space tile swap with upper tile
        int temp = tilePosition[row][column];
        tilePosition[row][column] = tilePosition[row - 1][column];
        tilePosition[row - 1][column] = temp;
        row--;
        count++;
        score++;

    }
    
    @Override
    public void moveRight() {
        if (column == 0) {
            return; // do nothing when tile is at the boundary
        }
        // space tile swap with left-hand tile
        int temp = tilePosition[row][column];
        tilePosition[row][column] = tilePosition[row][column - 1];
        tilePosition[row][column - 1] = temp;
        column--;
        count++;
        score++;

    }
    
    @Override
    public void moveLeft() {
        if (column == 3) {
            return; // do nothing when tile is at the boundary
        }
        // space tile swap with right-hand tile
        int temp = tilePosition[row][column];
        tilePosition[row][column] = tilePosition[row][column + 1];
        tilePosition[row][column + 1] = temp;
        column++;
        count++;
        score++;

    }
    
    @Override
    public void reset() {
        count = 0;
        score = 0;
        randomize(tilePosition);
        relocateSpace(tilePosition);
        generateElements(tilePosition);
        cheatFlag = false;

    }

    @Override
    public void cheat() {
        tilePosition = new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}
        };

        cheatFlag = true;

    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// check which menu item to be clicked at
		Object obj = e.getSource();
		if (obj == restartItem) {
	        count = 0;
	        score = 0;
	        randomize(tilePosition);
	        relocateSpace(tilePosition);
	        generateElements(tilePosition);
	        cheatFlag = false;
			
		} else if (obj == closeItem) {
			System.exit(0);
			
		} else if (obj == aboutItem) {
			// create a pop-up screen
			JDialog jDialog = new JDialog();
	        JLabel qr = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("GIT_QR.png")));
	        // set position and height & width
	        qr.setBounds(0, 0, 250, 250);
	        // add image to pop-up screen
	        jDialog.getContentPane().add(qr);
	        // pop-up screen settings
	        jDialog.setSize(344, 344);
	        jDialog.setTitle("About the Author");
	        jDialog.setAlwaysOnTop(true);
	        jDialog.setLocationRelativeTo(null);
	        jDialog.setModal(true);
	        jDialog.setVisible(true);
	        
		}
		
	}
    
}

