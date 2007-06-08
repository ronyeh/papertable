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

	private double start;
	private double stop;
	private double peak;

	/**
	 * Constructs a new DTSpan
	 * @param start
	 * @param stop
	 * @param peak
	 */
	public DTSpan(double start, double stop, double peak) {
		this.start = start;
		this.stop = stop;
		this.peak = peak;
	}

	/**
	 * @return start position
	 */
	public double getStart() {
		return start;
	}

	/**
	 * @return stop position
	 */
	public double getStop() {
		return stop;
	}

	/**
	 * @return max/peak position
	 */
	public double getPeak() {
		return peak;
	}
	
	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "DTSpan["+(int)(start*100) + "," +(int)(peak*100)+","+ (int)(stop*100)+"]";
	}
}
