package cello.papertable.model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cello.papertable.event.PageEvent;
import cello.papertable.event.TableListener;

/**
 * Collection of pages
 * 
 * @author Marcello
 *
 */
public class Table {

	private float width, height;
	/**
	 * Constructs a new Table object
	 * @param width width of the table
	 * @param height height of the table
	 *
	 */
	public Table(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	private List<Page> pages = new LinkedList<Page>();
	
	/**
	 * Draws the table
	 * @param g
	 */
	public void draw(Graphics2D g) {
		for (Page p : pages)
			p.paint(g);
	}
	
	/**
	 * 
	 * @param p
	 */
	public void add(Page p) {
		p.setTable(this);
		pages.add(p);
		PageEvent e = new PageEvent(p);
		for (TableListener l : listeners)
			l.pageAdded(e);
	}
	
	/**
	 * Brings a page to the top in hte draw stack
	 * @param p
	 */
	public void bringToTop(Page p) {
		pages.remove(p);
		pages.add(p);
		PageEvent e = new PageEvent(p);
		for (TableListener l : listeners)
			l.pageAdded(e);
	}
	/**
	 * Returns the pages that intersect with the given rectangle
	 * @param r the rectangle to find intersections with
	 * @return the list of pages that qualify
	 */
	public List<Page> getPages(Rectangle2D r) {
		List<Page> resultPages = new LinkedList<Page>();
		for (Page p : pages)
			if (p.getTransformedShape().intersects(r))
				resultPages.add(p);
		
		Collections.reverse(resultPages);
		
		return resultPages;
	}
	
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the pages
	 */
	public List<Page> getPages() {
		return pages;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	} 
	
	private List<TableListener> listeners = new LinkedList<TableListener>();
	
	/**
	 * Adds a table listener
	 * @param listener
	 */
	public void addTableListener(TableListener listener) {
		listeners.add(listener);
	}
	/**
	 * Removes a table listener.
	 * @param listener
	 */
	public void removeTableListener(TableListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Trigger a page changed event
	 * @param e
	 */
	public void invokePageChanged(PageEvent e) {
		for (TableListener l : listeners)
			l.pageChanged(e);
	}
	
	
}
