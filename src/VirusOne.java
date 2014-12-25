import javax.swing.ImageIcon;

/**
 * A type one virus
 * 
 * @author Jonathan Ng & Charles Shen
 * @date June 15, 2013
 * 
 */
public class VirusOne extends Virus {
	/**
	 * Initialization for the virus
	 */
	public VirusOne() {
		super(new ImageIcon("Images\\VirusOne.png").getImage(), 20, 10, 1.5,30);
	}
}
