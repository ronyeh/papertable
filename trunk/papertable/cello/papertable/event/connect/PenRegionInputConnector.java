package cello.papertable.event.connect;

import papertoolkit.events.EventHandler;
import papertoolkit.events.PenEvent;
import papertoolkit.paper.Region;
import papertoolkit.pen.PenSample;
import papertoolkit.pen.streaming.listeners.PenListener;
import papertoolkit.units.coordinates.PercentageCoordinates;
import cello.papertable.event.InputListener;
import cello.papertable.event.PointEvent;

/**
 * Connects Pen input region to an InputListener 
 * 
 * @author Marcello
 *
 */
public class PenRegionInputConnector implements PenListener {
	

	/**
	 * Constructs a new MouseInputAdapter with a given component and listener
	 * @param r the pen region to listen on 
	 * @param listener the listener to send events to 
	 */
	public PenRegionInputConnector(Region r, InputListener listener) {
		this.listener = listener;
		r.addEventHandler(new EventHandler() {

			@Override
			public void handleEvent(PenEvent event) {
				switch (event.getType()) {
					case DOWN:
						PenRegionInputConnector.this.listener.inputPoint(
								getEvent(PointEvent.Type.PRESS, event));
						break;
					case UP:
						PenRegionInputConnector.this.listener.inputPoint(
								getEvent(PointEvent.Type.RELEASE, event));
						break;
					case SAMPLE:
						PenRegionInputConnector.this.listener.inputPoint(
								getEvent(PointEvent.Type.DRAG, event));
						break;
				}
			}

			@Override
			public String toString() {
				return PenRegionInputConnector.this.toString();
			}
			
		});
	}
	
	private InputListener listener = null;
	

	private boolean calibrated = false;
	private float minX,minY,maxX,maxY; 

	private PointEvent getEvent(PointEvent.Type type, PenEvent e) {
		PercentageCoordinates pc = e.getPercentageLocation();
		return new PointEvent(this,type,
				pc.getPercentageInXDirection()/100.0,
				pc.getPercentageInYDirection()/100.0);
	}
	private PointEvent getEvent(PointEvent.Type type, PenSample s) {
		float x = (float)s.getX();
		float y = (float)s.getY();
		if (!calibrated) {
			minX = maxX = x;
			minY = maxY = y;
			x = y = 0;
			calibrated=true;
		} else {
			if (x<minX) minX = x;
			if (x>maxX) maxX = x;
			if (y<minY) minY = y;
			if (y>maxY) maxY = y;

			x = (x - minX) / (maxX-minX);
			y = (y - minY) / (maxY-minY);
		}
		System.out.println("="+x+","+y);
		return new PointEvent(this,type,x,y);
	}

	/** @see PenListener#penDown(PenSample) */
	public void penDown(PenSample sample) {
		listener.inputPoint(getEvent(PointEvent.Type.PRESS, sample));
	}

	/** @see PenListener#penUp(PenSample) */
	public void penUp(PenSample sample) {
		listener.inputPoint(getEvent(PointEvent.Type.RELEASE, sample));
	}

	/** @see PenListener#sample(PenSample) */
	public void sample(PenSample sample) {
		listener.inputPoint(getEvent(PointEvent.Type.DRAG, sample));
		
	}
}

