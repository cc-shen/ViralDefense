import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

/**
 * The "Virus" class. Viruses that invade the computer
 * 
 * @author Jonathan Ng & Charles Shen
 * @version June 2013
 */
public class Virus {
	// Constants the virus uses
	final private int SIDE_BAR_SIZE = 148;
	final private int MARGIN_SIZE = 16;
	final private int SQUARE_SIZE = 52;
	final private int VIRUS_SIZE = 40;
	final private BasicStroke HEALTH_STROKE = new BasicStroke(1);

	// Properties of the virus
	private Image imageVirus;
	private double totalHealth;
	private double health;
	public int damage;
	private double speed;
	public int resources;
	private boolean onBoard;
	public boolean reachEnd;
	private double angle;
	private int row;
	private int col;
	private int xPos;
	private int yPos;
	private int nextX;
	private int nextY;
	private int pathIndex;
	public boolean isRemove;
	public int spawnInterval;
	public int spawnAmount;
	private ArrayList<GridPosition> path = new ArrayList<GridPosition>();

	/**
	 * Initialization for the virus
	 * 
	 * @param virusImage
	 *            the virus' image
	 * @param startHealth
	 *            the virus' health
	 * @param startDamage
	 *            the virus' potential damage to the computer
	 * @param startSpeed
	 *            the virus' move speed
	 * @param startResources
	 *            the amount of resources the player receives when virus is
	 *            destroyed
	 */
	public Virus(Image virusImage, double startHealth, int startDamage,
			double startSpeed, int startResources) {
		imageVirus = virusImage;
		totalHealth = startHealth;
		health = startHealth;
		damage = startDamage;
		speed = startSpeed;
		onBoard = true;
		angle = 0;
		row = 12;
		col = 1;
		xPos = SIDE_BAR_SIZE + MARGIN_SIZE;
		yPos = MARGIN_SIZE + (SQUARE_SIZE * 11);
		isRemove = false;
		reachEnd = false;
		resources = startResources;
	}

	/**
	 * Scales the virus' health according to the wave number to increase
	 * difficulty of game
	 * 
	 * @param waveNumber
	 *            The current wave number
	 */
	public void scaleHealth(int waveNumber) {
		if (waveNumber >= 6) {
			totalHealth *= 1.3 * (waveNumber / 6);
			health = totalHealth;
		}
	}

	/**
	 * Decreases the virus' health when it is hit by a tower
	 * 
	 * @param damageTaken
	 *            the amount of damage taken
	 */
	public void takeDamage(double damageTaken) {
		health -= damageTaken;
		if (health <= 0) {
			isRemove = true;
			onBoard = false;
		}
	}

	/**
	 * Sets the position of the virus
	 * 
	 * @param startRow
	 *            the row of the virus
	 * @param startCol
	 *            the column of the virus
	 */
	public void position(int startRow, int startCol) {
		row = startRow;
		col = startCol;
	}

	/**
	 * A method returning the current row of the virus to avoid accidental
	 * changes to the variable
	 * 
	 * @return the current row of the virus
	 */
	public int row() {
		return row;
	}

	/**
	 * A method returning the current column of the virus to avoid accidental
	 * changes to the variable
	 * 
	 * @return the current column of the virus
	 */
	public int col() {
		return col;
	}

	/**
	 * Calculates the x position of the virus
	 * 
	 * @return the x position of the virus
	 */
	public int xPos() {
		return xPos + VIRUS_SIZE / 2;
	}

	/**
	 * Calculates the x position of the virus
	 * 
	 * @return the x position of the virus
	 */
	public int yPos() {
		return yPos + VIRUS_SIZE / 2;
	}

	/**
	 * Calculates the x position of the virus' next position
	 * 
	 * @return the x position of the virus' next position
	 */
	public int nextX() {
		return nextX + VIRUS_SIZE / 2;
	}

	/**
	 * Calculates the y position of the virus' next position
	 * 
	 * @return the y position of the virus' next position
	 */
	public int nextY() {
		return nextY + VIRUS_SIZE / 2;
	}

	public Image imageVirus() {
		return imageVirus;
	}

	/**
	 * A method returning whether the virus is on screen or not to avoid
	 * accidental changes to the variable
	 * 
	 * @return whether the virus is on screen or not
	 */
	public boolean onBoard() {
		return onBoard;
	}

	/**
	 * Imports the virus' path
	 * 
	 * @param newPath
	 *            the path the virus will take to the end
	 */
	public void importList(ArrayList<GridPosition> newPath) {
		path.clear();
		path.trimToSize();
		path.addAll(newPath);
		pathIndex = 0;

		nextPos(path.get(pathIndex));
	}

	/**
	 * Finds the virus' next position in its current path
	 * 
	 * @param newPosition
	 *            the virus' next position
	 */
	public void nextPos(GridPosition newPosition) {
		row = newPosition.row;
		col = newPosition.col;
		nextX = newPosition.xPos();
		nextY = newPosition.yPos();
	}

	/**
	 * Allows the virus to move along its path
	 */
	public void move() {

		// Finds the next position in the path when the virus has reached the
		// next position
		if (xPos == nextX && yPos == nextY && pathIndex < path.size()) {
			nextPos(path.get(pathIndex));
			pathIndex++;
		} else if (pathIndex == path.size()) {
			onBoard = false;
			reachEnd = true;
		}

		// Moves the virus until it reaches the next position
		if (xPos < nextX)
			xPos += speed;
		else if (xPos > nextX)
			xPos -= speed;
		else if (yPos < nextY)
			yPos += speed;
		else if (yPos > nextY)
			yPos -= speed;
	}

	/**
	 * Finds the angle of the virus
	 * 
	 * @param virusX
	 *            the virus' current x position
	 * @param virusY
	 *            the virus' current y position
	 * @return the angle between the virus and its y axis
	 */
	public double angle(double virusX, double virusY) {
		if (nextY < yPos)
			return 0;
		else if (nextX > xPos)
			return Math.PI / 2;
		else if (nextX < xPos)
			return -Math.PI / 2;
		else if (nextY > yPos)
			return Math.PI;
		else
			return angle;

	}

	/**
	 * Draws the virus
	 * 
	 * @param g2d
	 *            the Graphics2D to draw the virus
	 */
	public void draw(Graphics2D g2d) {
		angle = angle(nextX, nextY);

		// Draws the rotated virus
		if (onBoard) {
			g2d.rotate(angle, xPos + VIRUS_SIZE / 2, yPos + VIRUS_SIZE / 2);
			g2d.drawImage(imageVirus, xPos, yPos, VIRUS_SIZE, VIRUS_SIZE, null);
			g2d.rotate(-angle, xPos + VIRUS_SIZE / 2, yPos + VIRUS_SIZE / 2);

			// Draws the health bar
			g2d.setColor(Color.RED);
			g2d.setStroke(HEALTH_STROKE);
			g2d.drawLine(xPos + 6, yPos - 5,
					(int) (xPos + 6 + (VIRUS_SIZE - 12)
							* (health / totalHealth)), yPos - 5);
		}
	}
} // Virus class
