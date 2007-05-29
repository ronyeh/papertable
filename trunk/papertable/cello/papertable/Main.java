package cello.papertable;

import javax.swing.JFrame;

import cello.papertable.ui.MainFrame;

/**
 * Main entry class
 * 
 * @author Marcello
 *
 */
public class Main {

	/**
	 * Main entry point
	 * @param args commandline arguments
	 */
	public static void main(String args[]) {
		MainFrame m = new MainFrame();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.setVisible(true);
	}
}
