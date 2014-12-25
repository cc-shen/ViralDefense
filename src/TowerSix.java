import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * A type six tower, which amplifies the damage of surrounding towers
 * 
 * @author Jonathan & Charles Shen
 * @date June 15, 2014
 */
public class TowerSix extends Tower {

	/**
	 * Initialization for the tower
	 * 
	 * @param listTowers
	 *            the list of towers that can potential be amplified
	 */
	public TowerSix() {
		super(new ImageIcon("Images\\TowerSixBase.png").getImage(),
				new ImageIcon("Images\\TowerSixTop.png").getImage(),
				new ImageIcon("Images\\TowerFourWave.png").getImage(), 0, 0,
				0, 100, 1500, 6);
	}

	/**
	 * Constantly rotates the tower
	 */
	public void getAngle() {
		angle -= Math.PI / 90;
		if (angle <= -2 * Math.PI)
			angle = 0;
	}

	/**
	 * Draws the tower
	 * @param g2d the Graphics2D to draw the tower
	 */
	public void draw(Graphics2D g2d) {

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