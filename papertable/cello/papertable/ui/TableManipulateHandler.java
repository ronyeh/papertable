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
	protected void inputPoint(float x, float y, Page page, List<Page> pages,
			PointEvent e) {

		// input type?
		switch (e.getType()) {
			case PRESS:
				activePage = page;
				
				if (activePage != null) {
					activePage.setActive(true);
					getView().getTable().bringToTop(activePage);
					activePage.addConstraint(this, new Point2D.Float(x,y));
				}
				
				break;
			case RELEASE:
				if (activePage != null) {
					activePage.setActive(false);
					activePage.removeConstraint(this);
				}
				break;
			case DRAG:
				if (activePage != null) {
					activePage.moveConstraint(this, 
							new Point2D.Float(x,y));
				}
				break;
			default:
				break;
		}

	}

}
