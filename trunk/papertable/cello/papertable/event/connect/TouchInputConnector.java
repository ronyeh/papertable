package cello.papertable.event.connect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cello.papertable.dt.DTSpan;
import cello.papertable.dt.TouchDispatcher;
import cello.papertable.dt.TouchEvent;
import cello.papertable.dt.TouchListener;
import cello.papertable.event.InputListener;
import cello.papertable.event.PointEvent;

/**
 * 
 * @author Marcello
 */
public class TouchInputConnector implements TouchListener {
	
	private Map<Integer,TouchInputUser> users 
								= new HashMap<Integer,TouchInputUser>();
	
	/**
	 * Constructs a new touch input connector
	 * @param dispatcher
	 * @param listener
	 */
	public TouchInputConnector(TouchDispatcher dispatcher, 
			InputListener listener) {
		this.listener = listener;
		dispatcher.addTouchListener(this);
	}
	
	private double threshold = 50;
	
	private InputListener listener = null;


	class TouchInputUser {
		private int user;
		/**
		 * @param user
		 */
		protected TouchInputUser(int user) {
			this.user = user;
		}
		
		
		class TouchPoint {
			double x,y;
			TouchPoint(double x, double y) {
				this.x = x;
				this.y = y;
			}
		}
		
		private List<TouchPoint> touches = new ArrayList<TouchPoint>();
		
		/**
		 * @see Object#toString()
		 */
		@Override
		public String toString() {
			return "TouchInputUser["+user+"]";
		}

		/**
		 * Handle a touch event
		 * @param e
		 */
		public void handleTouch(TouchEvent e) {

			List<TouchPoint> unmodifiedTouches 
					= new LinkedList<TouchPoint>(touches);
			
			List<DTSpan> xspans = new ArrayList<DTSpan>();
			List<DTSpan> yspans = new ArrayList<DTSpan>();
			
			// Loop through all pairs of x and y spans
			for (DTSpan yspan : e.getYSpans()) {
				boolean yused = false;
				double centery = yspan.getMax();
				for (DTSpan xspan : e.getXSpans()) {
					boolean xused = false;
					double centerx = xspan.getMax();

					for (TouchPoint touch : touches) {
						double d = Math.hypot(centerx-touch.x, centery-touch.y);
						
						if (d<=threshold) {
							touch.x = centerx;
							touch.y = centery;
							listener.inputPoint(new PointEvent(TouchInputConnector.this,
									PointEvent.Type.DRAG,touch.x,touch.y));
							unmodifiedTouches.remove(touch);
							xused = yused = true;
							break;
						}
					}
					if (!xused)
						xspans.add(xspan);
				}
				if (!yused)
					yspans.add(yspan);
			}


			
			switch (e.getType()) {
				case DOWN:
					// Just get the first (remaining) possibility
					TouchPoint touch = new TouchPoint(xspans.get(0).getMax(),
													  yspans.get(0).getMax());
					touches.add(touch);
					listener.inputPoint(new PointEvent(TouchInputConnector.this, 
							PointEvent.Type.PRESS, touch.x, touch.y));
					break;
				case UP:
					// Find non-updated points
					for (TouchPoint t : unmodifiedTouches) {
						touches.remove(t);
						listener.inputPoint(new PointEvent(TouchInputConnector.this,
								PointEvent.Type.RELEASE,t.x,t.y));
					}
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * @see TouchListener#handleTouch(TouchEvent)
	 */
	public void handleTouch(TouchEvent e) {
		TouchInputUser user = users.get(e.getUser());
		if (user==null) {
			user = new TouchInputUser(e.getUser());
			users.put(e.getUser(), user);
		}
		user.handleTouch(e);
	}
	/**
	 * @return the users found by this input connector
	 */
	public Collection<TouchInputUser> getUsers() {
		return users.values();
	}


}
