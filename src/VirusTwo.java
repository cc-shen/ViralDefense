import javax.swing.ImageIcon;

/**
 * A type two virus
 * 
 * @author Jonathan Ng & Charles Shen
 * @date June 15, 2013
 * 
 */
public class VirusTwo extends Virus {
	/**
	 * Initialization for the virus
	 */
	public VirusTwo() {
		super(new ImageIcon("Images\\VirusTwo.png").getImage(), 25, 15, 1.5,35);
	}
}
