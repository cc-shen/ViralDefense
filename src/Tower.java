import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * The "Tower" class. Towers that defend against viruses
 * 
 * @author Jonathan Ng & Charles Shen
 * @version June 15, 2013
 */
public class Tower {
	// Constants used by the tower
	final protected int SIDE_BAR_SIZE = 148;
	final private int MARGIN_SIZE = 16;
	final private int SQUARE_SIZE = 52;
	protected final int TOWER_SIZE = 40;
	protected final int VIRUS_SIZE = 40;
	protected final int PROJ_SIZE = 8;

	// Properties of the tower
	protected static ArrayList<Virus> virusList;
	public ArrayList<Projectile> projList;
	protected Image imageAmplify;
	protected Image imageTowerBase;
	protected Image imageTowerTop;
	protected Image imageProj;
	protected double projSpeed;
	protected int shootInterval; // The 10's of milliseconds between each shot
	protected double damage;
	protected int range;
	public int cost;
	public int type;
	protected int row;
	protected int col;
	protected double angle;
	protected Virus target;
	protected int totalProjIndex;
	protected int currentShootInterval;
	public int laserOnTimer; // The amount of time the laser is on screen
	public boolean laserOn;
	protected boolean isAmplify;

	/**
	 * Intialization for the towers
	 * 
	 * @param towerBaseImage
	 *            the base image of the tower
	 * @param towerTopImage
	 *            the top image of the tower
	 * @param projImage
	 *            the projectile image of the tower
	 * @param startProjSpeed
	 *            the projectile's speed
	 * @param startShootInterval
	 *            the interval in which the tower shoots
	 * @param startDamage
	 *            the damage the tower does to viruses
	 * @param startRange
	 *            the range of the tower
	 * @param startCost
	 *            the cost in resources of the tower
	 * @param startType
	 *            the type of tower
	 */
	public Tower(Image towerBaseImage, Image towerTopImage, Image projImage,
			double startProjSpeed, int startShootInterval, double startDamage,
			int startRange, int startCost, int startType) {
		imageAmplify = new ImageIcon("Images\\TowerSixGlow.png").getImage();
		imageTowerBase = towerBaseImage;
		imageTowerTop = towerTopImage;
		imageProj = projImage;
		projSpeed = startProjSpeed;
		shootInterval = startShootInterval;
		damage = startDamage;
		range = startRange;
		cost = startCost;
		type = startType;
		angle = 0;
		isAmplify = false;

		virusList = new ArrayList<Virus>(0);
		projList = new ArrayList<Projectile>(0);

		currentShootInterval = shootInterval - 1;
	}

	/**
	 * Amplifies the damage of the tower if a type 6 tower is nearby
	 * 
	 * @param towerList
	 *            the list of towers on the grid
	 */
	public void amplify(ArrayList<Tower> towerList) {

		if (type != 6 && isAmplify == false && towerList != null) {
			for (int towerIndex = 0; towerIndex < towerList.size(); towerIndex++) {
				if (towerList.get(towerIndex).type == 6
						&& towerList.get(towerIndex)
								.withinRange(xPos(), yPos())) {
					damage *= 2;
					isAmplify = true;
				}
			}
		} else if (type == 6) {
			for (int towerIndex = 0; towerIndex < towerList.size(); towerIndex++) {
				if (towerList.get(towerIndex).type != 6
						&& withinRange(towerList.get(towerIndex).xPos(),
								towerList.get(towerIndex).yPos())) {
					towerList.get(towerIndex).damage *= 2;
					towerList.get(towerIndex).isAmplify = true;
				}
			}
		}
	}

	/**
	 * Sets the position of the tower
	 * 
	 * @param startRow
	 *            the row the tower is in
	 * @param startCol
	 *            the column the tower is in
	 */
	public void position(int startRow, int startCol) {
		row = startRow;
		col = startCol;
	}

	/**
	 * A method returning the row the tower is in to avoid accidental changes to
	 * the variable
	 * 
	 * @return the row the tower is in
	 */
	public int row() {
		return row;
	}

	/**
	 * A method returning the column the tower is in to avoid accidental changes
	 * to the variable
	 * 
	 * @return the column the tower is in
	 */
	public int col() {
		return col;
	}

