package cello.papertable.dt;

/**
 * <p>
 * 
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author Marcello
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 * 
 */
public class DTSpan {

	private int start;
	private int stop;
	private int max;

	/**
	 * Constructs a new DTSpan
	 * @param start
	 * @param stop
	 * @param max
	 */
	public DTSpan(int start, int stop, int max) {
		this.start = start;
		this.stop = stop;
		this.max = max;
	}

	/**
	 * @return start position
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return stop position
	 */
	public int getStop() {
		return stop;
	}

	/**
	 * @return max/peak position
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "DTSpan["+start + "," +max+","+ stop+"]";
	}
}
