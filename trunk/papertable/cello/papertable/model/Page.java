package cello.papertable.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cello.papertable.event.PageEvent;

/**
 * Class for a page object
 * 
 * @author Marcello
 *
 */
public class Page {
	private float width,height;
	private boolean active = false;
	
	private Table table = null;
	
	private AffineTransform transform;
	private Rectangle2D lastBounds = null;
	
	private Map<Object,Point2D> constraints = new HashMap<Object,Point2D>();
	
	private List<Path2D> annotations = new LinkedList<Path2D>();
	private Path2D activeStroke = null;
	
	/**
	 * Constructs a new Page object at a particular position
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Page(float x, float y, float width, float height) {
		transform = AffineTransform.getTranslateInstance(x,y);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Starts a new annotation stroke 
	 * @param sx
	 * @param sy
	 */
	public void startStroke(float sx, float sy) {
		endStroke();
		activeStroke = new Path2D.Float();
		Point2D transformed = reverseTransform(new Point2D.Float(sx,sy));
		activeStroke.moveTo(transformed.getX(), transformed.getY());
	}
	/**
	 * Adds to a started annotation stroke
	 * @param sx
	 * @param sy
	 */
	public void addStroke(float sx, float sy) {
		Point2D transformed = reverseTransform(new Point2D.Float(sx,sy));
		activeStroke.lineTo(transformed.getX(), transformed.getY());
	}
	/**
	 * Completes an existing annotation stroke
	 *
	 */
	public void endStroke() {
		if (activeStroke!=null)
			annotations.add(activeStroke);
		activeStroke = null;
	}
	
	/**
	 * @return the transformed shape in table coordinates
	 */
	public Shape getOriginalShape() {
		return new Rectangle2D.Float(0,0,width,height);
	}
	
	/**
	 * Transforms a shape based on 
	 * @param s  the shape
	 * @return the transformed shape
	 */
	public Shape getTransformedShape() {
		return getTransformedShape(getOriginalShape()); 
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
	public void paint(Graphics2D g) {

	    AffineTransform saveXform = g.getTransform();
	    g.setTransform(getTransformation());
	    
		paintContents(g);
		paintOverlay(g);

		g.setTransform(saveXform);
	}
	
	/**
	 * 
	 * @param g
	 */
	protected void paintContents(Graphics2D g) {
		Shape s = getOriginalShape();
		
		g.setColor(Color.GRAY);
		g.fill(s);
		
	}
	/**
	 * 
	 * @param g
	 */
	protected void paintOverlay(Graphics2D g) {
		
		Stroke oldStroke = g.getStroke();
		
		g.setStroke(new BasicStroke(4));
		g.setColor(Color.BLACK);
		for (Shape s : annotations)
			g.draw(s);
		if (activeStroke!=null)
			g.draw(activeStroke);
		g.setStroke(oldStroke);

		g.setColor(Color.WHITE);
		for (Shape s : annotations)
			g.draw(s);
		if (activeStroke!=null)
			g.draw(activeStroke);
			
		if (active) {
			Shape s = getOriginalShape();
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
	 * @return width of page
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param theta 
	 * @param anchorx 
	 * @param anchory 
	 */
	public void rotate(double theta, double anchorx, double anchory) {
		transform(AffineTransform.getRotateInstance(theta, anchorx, anchory));
	}
	
	/**
	 * Translate the page by dx/dy
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy) {
		transform(AffineTransform.getTranslateInstance(dx,dy));
	}
	
	/**
	 * Scales the page around a point
	 * @param scale
	 * @param anchorx
	 * @param anchory
	 */
	public void scale(double scale, double anchorx, double anchory) {
		translate(-anchorx,-anchory);
		transform(AffineTransform.getScaleInstance(scale, scale));
		translate(anchorx,anchory);
	}
	
	/**
	 * Arbitrary transformation
	 * @param at
	 */
	private void transform(AffineTransform at) {
		transform.preConcatenate(at);
		invokePageChanged();
	}


	/**
	 * Adds a constraint on movement for this object
	 * @param source
	 * @param constraint
	 */
	public void addConstraint(Object source, Point2D constraint) {
		if (constraints.size()>=2)
			return;
		constraints.put(source,constraint);
	}
	
	/**
	 * Removes an existing constraint
	 * @param source
	 */
	public void removeConstraint(Object source) {
		constraints.remove(source);
	}
	/**
	 * Moves an existing constraint to a new point transforming the underlying 
	 * object
	 * @param source
	 * @param pos
	 */
	public void moveConstraint(Object source, Point2D pos) {
		if (!constraints.containsKey(source))
			throw new IllegalArgumentException("source does not exist");
		
		switch (constraints.size()) {
			case 1:
				Point2D oldPos = constraints.get(source);
				translate((float)(pos.getX()-oldPos.getX()), 
						  (float)(pos.getY()-oldPos.getY()));
				constraints.put(source, pos);
				break;
			case 2:
				Point2D pt[] = constraints.values().toArray(new Point2D[0]);
				double oldx = (pt[0].getX()+pt[1].getX())/2;
				double oldy = (pt[0].getY()+pt[1].getY())/2;
				double olddx = pt[1].getX()-pt[0].getX();
				double olddy = pt[1].getY()-pt[0].getY();
				double oldtheta = Math.atan2(olddy, olddx);
				double oldr = Math.hypot(olddx, olddy);
				
				constraints.put(source, pos);
				Point2D pt2[] = constraints.values().toArray(new Point2D[0]);
				double newx = (pt2[0].getX()+pt2[1].getX())/2;
				double newy = (pt2[0].getY()+pt2[1].getY())/2;
				double newdx = pt2[1].getX()-pt2[0].getX();
				double newdy = pt2[1].getY()-pt2[0].getY();
				double newtheta = Math.atan2(newdy, newdx);
				double newr = Math.hypot(newdx, newdy);
				

				rotate(newtheta-oldtheta,newx,newy);
				translate(newx-oldx, newy-oldy);
				scale(newr/oldr,newx,newy);
				
				break;
		}
	}
	/**
	 * Moves an existing constraint to a new point
	 * @param source
	 * @param constraint
	 */
	public void updateConstraint(Object source, Point2D constraint) {
		if (!constraints.containsKey(source))
			throw new IllegalArgumentException("source does not exist");
		constraints.put(source,reverseTransform(constraint));
	}

	/**
	 * Transforms a pointin table space to page space
	 * @param source
	 * @return the transformed point
	 */
	private Point2D reverseTransform(Point2D source) {
		try {
			return transform.inverseTransform(source,null);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
			return null;
		}
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

		Rectangle2D newBounds = getTransformedShape().getBounds2D();
		
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

	/**
	 * @return the bounds of the transformed shape
	 */
	public Rectangle2D getBounds2D() {
		return getTransformedShape().getBounds2D();
	}

}
