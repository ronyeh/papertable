package cello.papertable.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JComponent;

import cello.papertable.event.PageEvent;
import cello.papertable.event.TableListener;
import cello.papertable.model.Page;
import cello.papertable.model.Table;

/**
 * A JComponent view of a Table object
 * 
 * @author Marcello
 */
public class TableView extends JComponent implements TableListener, 
MouseListener {

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
		addMouseListener(this);
		setBackground(new Color(0xFF000000));
		setOpaque(true);
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
		table.draw(g2d);
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
		Rectangle r = p.getPage().getShape().getBounds();
		r.width+=2;
		r.height+=2;
		repaint(r);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		List<Page> pages = table.getPages(
				new Rectangle2D.Float(e.getX(), e.getY(), 1, 1));
		for (Page p : pages) {
			p.setActive(true);
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		List<Page> pages = table.getPages();
		for (Page p : pages)
			p.setActive(false);
		
	}
	
	
}
