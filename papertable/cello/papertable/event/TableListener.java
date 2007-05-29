package cello.papertable.event;

/**
 * Table listener
 * @author Marcello
 *
 */
public interface TableListener {

	/**
	 * Triggered when a page is added
	 * @param p
	 */
	public void pageAdded(PageEvent p);
	/**
	 * Triggered when a page is changed
	 * @param p
	 */
	public void pageChanged(PageEvent p);
}