	/**
	 * A method returning the x position of the tower to avoid accidental
	 * changes to the variable
	 * 
	 * @return the x position of the tower
	 */
	public int xPos() {
		return (SIDE_BAR_SIZE + MARGIN_SIZE + SQUARE_SIZE * (col - 1));
	}

	/**
	 * A method returning the y position of the tower to avoid accidental
	 * changes to the variable
	 * 
	 * @return the y position of the tower
	 */
	public int yPos() {
		return (MARGIN_SIZE + SQUARE_SIZE * (row - 1));
	}

	/**
	 * Imports the list of viruses to the tower
	 * 
	 * @param listVirus
	 *            the list of viruses currently on screen
	 */
	public void importList(ArrayList<Virus> listVirus) {
		virusList.addAll(listVirus);
	}

	/**
	 * Determines whether the tower's target is within range
	 * 
	 * @param virusX
	 *            the target's x position
	 * @param virusY
	 *            the target's y position
	 * @return whether the target is in range or not
	 */
	public boolean withinRange(int virusX, int virusY) {
		if (Math.sqrt(Math.pow(virusX - this.xPos(), 2)
				+ Math.pow(virusY - this.yPos(), 2)) <= range)
			return true;
		return false;
	}

	/**
	 * Gets a target from the list of viruses previously imported
	 */
	public void getTarget() {
		for (int virusIndex = virusList.size() - 1; virusIndex >= 0; virusIndex--) {
			if (withinRange(virusList.get(virusIndex).xPos(),
					virusList.get(virusIndex).yPos())
					&& virusList.get(virusIndex).onBoard())
				target = virusList.get(virusIndex);
		}
	}

	/**
	 * Allows the tower to shoot at its target
	 */
	public void shoot() {
		getTarget();
		if (target != null && withinRange(target.xPos(), target.yPos())
				&& target.onBoard()) {
			currentShootInterval++;
			// Shoots at certain intervals
			if (currentShootInterval == shootInterval) {
				projList.add(0, new Projectile(projSpeed, xPos(), yPos(),
						target));
				totalProjIndex++;
				currentShootInterval = 0;
			}
		}
		moveProj();
	}

	/**
	 * Moves the projectiles the tower fires
	 */
	public void moveProj() {

		// Goes through the list of projectiles
		for (int projIndex = 0; projIndex < projList.size(); projIndex++) {
			if (projList.get(projIndex).onBoard()) {
				projList.get(projIndex).move();

				// Removes the projectile when it hits its target or it goes off
				// screen
				if (target.onBoard() && projList.get(projIndex).ifHit()) {
					totalProjIndex--;
					projList.remove(projIndex);
					projIndex--;
					target.takeDamage(damage);
				} else if (projList.get(projIndex).offBoard()) {
					totalProjIndex--;
					projList.remove(projIndex);
					projIndex--;
				}
			}
		}
	}

	/**
	 * Finds the angle between the virus and the tower
	 * 
	 * @param virusX
	 *            the x position of the virus
	 * @param virusY
	 *            the y position of the virus
	 * @return the angle at which the virus makes with the tower
	 */
	public double angle(double virusX, double virusY) {
		if (virusY > this.yPos())
			return Math.atan((virusX - this.xPos()) / (this.yPos() - virusY))
					+ Math.PI;
		else
			return Math.atan((virusX - this.xPos()) / (this.yPos() - virusY));

	}

	/**
	 * Draws the tower and its projectiles
	 * 
	 * @param g2d
	 *            the Graphics2D to draw the tower and its projectiles
	 */
	public void draw(Graphics2D g2d) {
		// Gets angle if tower has a target in range
		if (target != null && withinRange(target.xPos(), target.yPos())) {
			angle = angle(target.xPos(), target.yPos());
		}

		// Draws rotated tower
		g2d.drawImage(imageTowerBase, this.xPos(), this.yPos(), TOWER_SIZE,
				TOWER_SIZE, null);
		g2d.rotate(angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);
		g2d.drawImage(imageTowerTop, this.xPos(), this.yPos(), TOWER_SIZE,
				TOWER_SIZE, null);
		g2d.rotate(-angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);

	}

} // Tower class
