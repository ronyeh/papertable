package cello.papertable.event;

/**
 * 
 * @author Marcello
 */
public interface EventSource {

	/**
	 * 
	 * @return allows you to get the parent source of an event source
	 */
	public Object getParentSource();
}
