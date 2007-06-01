package cello.papertable.event;

import java.util.EventObject;

/**
 * Point event class
 * @author Marcello
 *
 */
public class PointEvent extends EventObject {

	private static final long serialVersionUID = -197277793111705069L;
	private double x,y,radius,pressure;
	private Type type;
	private boolean consumed = false;
	
	/**
	 * Type of event
	 */
	public enum Type {
		/** Hover state */
		HOVER,
		/** Press state */
		PRESS,
		/** Drag state */
		DRAG,
		/** Release state */
		RELEASE
	}

	/**
	 * @param source
	 * @param type
	 * @param x
	 * @param y
	 */
	public PointEvent(Object source, Type type, double x, double y) {
		this(source,type,x,y,1.0,1.0);
	}
	/**
	 * @param source
	 * @param type
	 * @param x
	 * @param y
	 * @param radius
	 * @param pressure
	 */
	public PointEvent(Object source, Type type, double x, double y,
					double pressure) {
		this(source,type,x,y,pressure,1.0);
	}

	/**
	 * @param source
	 * @param type
	 * @param x
	 * @param y
	 * @param radius
	 * @param pressure
	 */
	public PointEvent(Object source, Type type, double x, double y, 
			double pressure, double radius) {
		super(source);
		this.type = type;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.pressure = pressure;
	}

	/**
	 * @return the type of this event
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @return the pressure (0-1)
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @return the x position
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y position
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return whether the event has been consumed
	 */
	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Consume this event
	 */
	public void consume() {
		this.consumed = true;
	}
	
	/** @see Object#toString */
	@Override
	public String toString() {
		return "PointEvent[type="+type+",x="+x+",y="+y+",source="+source+",pressure="+
				pressure+"]";
	}
	
}
