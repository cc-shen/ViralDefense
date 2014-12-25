import java.awt.Graphics2D;

import javax.swing.ImageIcon;

/**
 * A type four tower, which emits a wave damaging surrounding viruses
 * 
 * @author Jonathan & Charles Shen
 * @date June 15, 2014
 */
public class TowerFour extends Tower {

	// Properties of the wave
	public int waveRadius;
	public boolean waveOn;

	/**
	 * Initialization for tower four
	 */
	public TowerFour() {
		super(new ImageIcon("Images\\TowerFourBase.png").getImage(),
				new ImageIcon("Images\\TowerFourTop.png").getImage(),
				new ImageIcon("Images\\TowerFourWave.png").getImage(), 4, 80,
				0.064, 100, 250, 4);

		waveRadius = 0;
		waveOn = false;
	}

	/**
	 * Determines whether the target is within the wave's radius
	 * 
	 * @param virusX
	 *            the x position of the virus
	 * @param virusY
	 *            the y positin of the virus
	 * @return whether the virus is within the wave's range
	 */
	public boolean withinWave(int virusX, int virusY) {
		if (Math.sqrt(Math.pow(virusX - this.xPos(), 2)
				+ Math.pow(virusY - this.yPos(), 2)) <= waveRadius)
			return true;
		return false;
	}

	/**
	 * Allows the tower to emit its wave
	 */
	public void shoot() {
		getTarget();
		if (target != null && withinRange(target.xPos(), target.yPos())
				&& target.onBoard() && waveOn == false) {
			if (waveOn == false)
				currentShootInterval++;

			if (currentShootInterval >= shootInterval && waveOn == false) {
				waveOn = true;

				// Damages all viruses within the tower's range
				for (int virusIndex = 0;
						 virusIndex < virusList.size(); virusIndex++) {
					if (withinRange(virusList.get(virusIndex).xPos(), virusList
							.get(virusIndex).yPos())) {
						virusList.get(virusIndex).takeDamage(damage);
					}
				}
			}
		}

		// Times how long the wave is on screen
		if (waveOn == true) {
			waveRadius += 7;
			if (waveRadius >= range + 40) {
				waveOn = false;
				waveRadius = 0;
				currentShootInterval = 0;
			}
		}
	}

	/**
	 * Constantly rotates the tower
	 */
	public void getAngle() {
		angle += Math.PI / 20;
		if (angle >= 2 * Math.PI)
			angle = 0;
	}

	/**
	 * Draws the tower
	 *  @param g2d the Graphics2D to draw the tower
	 */
	public void draw(Graphics2D g2d) {
		// Draws the amplify image if the tower is being amplified
		if (isAmplify)
			g2d.drawImage(imageAmplify, xPos(), yPos(), TOWER_SIZE, TOWER_SIZE,
					null);

		// Draws the wave
		if (waveOn) {
			g2d.drawImage(imageProj, xPos() - waveRadius / 2 + TOWER_SIZE / 2,
					yPos() - waveRadius / 2 + TOWER_SIZE / 2, waveRadius,
					waveRadius, null);
		}

		// Draws the rotated tower
		getAngle();
		g2d.drawImage(imageTowerBase, this.xPos(), this.yPos(), TOWER_SIZE,
				TOWER_SIZE, null);
		g2d.rotate(angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);
		g2d.drawImage(imageTowerTop, this.xPos(), this.yPos(), TOWER_SIZE,
				TOWER_SIZE, null);
		g2d.rotate(-angle, this.xPos() + TOWER_SIZE / 2, this.yPos()
				+ TOWER_SIZE / 2);
	}
}
