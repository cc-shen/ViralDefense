import javax.swing.ImageIcon;

/**
 * A type three virus
 * 
 * @author Jonathan Ng & Charles Shen
 * @date June 15, 2013
 * 
 */
public class VirusThree extends Virus {
	/**
	 * Initialization for the virus
	 */
	public VirusThree() {
		super(new ImageIcon("Images\\VirusThree.png").getImage(), 30, 20, 2,50);
	}
}
