import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

/**
 * A type five tower, which fires a strong laser at viruses
 * 
 * @author Jonathan & Charles Shen
 * @date June 15, 2014
 */
public class TowerFive extends Tower {

	// Constants for the color and width of the laser
	final private Color LASER_COLOR = Color.ORANGE;
	final private BasicStroke LASER_STROKE = new BasicStroke(3);

	// Properties of the laser
	private int startLineX;
	private int startLineY;
	private int endLineX;
	private int endLineY;

	/**
	 * Initialization for tower five
	 */
	public TowerFive() {
		super(new ImageIcon("Images\\TowerFiveBase.png").getImage(),
				new ImageIcon("Images\\TowerFiveTop.png").getImage(),
				new ImageIcon("Images\\ProjOne.png").getImage(), 10, 80, 15,
				150, 275, 5);

		laserOnTimer = 0;
		laserOn = false;
	}

	/**
	 * Finds the start and end points of the line
	 */
	public void getLine() {
		startLineX = xPos() + TOWER_SIZE / 2;
		startLineY = yPos() + TOWER_SIZE / 2;
		int targetX = target.xPos() + VIRUS_SIZE / 2;
		int targetY = target.yPos() + VIRUS_SIZE / 2;
		double initialLength = Math.sqrt(Math.pow(startLineX - targetX, 2)
				+ Math.pow(startLineY - targetY, 2));
		double newLength = 685.65;

		endLineX = (int) (targetX + ((targetX - startLineX) / initialLength)
				* newLength);
		endLineY = (int) (targetY + ((targetY - startLineY) / initialLength)
				* newLength);

	}

	/**
	 * Allows the tower to shoot its target
	 */
	public void shoot() {
		getTarget();
		if (target != null && withinRange(target.xPos(), target.yPos())
				&& target.onBoard() && laserOn == false) {
			currentShootInterval++;
			if (currentShootInterval >= shootInterval) {
				getLine();
				laserOn = true;
				currentShootInterval = 0;
				target.takeDamage(damage);
			}
		}

		// Times how long the laser will be on screen
		if (laserOn == true) {
			laserOnTimer++;
			if (laserOnTimer >= 20) {
				laserOn = false;
				laserOnTimer = 0;
			}
		}
	}

	/**
	 * Draws the tower and its laser
	 * 
	 * @param g2d
	 *            the Graphics2D to draw the tower
	 */
	public void draw(Graphics2D g2d) {
		// Draws the amplify image if the tower is being amplified
		if (isAmplify)
			g2d.drawImage(imageAmplify, xPos(), yPos(), TOWER_SIZE, TOWER_SIZE,
					null);

		// Draws the laser
		if (laserOn) {
			// Sets laser width
			g2d.setStroke(LASER_STROKE);

			g2d.setColor(LASER_COLOR);
			g2d.drawLine(startLineX, startLineY, endLineX, endLineY);
		}
		
		// Draws the rotated tower
		super.draw(g2d);
	}
}
