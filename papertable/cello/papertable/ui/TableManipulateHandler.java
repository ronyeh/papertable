package cello.papertable.ui;

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
	private float lastX,lastY;
	
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
				}
				
				break;
			case RELEASE:
				if (activePage != null)
					activePage.setActive(false);
				break;
			case DRAG:
				if (activePage != null)
					activePage.translate(x - lastX, y - lastY);
				break;
			default:
				break;
		}
		lastX = x;
		lastY = y;

	}

}
