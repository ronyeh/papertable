package cello.papertable.ui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import papertoolkit.PaperToolkit;
import papertoolkit.application.Application;
import papertoolkit.paper.Region;
import papertoolkit.paper.Sheet;
import papertoolkit.paper.regions.TextRegion;
import papertoolkit.pen.Pen;
import papertoolkit.units.Centimeters;

import cello.papertable.dt.TouchDispatcher;
import cello.papertable.event.InputAggregator;
import cello.papertable.event.connect.MouseInputConnector;
import cello.papertable.event.connect.PenRegionInputConnector;
import cello.papertable.event.connect.TouchInputConnector;
import cello.papertable.model.PhotoPage;
import cello.papertable.model.Table;

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
		
		int widthTable = 1280;
		int heightTable = 1024;
		
		setSize(widthTable,heightTable);
		setLocation(1920,0);
		
		table = new Table(widthTable,heightTable);
		aggregator = new InputAggregator();
		
		float x = 10; 
		
		File folder = new File("photos");
		for (File f : folder.listFiles())
			if (f.isFile() && f.canRead()) {
				try {
					BufferedImage img = ImageIO.read(f);
					PhotoPage pp = new PhotoPage(img,x,x,300);
					//pp.rotate((float)Math.PI/8, x+150,x+100);
					//pp.addConstraint(table, new Point2D.Double(x+10,x+10));
					
					table.add(pp);
					x += 150;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		
		
		tableview = new TableView(table);

		Pen p = new Pen("Local Pen");
		p.startLiveMode();

		TouchDispatcher dispatcher = new TouchDispatcher();
		dispatcher.start();
		
		MouseInputConnector mouseinput = new MouseInputConnector(tableview, 
				aggregator);
		

		Centimeters centimeters = new Centimeters();
		Sheet surface = new Sheet(65.5, 49.3, centimeters);
		Region textRegion = new TextRegion("Top Left", 0, 0);
		surface.addRegion(textRegion);
		Region mainRegion = new Region("Main Region", 0, 0, 66, 49.6, 
				centimeters);

		PenRegionInputConnector peninput = new PenRegionInputConnector(mainRegion, 
				aggregator);
		
		surface.addRegion(mainRegion);
		
		
		Application app = new Application("DiamondTouchTableTop");
		app.addSheet(surface, new File("data/DiamondTouch.patternInfo.xml"));

		PaperToolkit.runApplication(app);
		
		
		
		TouchInputConnector touchinput = new TouchInputConnector(dispatcher, 
				aggregator);
		
		tableview.setHandler(mouseinput, new TableManipulateHandler());
		tableview.setHandler(peninput, new TableStrokeHandler());
		tableview.setHandler(touchinput, new TableManipulateHandler());

		aggregator.addInputListener(tableview);
		
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tableview,BorderLayout.CENTER);
		
		getContentPane().invalidate();
	}

}
