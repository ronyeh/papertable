package cello.papertable.ui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import cello.papertable.event.InputAggregator;
import cello.papertable.event.connect.MouseInputConnector;
import cello.papertable.event.connect.PenInputConnector;
import cello.papertable.model.PhotoPage;
import cello.papertable.model.Table;
import edu.stanford.hci.r3.pen.Pen;

/**
 * Main GUI window
 * @author Marcello
 */
public class MainFrame extends JFrame {
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -8431048183784216400L;
	
	private Table table; 
	private TableView tableview;
	private InputAggregator aggregator;

	
	/**
	 * Constructs a new MainFrame
	 *
	 */
	public MainFrame() {
		super("Papertable");
		setUndecorated(true);
		setSize(1024,768);
		
		table = new Table(1024,768);
		aggregator = new InputAggregator();
		
		float x = 10; 
		
		File folder = new File("photos");
		for (File f : folder.listFiles())
			if (f.isFile() && f.canRead()) {
				try {
					BufferedImage img = ImageIO.read(f);
					PhotoPage pp = new PhotoPage(img,x,x,300);
					pp.setAnchor(0.5f, 0.5f);
					pp.setRotation((float)Math.PI/8);
					
					table.add(pp);
					x += 150;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		
		
		tableview = new TableView(table);

		Pen p = new Pen("Local Pen");
		p.startLiveMode();
		
		MouseInputConnector mouseinput = new MouseInputConnector(tableview, aggregator);
		PenInputConnector peninput = new PenInputConnector(p, aggregator);
		
		tableview.setHandler(mouseinput, new TableStrokeHandler());
		tableview.setHandler(peninput, new TableStrokeHandler());

		aggregator.addInputListener(tableview);
		
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tableview,BorderLayout.CENTER);
		
		getContentPane().invalidate();
	}

}
