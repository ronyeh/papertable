package cello.papertable.event;

/**
 * A listener for point events
 * 
 * @author Marcello
 *
 */
public interface InputListener {
	/**
	 * Triggered when a point is input
	 * @param p
	 */
	public void inputPoint(PointEvent p);
}
