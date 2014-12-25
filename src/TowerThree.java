import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * A type three tower, which fires a missile at viruses
 * 
 * @author Jonathan & Charles Shen
 * @date June 15, 2014
 */
public class TowerThree extends Tower {
	private int PROJ_SIZE = 12;

	/**
	 * Initialization for the tower
	 */
	public TowerThree() {
		super(new ImageIcon("Images\\TowerThreeBase.png").getImage(),
				new ImageIcon("Images\\TowerThreeTop.png").getImage(),
				new ImageIcon("Images\\ProjThree.png").getImage(), 4, 80, 12,
				150, 230, 3);

		totalProjIndex = -1;
	}

	/**
	 * Allows the tower to shoot
	 */
	public void shoot() {
		getTarget();
		if (target != null && withinRange(target.xPos(), target.yPos())
				&& target.onBoard()) {
			currentShootInterval++;
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
	 * Moves the missiles of the tower
	 */
	public void moveProj() {
		// Goes through the list of missiles fired
		for (int projIndex = 0; projIndex < projList.size(); projIndex++) {
			if (projList.get(projIndex).onBoard()) {
				projList.get(projIndex).move();

				// Removes the missiles if they have hit the target or are off
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
	 * Draws the tower and its missiles
	 * 
	 * @param g2d
	 *            the Graphics2D to draw the tower
	 */
	public void draw(Graphics2D g2d) {
		// Draws the amplify image if the tower is being amplified
		if (isAmplify) {
			g2d.drawImage(imageAmplify, xPos(), yPos(), TOWER_SIZE, TOWER_SIZE,
					null);
		}

		// Draws the projectiles
		for (int projIndex = 0; projIndex <= totalProjIndex; projIndex++) {
			if (projList.get(projIndex).onBoard())
				g2d.drawImage(imageProj, projList.get(projIndex).xPos(),
						projList.get(projIndex).yPos(), PROJ_SIZE, PROJ_SIZE,
						null);
		}

		super.draw(g2d);
	}
}