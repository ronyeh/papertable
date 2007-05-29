package cello.papertable.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import cello.papertable.event.PageEvent;

/**
 * Class for a page object
 * 
 * @author Marcello
 *
 */
public class Page {
	
	private float x, y;
	private float anchorX, anchorY;
	private float width, height;
	private float rotation;
	
	private boolean active = false;
	
	private Table table = null;
	
	private AffineTransform rotateTransform, translateTransform, transform;
	private Rectangle2D lastBounds = null;
	
	/**
	 * Constructs a new Page object at a particular position
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Page(float x, float y, float width, float height) {
		translateTransform = AffineTransform.getTranslateInstance(0, 0);
		rotateTransform = AffineTransform.getTranslateInstance(0, 0);
		setRotation(0);
		setLocation(x,y);
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * @return the transformed shape in table coordinates
	 */
	public Shape getShape() {
		return getTransformedShape(new Rectangle2D.Float(0,0,width,height));
	}
	
	/**
	 * Transforms a shape based on 
	 * @param s  the shape
	 * @return the transformed shape
	 */
	public Shape getTransformedShape(Shape s) {
		return transform.createTransformedShape(s); 
	}
	
	/**
	 * @return the current transformation
	 */
	public AffineTransform getTransformation() {
		return transform;
	}

	/**
	 * @param g drawing surface
	 * 
	 */
	public void draw(Graphics2D g) {
		drawContents(g);
		drawOverlay(g);
	}
	
	/**
	 * 
	 * @param g
	 */
	protected void drawContents(Graphics2D g) {
		Shape s = getShape();
		
		g.setColor(Color.GRAY);
		g.fill(s);
		
	}
	/**
	 * 
	 * @param g
	 */
	protected void drawOverlay(Graphics2D g) {
		Shape s = getShape();
		
		if (active) {
			g.setColor(Color.WHITE);
			g.draw(s);
		}
		
	}

	/**
	 * @return height of page
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Set height of page
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return width of page
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Set width of page
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return x position
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set x position
	 * @param x
	 */
	public void setX(float x) {
		setLocation(x,y);
	}

	/**
	 * @return y position
	 */
	public float getY() {
		return y;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(float y) {
		setLocation(x,y);
	}
	
	/**
	 * Set the current location
	 * @param x
	 * @param y
	 */
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		translateTransform = AffineTransform.getTranslateInstance(x,y);
		updateTransform();
	}
	
	/**
	 * Translate the page by dx/dy
	 * @param dx
	 * @param dy
	 */
	public void translate(float dx, float dy) {
		setLocation(getX()+dx,getY()+dy);
	}
	
	private void updateTransform() {
		transform = (AffineTransform)translateTransform.clone();
		transform.concatenate(rotateTransform);
		invokePageChanged();
	}


	
	/**
	 * Sets the anchor
	 * @param x
	 * @param y
	 */
	public void setAnchor(float x, float y) {
		this.anchorX = x * getWidth();
		this.anchorY = y * getHeight();
		updateRotation();
	}
	
	/**
	 * @return the anchorx
	 */
	public float getAnchorX() {
		return anchorX;
	}


	/**
	 * @return the anchory
	 */
	public float getAnchorY() {
		return anchorY;
	}
	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
		updateRotation();
	}
	
	private void updateRotation() {
		rotateTransform = 
			AffineTransform.getRotateInstance(rotation, anchorX, anchorY);
		updateTransform(); 
		
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
		invokePageChanged();
	}

	private void invokePageChanged() {
		if (table==null)
			return;

		Rectangle2D newBounds = getShape().getBounds2D();
		
		if (lastBounds!=null)
			lastBounds.add(newBounds);
		else
			lastBounds = newBounds; 

		table.invokePageChanged(new PageEvent(this,lastBounds));
		lastBounds = newBounds;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}

}
