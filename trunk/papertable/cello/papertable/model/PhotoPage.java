package cello.papertable.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * 
 * 
 * @author Marcello
 *
 */
public class PhotoPage extends Page {

	private BufferedImage image;
	/**
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 */
	public PhotoPage(BufferedImage image, float x, float y, float width) {
		super(x, y, width, width / image.getWidth(null) * image.getHeight(null));
		this.image = image;
	}

	/**
	 * @see Page#paint(Graphics2D)
	 */
	@Override
	public void paintContents(Graphics2D g) {
		g.drawImage(image, 0,0, (int)getWidth(), (int)getHeight(),null);
	}
	

}
