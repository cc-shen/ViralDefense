import javax.swing.ImageIcon;

/**
 * A type four virus
 * 
 * @author Jonathan Ng & Charles Shen
 * @date June 15, 2013
 * 
 */
public class VirusFour extends Virus {
	/**
	 * Initialization for the virus
	 */
	public VirusFour() {
		super(new ImageIcon("Images\\VirusFour.png").getImage(), 10, 20, 1,25);
	}
}
