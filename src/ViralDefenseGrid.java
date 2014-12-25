import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.event.*;

/**
 * The "ViralDefenseGrid" class. A tower defense game.
 * 
 * @author Jonathan Ng & Charles Shen
 * @version June 15, 2013
 */
public class ViralDefenseGrid extends JPanel implements MouseListener,
		MouseMotionListener {
	// Constants to represent what occupies each element in the 2D array
	final private int EMPTY = -1;
	final private int OCCUPIED = 0;
	final private int TOWER_ONE = 1;
	final private int TOWER_TWO = 2;
	final private int TOWER_THREE = 3;
	final private int TOWER_FOUR = 4;
	final private int TOWER_FIVE = 5;

	// Constants for calculations and logic codes
	final private int NO_OF_ROWS = 12;
	final private int NO_OF_COLUMNS = 9;
	final private int SQUARE_SIZE = 52;
	final private int TOWER_SIZE = 40;
	final private int VIRUS_SIZE = 40;
	final private int SIDE_BAR_SIZE = 148;
	final private int MARGIN_SIZE = 16;

	// To store the images of towers
	private Image imageGrid;
	private Image imageGameOver;
	private Image imageSideBar;
	private Image imageHPBar;
	private Image imageRange;
	private Image[] imageTowers = new Image[7];
	private Image[] imageInfo = new Image[7];

	// To store values for actions using the mouse (drag/hover/click)
	private int selectedTower;
	private int infoSelectedTower;
	private int toCol;
	private int toRow;
	private int hoverCol;
	private int hoverRow;
	private int draggedCol;
	private int draggedRow;
	private int draggedX;
	private int draggedY;

	// To store information such as the game board and path
	public int[][] grid;
	private ArrayList<Virus> virusList;
	private ArrayList<Tower> towerList;
	private int totalTowerIndex;
	private int totalVirusIndex;
	private int waveNumber;
	private ArrayList<GridPosition> path;
	private FindPath findPath;
	private int resources;
	private double health;
	private int totalHealth;
	public boolean gameOver;
	// Whether a wave is occurring or not
	private boolean startWave;
	// Whether a tower is being dragged or not
	private boolean isDragged;
	private boolean showRange;
	private int displayRange;

	// Constants and variables to spawn the viruses from the starting grid
	private final int TIME_INTERVAL = 10;
	private final int SPAWN_INTERVAL = 450;
	private Timer timer;
	private Timer spawnTimer;
	private int spawnCounter;
	private int currentSpawnAmount;
	private int virusToSpawn;

	// Initializes fonts
	private final Font infoFont = new Font("BATMFA_", Font.PLAIN, 20);
	private final Font costFont = new Font("BATMFA_", Font.PLAIN, 10);

	/**
	 * A constructor for the ViralDefenseGrid panel
	 */
	public ViralDefenseGrid() {
		setPreferredSize(new Dimension(640, 640));
		grid = new int[NO_OF_ROWS + 2][NO_OF_COLUMNS + 2];
		addMouseListener(this);
		addMouseMotionListener(this);

		newGame(1);

		// Initializes images used in game
		imageGrid = new ImageIcon("Images\\Grid.png").getImage();
		imageGameOver = new ImageIcon("Images\\GameOver.png").getImage();
		imageSideBar = new ImageIcon("Images\\SideBar.png").getImage();
		imageHPBar = new ImageIcon("Images\\ComputerHPBar.png").getImage();
		imageRange = new ImageIcon("Images\\Range.png").getImage();
		imageTowers[1] = new ImageIcon("Images\\TowerOne.png").getImage();
		imageTowers[2] = new ImageIcon("Images\\TowerTwo.png").getImage();
		imageTowers[3] = new ImageIcon("Images\\TowerThree.png").getImage();
		imageTowers[4] = new ImageIcon("Images\\TowerFour.png").getImage();
		imageTowers[5] = new ImageIcon("Images\\TowerFive.png").getImage();
		imageTowers[6] = new ImageIcon("Images\\TowerSix.png").getImage();
		imageInfo[1] = new ImageIcon("Images\\InfoBoxOne.png").getImage();
		imageInfo[2] = new ImageIcon("Images\\InfoBoxTwo.png").getImage();
		imageInfo[3] = new ImageIcon("Images\\InfoBoxThree.png").getImage();
		imageInfo[4] = new ImageIcon("Images\\InfoBoxFour.png").getImage();
		imageInfo[5] = new ImageIcon("Images\\InfoBoxFive.png").getImage();
		imageInfo[6] = new ImageIcon("Images\\InfoBoxSix.png").getImage();

	}

	/**
	 * Starts a new game and reset/initialize all key variables
	 * 
	 * @param difficulty
	 *            The difficulty chosen, normal is 1 and hard is 2
	 */
	public void newGame(int difficulty) {
		clearBoard();

		// Sets outside of grid as occupied
		for (int row = 0; row <= NO_OF_ROWS + 1; row++) {
			grid[row][0] = OCCUPIED;
			grid[row][NO_OF_COLUMNS + 1] = OCCUPIED;
		}
		for (int column = 0; column <= NO_OF_COLUMNS + 1; column++) {
			grid[0][column] = OCCUPIED;
			grid[NO_OF_ROWS + 1][column] = OCCUPIED;
		}
		// Sets the start and end points as occupied
		grid[1][NO_OF_COLUMNS] = OCCUPIED;
		grid[NO_OF_ROWS][1] = OCCUPIED;

		totalTowerIndex = -1;
		totalVirusIndex = -1;
		resources = 1000;

		// Sets the computer's health according to difficulty
		if (difficulty == 1)
			totalHealth = 200;
		else
			totalHealth = 100;
		health = totalHealth;

		// Resets variables and lists
		virusList = new ArrayList<Virus>(0);
		towerList = new ArrayList<Tower>(0);
		path = new ArrayList<GridPosition>();
		findPath = new FindPath();
		waveNumber = 0;
		gameOver = false;
		startWave = false;
		isDragged = false;
		showRange = false;
		displayRange = 0;
		selectedTower = 0;

		timer = new Timer(TIME_INTERVAL, new TimerEventHandler());
		timer.start();
		spawnTimer = new Timer(SPAWN_INTERVAL, new SpawnEventHandler());
		virusToSpawn = 0;

		repaint();
	}

	/**
	 * Clears all the pieces off the board
	 */
	private void clearBoard() {
		// Goes through every position in the board, making it empty
		for (int row = 1; row <= NO_OF_ROWS; row++)
			for (int col = 1; col <= NO_OF_COLUMNS; col++)
				grid[row][col] = EMPTY;
	}

	/**
	 * Starts the wave
	 */
	public void startWave() {

		// Cycles through the types of viruses to spawn
		virusToSpawn++;

		// Determines the number of viruses to spawn
		// A virus is added every other wave of the same type
		if (virusToSpawn == 1)
			currentSpawnAmount = 4 + waveNumber / 10;
		else if (virusToSpawn == 2)
			currentSpawnAmount = 3 + waveNumber / 10;
		else if (virusToSpawn == 3)
			currentSpawnAmount = 1 + waveNumber / 10;
		else if (virusToSpawn == 4)
			currentSpawnAmount = 2 + waveNumber / 10;
		else if (virusToSpawn == 5) {
			currentSpawnAmount = 3 + waveNumber / 10;
			virusToSpawn = 0;
		}

		startWave = true;
		waveNumber++;
		spawnTimer.start();

		repaint();
	}

	/**
	 * Paints graphics onto the ViralDefenseGrid panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		// Background image
		g2d.drawImage(imageGrid, 0, 0, 640, 640, this);

		// Draws the viruses
		for (int virusIndex = 0; virusIndex <= totalVirusIndex; virusIndex++) {
			virusList.get(virusIndex).draw(g2d);
		}

		// Draws the towers
		for (int towerIndex = 0; towerIndex < towerList.size(); towerIndex++) {
			towerList.get(towerIndex).draw(g2d);
		}

		// Draws the range circle of the tower being dragged
		if (selectedTower != EMPTY && isDragged == true && !gameOver) {
			g2d.drawImage(imageRange, draggedX + TOWER_SIZE / 2 - displayRange
					/ 2, draggedY + TOWER_SIZE / 2 - displayRange / 2,
					displayRange, displayRange, this);
		}

		// Draws side bar and tower list
		g2d.drawImage(imageSideBar, 0, 0, 640, 640, this);
		g2d.drawImage(imageTowers[1], 10, 100, TOWER_SIZE, TOWER_SIZE, this);
		g2d.drawImage(imageTowers[2], 55, 100, TOWER_SIZE, TOWER_SIZE, this);
		g2d.drawImage(imageTowers[3], 100, 100, TOWER_SIZE, TOWER_SIZE, this);
		g2d.drawImage(imageTowers[4], 10, 160, TOWER_SIZE, TOWER_SIZE, this);
		g2d.drawImage(imageTowers[5], 55, 160, TOWER_SIZE, TOWER_SIZE, this);
		g2d.drawImage(imageTowers[6], 100, 160, TOWER_SIZE, TOWER_SIZE, this);

		// Draws the cost in resources of each tower
		g2d.setFont(costFont);
		g2d.setColor(Color.WHITE);
		g2d.drawString("180", 22, 95);
		g2d.drawString("200", 67, 95);
		g2d.drawString("230", 112, 95);
		g2d.drawString("250", 22, 155);
		g2d.drawString("275", 67, 155);
		g2d.drawString("1500", 109, 155);

		// Draws the information along the side bar
		g2d.setFont(infoFont);
		g2d.drawString(Integer.toString(resources), 60, 55);
		g2d.drawString(Integer.toString(waveNumber), 120, 485);
		g2d.drawString(Integer.toString(totalVirusIndex + 1), 110, 535);

		// Draws the computer's health bar
		if (health <= totalHealth && health >= 0) {
			g2d.drawImage(imageHPBar, 51, 14,
					(int) (86 * (health / totalHealth)), 13, this);
		}

		if (!gameOver) {
			// Draws the information box below the tower list
			if (infoSelectedTower != 0)
				g2d.drawImage(imageInfo[infoSelectedTower], 0, 210, 150, 250,
						this);

			// Draws the tower being dragged over everything
			if (selectedTower != EMPTY && isDragged == true) {
				g2d.drawImage(imageTowers[selectedTower], draggedX, draggedY,
						TOWER_SIZE, TOWER_SIZE, this);
			}
		}

		// Displays a game over message
		if (gameOver) {
			g2d.drawImage(imageGameOver, 130, 250, 400, 100, this);
		}

	}

	/**
	 * Mouse event to see if the player has chosen a tower from the tower
	 * selection section
	 */
	public void mousePressed(MouseEvent event) {
		Point pressedPoint = event.getPoint();
		if (pressedPoint.x > 10 && pressedPoint.x < 10 + TOWER_SIZE
				&& pressedPoint.y > 020 && pressedPoint.y < 100 + TOWER_SIZE) {
			selectedTower = 1;
			displayRange = 160;
		} else if (pressedPoint.x > 55 && pressedPoint.x < 55 + TOWER_SIZE
				&& pressedPoint.y > 100 && pressedPoint.y < 100 + TOWER_SIZE) {
			selectedTower = 2;
			displayRange = 300;
		} else if (pressedPoint.x > 100 && pressedPoint.x < 100 + TOWER_SIZE
				&& pressedPoint.y > 100 && pressedPoint.y < 100 + TOWER_SIZE) {
			selectedTower = 3;
			displayRange = 300;
		} else if (pressedPoint.x > 10 && pressedPoint.x < 10 + TOWER_SIZE
				&& pressedPoint.y > 155 && pressedPoint.y < 155 + TOWER_SIZE) {
			selectedTower = 4;
			displayRange = 200;
		} else if (pressedPoint.x > 55 && pressedPoint.x < 55 + TOWER_SIZE
				&& pressedPoint.y > 155 && pressedPoint.y < 155 + TOWER_SIZE) {
			selectedTower = 5;
			displayRange = 300;
		} else if (pressedPoint.x > 100 && pressedPoint.x < 100 + TOWER_SIZE
				&& pressedPoint.y > 155 && pressedPoint.y < 155 + TOWER_SIZE) {
			selectedTower = 6;
			displayRange = 200;
		} else
			selectedTower = EMPTY;
	}

	/**
	 * Determines the position the player has released the tower to be
	 * constructed
	 */
	public void mouseReleased(MouseEvent event) {
		Point releasedPoint = event.getPoint();
		toCol = (int) (releasedPoint.x - SIDE_BAR_SIZE - MARGIN_SIZE)
				/ (SQUARE_SIZE) + 1;
		toRow = (int) (releasedPoint.y - MARGIN_SIZE) / (SQUARE_SIZE) + 1;

		// Checks to see if the position is empty or not, then adds tower to the
		// list of current towers
		if (!gameOver && selectedTower != EMPTY
				&& releasedPoint.x > SIDE_BAR_SIZE + MARGIN_SIZE
				&& releasedPoint.y > MARGIN_SIZE && grid[toRow][toCol] == EMPTY) {
			grid[toRow][toCol] = selectedTower;

			// Determines if placing a tower in this position will block the
			// viruses' path
			if ((startWave == true && virusList.size() != 0 && findPath
					.findPath(grid, virusList.get(virusList.size() - 1).row(),
							virusList.get(virusList.size() - 1).col()) != null)
					|| (findPath.findPath(grid, NO_OF_ROWS, 1) != null)) {
				totalTowerIndex++;

				// Adds tower to the list of towers
				if (selectedTower == 1)
					towerList.add(new TowerOne());
				else if (selectedTower == 2)
					towerList.add(new TowerTwo());
				else if (selectedTower == 3)
					towerList.add(new TowerThree());
				else if (selectedTower == 4)
					towerList.add(new TowerFour());
				else if (selectedTower == 5)
					towerList.add(new TowerFive());
				else if (selectedTower == 6)
					towerList.add(new TowerSix());

				// Sets the position of the tower and determines if it will be
				// amplified
				towerList.get(totalTowerIndex).position(toRow, toCol);
				towerList.get(towerList.size() - 1).amplify(towerList);

				// Determines if one has enough resources to build the tower
				if (resources - towerList.get(totalTowerIndex).cost < 0) {
					towerList.remove(towerList.size() - 1);
					totalTowerIndex--;
					grid[toRow][toCol] = EMPTY;
				} else
					resources -= towerList.get(totalTowerIndex).cost;

				// Recalculates the path for the viruses
				for (int virusIndex = 0; virusIndex < virusList.size(); virusIndex++) {
					virusList.get(virusIndex).importList(
							findPath.findPath(grid, virusList.get(virusIndex)
									.row(), virusList.get(virusIndex).col()));
				}
			} else
				grid[toRow][toCol] = EMPTY;
		}

		selectedTower = EMPTY;

		// Determines whether the player has clicked the NEXT WAVE button
		if (releasedPoint.x < SIDE_BAR_SIZE && releasedPoint.y > 550
				&& spawnCounter == 0 && !gameOver)
			startWave();

		repaint();
	}

	/**
	 * Used when the mouse is dragging a tower from the selection section onto
	 * the board
	 */
	public void mouseMoved(MouseEvent event) {
		Point hoverPoint = event.getPoint();
		// Changes cursor as player hovers over tower list
		if (hoverPoint.getX() > 10 && hoverPoint.getX() < 10 + TOWER_SIZE
				&& hoverPoint.getX() > 120
				&& hoverPoint.getY() < 120 + SQUARE_SIZE)
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());

		// Displays the information box of the tower the mouse is hovered over
		if (hoverPoint.x > 10 && hoverPoint.x < 10 + TOWER_SIZE
				&& hoverPoint.y > 100 && hoverPoint.y < 100 + TOWER_SIZE) {
			infoSelectedTower = 1;
		} else if (hoverPoint.x > 55 && hoverPoint.x < 55 + TOWER_SIZE
				&& hoverPoint.y > 100 && hoverPoint.y < 100 + TOWER_SIZE) {
			infoSelectedTower = 2;
		} else if (hoverPoint.x > 100 && hoverPoint.x < 100 + TOWER_SIZE
				&& hoverPoint.y > 100 && hoverPoint.y < 100 + TOWER_SIZE) {
			infoSelectedTower = 3;
		} else if (hoverPoint.x > 10 && hoverPoint.x < 10 + TOWER_SIZE
				&& hoverPoint.y > 155 && hoverPoint.y < 155 + TOWER_SIZE) {
			infoSelectedTower = 4;
		} else if (hoverPoint.x > 55 && hoverPoint.x < 55 + TOWER_SIZE
				&& hoverPoint.y > 175 && hoverPoint.y < 155 + TOWER_SIZE) {
			infoSelectedTower = 5;
		} else if (hoverPoint.x > 100 && hoverPoint.x < 100 + TOWER_SIZE
				&& hoverPoint.y > 155 && hoverPoint.y < 155 + TOWER_SIZE) {
			infoSelectedTower = 6;
		} else
			infoSelectedTower = 0;
	}

	/**
	 * Determines what position the dragged tower is at
	 */
	public void mouseDragged(MouseEvent event) {
		draggedX = event.getX() - TOWER_SIZE / 2;
		draggedY = event.getY() - TOWER_SIZE / 2;

		// Stores the row and column the player is dragging the tower over
		if (!gameOver && event.getX() > SIDE_BAR_SIZE + MARGIN_SIZE
				&& event.getY() > MARGIN_SIZE) {
			draggedCol = (int) (event.getX() - SIDE_BAR_SIZE - MARGIN_SIZE)
					/ (SQUARE_SIZE + 1);
			draggedRow = (int) (event.getY() - MARGIN_SIZE) / (SQUARE_SIZE + 1);
			isDragged = true;
		}

		repaint();
	}

	/**
	 * Not used
	 */
	public void mouseClicked(MouseEvent event) {

	}

	/**
	 * Not used
	 */
	public void mouseEntered(MouseEvent event) {

	}

	/**
	 * Not used
	 */
	public void mouseExited(MouseEvent event) {

	}

	/**
	 * Performs certain events every 10 milliseconds
	 * 
	 * @author Jonathan Ng & Charles Shen
	 * @date June 15, 2013
	 */

	private class TimerEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (towerList.size() >= 1)
				towerList.get(0).importList(virusList);

			// Allows the towers to shoot
			for (int towerIndex = 0; towerIndex < towerList.size(); towerIndex++) {
				towerList.get(towerIndex).shoot();
			}

			// Moves the viruses
			for (int virusIndex = 0; virusIndex <= totalVirusIndex; virusIndex++) {
				virusList.get(virusIndex).move();

				// Removes viruses from the list if they have been destroyed or
				// reach the end
				if (virusList.get(virusIndex).isRemove) {
					// Increases resources as viruses are destroyed
					resources += virusList.get(virusIndex).resources;

					virusList.get(virusIndex).isRemove = false;
					virusList.remove(virusIndex);
					totalVirusIndex--;
					virusIndex--;

					// Ends wave if all viruses are destroyed
					if (virusList.size() == 0
							&& spawnCounter == currentSpawnAmount)
						startWave = false;
				} else if (virusList.get(virusIndex).reachEnd) {

					// Decreases computer's health and ends game when health
					// reaches 0
					health -= virusList.get(virusIndex).damage;
					if (health <= 0) {
						gameOver = true;
						timer.restart();
						timer.stop();
						spawnTimer.restart();
						spawnTimer.stop();
					}

					virusList.remove(virusIndex);
					totalVirusIndex--;
					virusIndex--;

					// Ends wave if all viruses reach the end
					if (virusList.size() == 0)
						startWave = false;
				}
			}
			repaint();
		}
	}

	/**
	 * Spawns the viruses when a wave begins with a delay between each one
	 * 
	 * @author Jonathan Ng & Charles Shen
	 * @date June 15, 2013
	 */
	private class SpawnEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (startWave) {

				// Spawns a certain type of virus
				if (virusToSpawn == 1)
					virusList.add(new VirusFour());
				else if (virusToSpawn == 2)
					virusList.add(new VirusTwo());
				else if (virusToSpawn == 3)
					virusList.add(new VirusThree());
				else if (virusToSpawn == 4)
					virusList.add(new VirusOne());
				else if (virusToSpawn == 5)
					virusList.add(new VirusFive());
				else
					virusList.add(new VirusOne());

				virusList.get(virusList.size() - 1).scaleHealth(waveNumber);
				totalVirusIndex++;
				spawnCounter++;

				// Calculates the path the virus will take
				virusList.get(totalVirusIndex).importList(
						findPath.findPath(grid, virusList.get(totalVirusIndex)
								.row(), virusList.get(totalVirusIndex).col()));

				repaint();

				// Stops the timer when the correct amount of viruses have been
				// spawned
				if (spawnCounter == currentSpawnAmount) {
					spawnTimer.restart();
					spawnTimer.stop();
					spawnCounter = 0;
				}

			}
		}
	}

} // ViralDefenseBoard class
