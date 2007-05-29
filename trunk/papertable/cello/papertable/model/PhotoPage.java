package cello.papertable.model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
	 * @see Page#draw(Graphics2D)
	 */
	@Override
	public void drawContents(Graphics2D g) {
	    AffineTransform saveXform = g.getTransform();
	    
	    g.setTransform(getTransformation());

		g.drawImage(image, 0,0, (int)getWidth(), (int)getHeight(),null);
		
		g.setTransform(saveXform);
	}
	

}
