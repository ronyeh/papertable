package cello.papertable.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import cello.papertable.event.InputListener;
import cello.papertable.event.PageEvent;
import cello.papertable.event.PointEvent;
import cello.papertable.event.TableListener;
import cello.papertable.model.Table;

/**
 * A JComponent view of a Table object
 * 
 * @author Marcello
 */
public class TableView extends JComponent 
	implements TableListener, InputListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6636161939556412231L;
	
	
	private Table table;
	
	/**
	 * Constructs a new TableView
	 * @param table
	 */
	public TableView(Table table) {
		this.table = table;
		table.addTableListener(this);
		setBackground(new Color(0xFF000000));
		setOpaque(true);
	}
	
	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,getWidth(),getHeight());
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
		table.draw(g2d);
		for (TableInputHandler handler : inputHandlers.values())
			handler.paint(g2d);
	}

	/**
	 * Triggered when a page is added
	 * @param p the page event
	 */
	public void pageAdded(PageEvent p) {
		pageChanged(p);
	}

	/**
	 * Triggered when a page is changed
	 * @param p the page event
	 */
	public void pageChanged(PageEvent p) {
		Rectangle r = p.getBounds2D().getBounds();
		r.width+=2;
		r.height+=2;
		repaint(r);
	}
	
	/**
	 * 
	 */
	@Override
	public void repaint(final Rectangle r) {
		super.repaint(r.x-2, r.y-2, r.width+4, r.height+4);
	}
	
	private Map<Object,TableInputHandler> inputHandlers 
									= new HashMap<Object,TableInputHandler>();

	/**
	 * 
	 * @param source
	 * @param handler
	 */
	public void setHandler(Object source, TableInputHandler handler) {
		inputHandlers.put(source,handler);
		handler.setTableView(this);
	}
	
	/** @see InputListener#inputPoint(PointEvent) */
	public void inputPoint(PointEvent e) {
		TableInputHandler handler = inputHandlers.get(e.getSource());
		
		if (handler!=null)
			handler.inputPoint(e);
	}
}
