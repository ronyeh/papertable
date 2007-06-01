package cello.papertable.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import cello.papertable.event.PointEvent;
import cello.papertable.model.Page;

/**
 * Stores information for a particular input mechanism
 */
public class TableStrokeHandler extends TableInputHandler {

	Page lastPage = null;
	Path2D stroke = null;
	double lastX,lastY;
	List<Page> pages = new LinkedList<Page>();
	
	
	
	/** @see TableInputHandler#paint(Graphics2D) */
	@Override
	public void paint(Graphics2D g) {
		if (stroke!=null) {
			g.setColor(Color.GREEN);
			g.draw(stroke);
		}
	}

	/** @see TableInputHandler#inputPoint(float,float,Page,List,PointEvent) */
	@Override
	public void inputPoint(double x, double y, Page page, List<Page> activePages, PointEvent e) {

		// input type?
		switch (e.getType()) {
			case PRESS:
				System.out.println("started "+(page!=null?"on":"off"));
				startStroke(x, y);
				if (page!=null) {
					add(page);
					page.startStroke(x,y);
					page.addConstraint(this, new Point2D.Double(x,y));
				}
				break;
			case RELEASE:
				if (lastPage!=null)
					lastPage.removeConstraint(this);
				System.out.println("released "+(page!=null?"on":"off"));
				reset();
				break;
			case DRAG:
				if (page != lastPage) {
					if (lastPage!=null && page!=null)
						System.out.println("switched");
					else
						System.out.println("dragged "+(page!=null?"on":"off"));
					if (lastPage!=null) {
						lastPage.endStroke();
						lastPage.removeConstraint(this);
					}
					if (page!=null) {
						page.startStroke(x,y);
						page.moveConstraint(this, new Point2D.Double(x,y));
					}
				}
				if (page!=null) {
					add(page);
					page.addStroke(x,y);
				}
				addStroke(x,y);
				
				break;
			default:
				break;
		}
		lastPage = page;
		lastX = x;
		lastY = y;
	}

	private void startStroke(double x, double y) {
		stroke = new Path2D.Double();
		stroke.moveTo(x, y);
	}
	private void addStroke(double x, double y) {
		stroke.lineTo(x, y);
		Rectangle r = new Rectangle((int)x,(int)y);
		r.add((int)lastX,(int)lastY);;
		getView().repaint(r);
	}
	private void add(Page p) {
		pages.add(p);
		p.setActive(true);
	}
	private void reset() {
		if (stroke!=null)
			getView().repaint(stroke.getBounds());
		lastPage = null;
		stroke = null;
		for (Page p : pages)
			p.setActive(false);
		pages.clear();
	}
}