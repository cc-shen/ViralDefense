/**
 * The "Projectile" class. Projectiles fired from the towers towards viruses
 * 
 * @author Jonathan Ng and Charles Shen
 * @version June 12, 2013
 */
public class Projectile {

	// Constants
	final private int SIDE_BAR_SIZE = 148;
	final private int VIRUS_SIZE = 40;
	final private int TOWER_SIZE = 40;

	// Properties of the projectiles
	private double speed;
	private int xPos;
	private int yPos;
	private Virus target;
	private boolean onBoard;

	int dx;
	int dy;
	double distance;

	/**
	 * Initialization for the projectiles
	 * 
	 * @param startSpeed
	 *            the speed the projectile will move at
	 * @param posX
	 *            the x position the projectile will begin at
	 * @param posY
	 *            the y position the projectile will begin at
	 * @param newTarget
	 *            the virus the projectile is heading towards
	 */
	public Projectile(double startSpeed, int posX, int posY, Virus newTarget) {
		speed = startSpeed;
		xPos = posX + TOWER_SIZE / 2;
		yPos = posY + TOWER_SIZE / 2;
		target = newTarget;
		onBoard = true;

		dx = target.xPos() - xPos;
		dy = target.yPos() - yPos;
		distance = Math.sqrt(dx * dx + dy * dy);

	}

	/**
	 * Moves the projectile towards the target
	 */
	public void move() {
		if (onBoard) {

			// Changes projectile's x and y position
			if (target.onBoard()) {
				dx = target.xPos() - xPos;
				dy = target.yPos() - yPos;
				distance = Math.sqrt(dx * dx + dy * dy);
			}

			// Checks if the projectile is moving too far
			if (distance <= speed) {
				xPos += dx;
				yPos += dy;
			} else {
				double reduceDistanceBy = speed / distance;
				xPos += dx * reduceDistanceBy;
				yPos += dy * reduceDistanceBy;
			}

			// Checks to see if the projectile is still on screen
			if (xPos <= SIDE_BAR_SIZE || xPos >= 640 || yPos <= 0
					|| yPos >= 640)
				onBoard = false;
		}

	}

	/**
	 * Determines if the projectile has hit the target
	 * 
	 * @return whether the projectile has hit its target
	 */
	public boolean ifHit() {
		if (xPos < target.xPos() + VIRUS_SIZE / 2
				&& xPos > target.xPos() - VIRUS_SIZE / 2
				&& yPos < target.yPos() + VIRUS_SIZE / 2
				&& yPos > target.yPos() - VIRUS_SIZE / 2)
			onBoard = false;

		return !onBoard;
	}

	/**
	 * Determines whether the projectile is off the screen
	 * 
	 * @return whether the projectile is off the screen
	 */
	public boolean offBoard() {
		if (xPos < SIDE_BAR_SIZE || xPos > 640 || yPos < 0 || yPos > 640)
			return true;
		return false;
	}

	/**
	 * Method returning onBoard to avoid changes to the onBoard variable
	 * 
	 * @return whether the projectile is still on screen
	 */
	public boolean onBoard() {
		return onBoard;
	}

	/**
	 * Method returning the x position of the projectile to avoid changes to the
	 * xPos variable
	 * 
	 * @return the x position of the projectile
	 */
	public int xPos() {
		return xPos;
	}

	/**
	 * Method returning the y position of the projectile to avoid changes to the
	 * yPos variable
	 * 
	 * @return the y position of the projectile
	 */
	public int yPos() {
		return yPos;
	}
}
