package cello.papertable.event;

import java.util.LinkedList;
import java.util.List;

/**
 * Aggregates input from multiple listeners
 * @author Marcello
 *
 */
public class InputAggregator implements InputListener {
	
	private List<InputListener> listeners = new LinkedList<InputListener>();
	
	/**
	 * Constructs a new InputAggregator
	 *
	 */
	public InputAggregator() {
		// nada
	}

	/**
	 * Adds a new input listener to the aggregator
	 * @param input
	 */
	public void addInputListener(InputListener input) {
		listeners.add(input);
	}

	/**
	 * Removes an existing input listener from the aggregator
	 * @param input
	 */
	public void removeInputListener(InputListener input) {
		listeners.remove(input);
	}

	/**
	 * @see InputListener#inputPoint(PointEvent)
	 */
	@Override
	public void inputPoint(PointEvent p) {
		for (InputListener l : listeners)
			l.inputPoint(p);
	}
}
