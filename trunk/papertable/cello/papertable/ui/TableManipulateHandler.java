package cello.papertable.ui;

import java.awt.geom.Point2D;
import java.util.List;

import cello.papertable.event.PointEvent;
import cello.papertable.model.Page;

/**
 * Handles manipulating pages (translation, rotation, scaling)
 * 
 * @author Marcello
 *
 */
public class TableManipulateHandler extends TableInputHandler {
	
	private Page activePage = null;
	
	/**
	 * @see TableInputHandler#inputPoint(float, float, Page, List, PointEvent)
	 */
	@Override
	protected void inputPoint(double x, double y, Page page, List<Page> pages,
			PointEvent e) {

		// input type?
		switch (e.getType()) {
			case PRESS:
				activePage = page;
				
				if (activePage != null) {
					activePage.setActive(true);
					getView().getTable().bringToTop(activePage);
					activePage.addConstraint(e.getSource(), new Point2D.Double(x,y));
				}
				
				break;
			case RELEASE:
				if (activePage != null) {
					activePage.removeConstraint(e.getSource());
				}
				break;
			case DRAG:
				if (activePage != null) {
					activePage.moveConstraint(e.getSource(), new Point2D.Double(x,y));
				}
				break;
			default:
				break;
		}

	}
	

}
