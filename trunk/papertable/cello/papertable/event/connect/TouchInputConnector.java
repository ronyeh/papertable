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
import cello.papertable.event.EventSource;
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
	
	private double threshold = 0.05;
	
	private InputListener listener = null;


	class TouchInputUser {
		private int user;
		/**
		 * @param user
		 */
		protected TouchInputUser(int user) {
			this.user = user;
		}
		
		int touchid = 0;
		
		class TouchPoint implements EventSource {
			double x,y;
			int id;
			TouchPoint(double x, double y) {
				this.x = x;
				this.y = y;
				this.id = touchid++;
			}
			/**
			 * @see EventSource#getParentSource()
			 */
			@Override
			public Object getParentSource() {
				return TouchInputConnector.this;
			}
			@Override
			public String toString() {
				return touchid+"-"+user+" touch["+x+","+y+"]";
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
			
			List<DTSpan> xspans = new ArrayList<DTSpan>(e.getXSpans());
			List<DTSpan> yspans = new ArrayList<DTSpan>(e.getYSpans());
			
			// Loop through all pairs of x and y spans
			for (DTSpan yspan : e.getYSpans()) {
				double centery = yspan.getPeak();
				for (DTSpan xspan : e.getXSpans()) {
					double centerx = xspan.getPeak();

					for (TouchPoint touch : touches) {
						double d = Math.hypot(centerx-touch.x, centery-touch.y);
						
						if (d<=threshold) {
							touch.x = centerx;
							touch.y = centery;
							listener.inputPoint(new PointEvent(touch,
									PointEvent.Type.DRAG,touch.x,touch.y));
							unmodifiedTouches.remove(touch);
							xspans.remove(xspan);
							yspans.remove(yspan);
							break;
						}
					}
				}
			}


			TouchPoint touch;
			switch (e.getType()) {
				case DOWN:
					// Just get the first (remaining) possibility
					touch = new TouchPoint(xspans.get(0).getPeak(),
										   yspans.get(0).getPeak());
					touches.add(touch);
					System.out.println(touches.size()+" add "+touch);
					listener.inputPoint(new PointEvent(touch, 
							PointEvent.Type.PRESS, touch.x, touch.y));
					break;
				case UP:
					for (TouchPoint t : touches) {
						System.out.println(touches.size()+" remove "+t);
						listener.inputPoint(new PointEvent(t,
								PointEvent.Type.RELEASE,t.x,t.y));
						touchid--;
					}
					touches.clear();
					break;
				default:
					// Find non-updated points
					for (TouchPoint t : unmodifiedTouches) {
						touches.remove(t);
						System.out.println(touches.size()+" remove "+t);
						listener.inputPoint(new PointEvent(t,
								PointEvent.Type.RELEASE,t.x,t.y));
						touchid--;
					}
					// Look for new points
					if (xspans.size()>0 && yspans.size()>0 && 
							touches.size()==1) {
						touch = new TouchPoint(xspans.get(0).getPeak(),
								  			   yspans.get(0).getPeak());
						touches.add(touch);
						System.out.println(touches.size()+" add "+touch);
						
						listener.inputPoint(new PointEvent(touch, 
							PointEvent.Type.PRESS, touch.x, touch.y));
					}
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
