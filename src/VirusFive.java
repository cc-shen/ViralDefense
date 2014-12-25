import javax.swing.ImageIcon;

/**
 * A type five virus
 * 
 * @author Jonathan Ng & Charles Shen
 * @date June 15, 2013
 * 
 */
public class VirusFive extends Virus {
	/**
	 * Initialization for the virus
	 */
	public VirusFive() {
		super(new ImageIcon("Images\\VirusFive.png").getImage(), 35, 25, 2, 50);
	}
}
