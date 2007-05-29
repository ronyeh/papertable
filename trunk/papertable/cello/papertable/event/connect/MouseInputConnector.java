package cello.papertable.event.connect;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import cello.papertable.event.InputListener;
import cello.papertable.event.PointEvent;


/**
 * Takes mouse input from a Component and generates PointEvents for an 
 * InputListener.
 * 
 * @author Marcello
 */
public class MouseInputConnector implements MouseListener, MouseMotionListener {
	
	
	private Component c;
	/**
	 * Constructs a new MouseInputAdapter with a given component and listener
	 * @param c the component to listen on 
	 * @param listener the listener to send events to 
	 */
	public MouseInputConnector(Component c, InputListener listener) {
		this.c = c;
		this.listener = listener;
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
	}
	
	private InputListener listener = null;
	
	private PointEvent getEvent(PointEvent.Type type, MouseEvent e, float pressure) {
		return new PointEvent(this,type,
				(float)e.getX()/c.getWidth(),
				(float)e.getY()/c.getHeight(),pressure);
	}

	/** @see MouseListener#mousePressed(java.awt.event.MouseEvent) */
	public void mousePressed(MouseEvent e) {
		listener.inputPoint(getEvent(PointEvent.Type.PRESS,e,1.0f));
				
	}

	/** @see MouseListener#mouseReleased(java.awt.event.MouseEvent) */
	public void mouseReleased(MouseEvent e) {
		listener.inputPoint(getEvent(PointEvent.Type.RELEASE,e,0));
	}

	/** @see MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) */
	public void mouseDragged(MouseEvent e) {
		listener.inputPoint(getEvent(PointEvent.Type.DRAG,e,1.0f));
	}

	/** @see MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) */
	public void mouseMoved(MouseEvent e) {
		listener.inputPoint(getEvent(PointEvent.Type.HOVER,e,0));
	}

	/** @see MouseListener#mouseClicked(java.awt.event.MouseEvent) */
	public void mouseClicked(MouseEvent e) { /**/ }
	/** @see MouseListener#mouseEntered(java.awt.event.MouseEvent) */
	public void mouseEntered(MouseEvent e) { /**/ }
	/** @see MouseListener#mouseExited(java.awt.event.MouseEvent) */
	public void mouseExited(MouseEvent e) { /**/ }
	
	
}
