import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * A type one tower, which shoots fast moving projectiles
 * 
 * @author Jonathan & Charles Shen
 * @date June 15, 2014
 */
public class TowerOne extends Tower {

	/**
	 * Initialization of the tower
	 */
	public TowerOne() {
		super(new ImageIcon("Images\\TowerOneBase.png").getImage(),
				new ImageIcon("Images\\TowerOne.png").getImage(),
				new ImageIcon("Images\\ProjOne.png").getImage(), 8, 30, 4.8, 100,
				180, 1);

		totalProjIndex = -1;
	}

	/**
	 * Allows the tower to shoot projectiles
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
	 * Draws the tower
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

		// Gets angle if tower has a target in range
		if (target != null && withinRange(target.xPos(), target.yPos())) {
			angle = angle(target.xPos(), target.yPos());
		}

		// Draws the projectiles
		for (int projIndex = 0; projIndex <= totalProjIndex; projIndex++) {
			if (projList.get(projIndex).onBoard())
				g2d.drawImage(imageProj, projList.get(projIndex).xPos(),
						projList.get(projIndex).yPos(), PROJ_SIZE, PROJ_SIZE,
						null);
		}

		// Draws rotated tower
		g2d.rotate(angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);
		g2d.drawImage(imageTowerTop, this.xPos(), this.yPos(), TOWER_SIZE,
				TOWER_SIZE, null);
		g2d.rotate(-angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);

	}
}
