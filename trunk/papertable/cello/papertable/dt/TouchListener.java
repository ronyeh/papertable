package cello.papertable.dt;

/**
 * Listener used by TouchDispatcher
 * 
 * @author Marcello
 *
 */
public interface TouchListener {

	/**
	 * Called when a new touch event occurs 
	 * @param e
	 */
	public void handleTouch(TouchEvent e);

}
