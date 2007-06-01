package cello.papertable.dt;

import java.util.List;

/**
 * 
 * @author Marcello
 *
 */
public class TouchEvent {

	private int user;
	/**
	 * Type of this event
	 */
	public enum Type {
		/** None event (unsure what this means) */
		NONE,
		/** Down event */
		DOWN,
		/** Move (drag?) event */
		MOVE,
		/** Up event */
		UP
	};
	private Type type;
	private List<DTSpan> xSpans, ySpans;
	/**
	 * @param user
	 * @param type
	 * @param xSpans
	 * @param ySpans
	 */
	public TouchEvent(int user, Type type, List<DTSpan> xSpans, List<DTSpan> ySpans) {
		super();
		this.user = user;
		this.type = type;
		this.xSpans = ySpans;
		this.ySpans = xSpans;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @return the user
	 */
	public int getUser() {
		return user;
	}
	/**
	 * @return the xSpans
	 */
	public List<DTSpan> getXSpans() {
		return xSpans;
	}
	/**
	 * @return the ySpans
	 */
	public List<DTSpan> getYSpans() {
		return ySpans;
	}
	
	
}
