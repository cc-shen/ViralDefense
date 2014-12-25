import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The "ViralDefenseMain" class. Creates a JFrame for Viral Defense.
 * Plays the game by calling the ViralDefenseGrid class.
 * 
 * @author Jonathan Ng and Charles Shen
 * @version June 12, 2013
 */
public class ViralDefenseMain extends JFrame implements ActionListener
{
	// Program variable for the game board
	private ViralDefenseGrid gameGrid;

	/**
	 * Creates a new ViralDefenseMain frame to set up the game
	 */
	public ViralDefenseMain()
	{
		// Sets up the frame
		super("Viral Defense");
		setResizable(false);
		// Sets up the main game board and add it into the 
		// main frame at the center
		gameGrid = new ViralDefenseGrid();
		this.setPreferredSize(new Dimension(640, 687));
		getContentPane().add(gameGrid, BorderLayout.CENTER);
		// Adds the menu bar to the main frame
		addGameMenuBar();

	}

	/**
	 * Sets up the menu bar and its items
	 */
	private void addGameMenuBar()
	{
		// Create a new menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create a game menu
		JMenu gameMenu = new JMenu("Game");

		// Create the game menu items
		// New game option on normal difficulty
		JMenuItem newGameNormal = new JMenuItem("New Game - Normal");
		newGameNormal.addActionListener(new ActionListener() {
			/**
			 * Responds to choosing a new game on normal difficulty
			 * @param event	The event that selected this option
			 */
			public void actionPerformed(ActionEvent event)
			{
				gameGrid.newGame(1);
			}
		});
		// New game option on hard difficulty
		JMenuItem newGameHard = new JMenuItem("New Game - Hard");
		newGameHard.addActionListener(new ActionListener() {
			/**
			 * Responds to choosing a new game on hard difficulty
			 * @param event The event that selected this option
			 */
			public void actionPerformed(ActionEvent event)
			{
				gameGrid.newGame(2);
			}
		});
		// Quit the game option
		JMenuItem exitGame = new JMenuItem("Exit Game");
		exitGame.addActionListener(new ActionListener() {
			/**
			 * Responds to choosing to quit (close) the game
			 * @param event The event that selected this option
			 */
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		// Adds the game menu items to the game menu
		gameMenu.add(newGameNormal);
		gameMenu.add(newGameHard);
		gameMenu.addSeparator();
		gameMenu.add(exitGame);

		// Create a help menu
		JMenu helpMenu = new JMenu("Help");

		// Create the help menu items
		// Instruction option
		JMenuItem instructions = new JMenuItem("Instruction");
		instructions.addActionListener(new ActionListener() {
			/**
			 * Responds to choosing to read the instructions
			 * @param event The event that selected this option
			 */
			public void actionPerformed(ActionEvent event)
			{
				JOptionPane.showMessageDialog(ViralDefenseMain.this,
					"Viruses are attacking your computer from a loop hole!\n"
						+ "Hurry! Construct different towers to defend your computer!\n"
						+ "Drag the desired tower to the square to set it into action!\n"
						+ "Plan ahead! You cannot undo your tower placements! Good luck!");
			}
		});
		// Credits option
		JMenuItem credit = new JMenuItem("Credits");
		credit.addActionListener(new ActionListener() {
			/**
			 * Responds to choosing to read the credits
			 * @param event The event that selected this option
			 */
			public void actionPerformed(ActionEvent event)
			{
				JOptionPane.showMessageDialog(ViralDefenseMain.this,
						"By Jonathan Ng and Charles Shen.\n" + "Made in June, 2013.\n");
			}
		});
		// Adds the help menu items to the help menu
		helpMenu.add(instructions);
		helpMenu.addSeparator();
		helpMenu.add(credit);

		// Adds the different menus to the menu bar
		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		// Sets the menu bar in the main frame
		setJMenuBar(menuBar);
	}

	/**
	 * Sets up a ViralDefenseMain frame
	 * @param args	An array of Strings
	 */
	public static void main(String[] args)
	{
		ViralDefenseMain mainWindow = new ViralDefenseMain();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setVisible(true);

	}

	@Override
	/**
	 * Not used. All actions are responded to when it is used in
	 * addGameMenuBar()
	 */
	public void actionPerformed(ActionEvent arg0)
	{

	}
} // ViralDefenseMain class
