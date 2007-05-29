package cello.papertable.event;

import java.awt.geom.Rectangle2D;

import cello.papertable.model.Page;

/**
 * Page event class contains a page
 * @author Marcello
 *
 */
public class PageEvent {
	
	private Page page;
	private Rectangle2D bounds;
	
	/**
	 * 
	 * @param page
	 */
	public PageEvent(Page page) {
		this(page,page.getShape().getBounds2D());
	}

	/**
	 * 
	 * @param page 
	 * @param bounds the rectangle affected by this event 
	 */
	public PageEvent(Page page, Rectangle2D bounds) {
		this.page = page;
		this.bounds = bounds;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}


	/**
	 * @return the rectangle affected by this event
	 */
	public Rectangle2D getBounds2D() {
		return bounds;
	}

}
