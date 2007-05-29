package cello.papertable.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import cello.papertable.model.Page;
import cello.papertable.model.Table;
import edu.stanford.hci.r3.PaperToolkit;
import edu.stanford.hci.r3.application.Application;
import edu.stanford.hci.r3.pen.Pen;
import edu.stanford.hci.r3.pen.PenSample;
import edu.stanford.hci.r3.pen.streaming.listeners.PenListener;

/**
 * Main GUI window
 * @author Marcello
 */
public class MainFrame extends JFrame implements PenListener {
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -8431048183784216400L;
	
	private Table table; 
	private TableView tableview;

	private boolean calibrated = false;
	private double minX,minY,maxX,maxY; 
	
	/**
	 * Constructs a new MainFrame
	 *
	 */
	public MainFrame() {
		super("Papertable");
		setSize(1024,768);
		
		table = new Table(1024,768);
		
		table.add(new Page(10,10,300,300));
		tableview = new TableView(table);


		Pen p = new Pen("Local Pen");
		p.startLiveMode();
		p.addLivePenListener(this);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tableview,BorderLayout.CENTER);
		
		getContentPane().invalidate();
	}

	public void penDown(PenSample sample) {
		if (!calibrated) {
			minX = maxX = sample.getX();
			minY = maxY = sample.getY();
		}
		sample(sample);
	}

	public void penUp(PenSample sample) {
		sample(sample);
		calibrated = true;
	}

	public void sample(PenSample sample) {
		if (!calibrated) {
			System.out.println("sample="+sample);
			minX = Math.min(minX, sample.getX());
			maxX = Math.max(maxX, sample.getX());
			minY = Math.min(minY, sample.getY());
			maxY = Math.max(maxY, sample.getY());
		} else {
			double scaledX = (sample.getX() - minX) / (maxX-minX);
			double scaledY = (sample.getY() - minY) / (maxY-minY);
			System.out.println("sample = "+scaledX+","+scaledY);
		}
	}

}
