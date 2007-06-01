/**
 * 
 */
package cello.papertable.ui;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cello.papertable.event.PointEvent;
import cello.papertable.model.Page;

/**
 * 
 * Generic class for handling input in the context of the table
 * @author Marcello
 *
 */
public abstract class TableInputHandler {

	/**
	 * 
	 */
	private TableView view;
	
	/**
	 * Set the table view
	 * @param view
	 */
	public void setTableView(TableView view) {
		if (this.view!=null)
			throw new UnsupportedOperationException("Already set view");
		this.view = view;
	}

	/**
	 * Handles direct input from 
	 * @param e
	 */
	public void inputPoint(PointEvent e) {

		double x = e.getX() * view.getTable().getWidth();
		double y = e.getY() * view.getTable().getHeight();


		List<Page> pages = view.getTable().getPages( new Rectangle2D.Double(x, y, 1, 1));
		
		Page page = null;
		if (pages.size() > 0)
			page = pages.get(0);
		
		inputPoint(x,y,page,pages,e);

	}
	
	/**
	 * @return the table view associated with this 
	 */
	protected TableView getView() {
		return view;
	}
	
	/**
	 * Handle translated input point
	 * @param x
	 * @param y
	 * @param page
	 * @param pages
	 * @param e
	 */
	protected abstract void inputPoint(double x, double y, Page page, 
			List<Page> pages, PointEvent e);
	/**
	 * Paints the input feedback (if necessary)
	 * @param g
	 */
	protected void paint(Graphics2D g) {
		// nothing
	}
	
}