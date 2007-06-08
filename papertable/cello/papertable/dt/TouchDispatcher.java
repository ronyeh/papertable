package cello.papertable.dt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.merl.diamondtouch.DtioDeviceId;
import com.merl.diamondtouch.DtlibDevice;
import com.merl.diamondtouch.DtlibException;
import com.merl.diamondtouch.DtlibInputTouch;
import com.merl.diamondtouch.DtlibSegment;

/**
 * Dispatcher for DiamondTouch events.  Based on code by Ron Yeh.
 * 
 * 
 * @author Marcello
 *
 */

public class TouchDispatcher implements Runnable {

	static {
		System.loadLibrary("jdt");
	}

	private DtlibDevice device = null;
	private List<TouchListener> listeners = new LinkedList<TouchListener>();
	private boolean running = true;
	

	/**
	 * Constructs a new dispatcher
	 */
	public TouchDispatcher() {
		//
	}

	/**
	 * Frees resource
	 */
	@Override
	public void finalize() {
		device.setApplyTransform(false);
		device.stop();
	}
	
	/**
	 * Starts the dispatcher
	 */
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * @see Runnable#run()
	 */
	@Override
	public void run() {
		try {
			initialize();

			while (running) {
				pollTable();
				Thread.yield();
			}
		} catch (DtlibException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Stops 
	 */
	public void stop() {
		running = false;
	}

	private void initialize() {
		// Get device ids for known devices.
		long[] deviceIds = DtlibDevice.getDeviceIds();

		// Find an available device.
		boolean found = false;
		System.out.println("Devices:");
		for (int k = 0; k < deviceIds.length; k++) {
			System.out.println("    " + DtioDeviceId.toString(deviceIds[k]));
			if (!found) {
				device = DtlibDevice.getDevice(deviceIds[k]);
				if (device.getStatus() != DtlibDevice.STATUS_UNAVAILABLE)
					found = true;
			}
		}
		if (!found)
			throw new RuntimeException("Could not find DiamondTouch");
		System.out.println("Using DiamondTouch device " + 
				DtioDeviceId.toString(device.getId()));

		// Do automatic coordinate transformation
		device.setApplyTransform(true);
		
		// We want to read DtlibInputTouch objects.
		if (device.start(DtlibInputTouch.getClassInputclass()) != 0)
			System.err.println("JdtTest.runapp: start failed");


	}

	private void pollTable() throws DtlibException {
		// We know that we're reading DtlibInputTouch objects.
		DtlibInputTouch[] inputs = null;
		
		inputs = (DtlibInputTouch[]) device.read();

		if (inputs == null) {
			// Does this even happen?
			System.err.println("input error (Does this happen?)");
			return;
		}
		

		final double MAX_X = 2048;
		final double MAX_Y = 1536;

		// for each user (four)
		for (int user = 0; user < inputs.length; user++) {
			DtlibInputTouch input = inputs[user];
			if (input != null) {
				List<DTSpan> xSpans = new ArrayList<DTSpan>(),
							 ySpans = new ArrayList<DTSpan>();
				
				for (DtlibSegment x : input.getXSegments())
					xSpans.add(new DTSpan(x.startPos/MAX_X,x.stopPos/MAX_X,x.maxSignalPos/MAX_X));
				
				for (DtlibSegment y : input.getYSegments())
					ySpans.add(new DTSpan(y.startPos/MAX_Y,y.stopPos/MAX_Y,y.maxSignalPos/MAX_Y));

				// Get type
				TouchEvent.Type type = TouchEvent.Type.NONE;
				switch (input.getType()) {
					case DtlibInputTouch.DOWN:
						type = TouchEvent.Type.DOWN;
						break;
					case DtlibInputTouch.UP:
						type = TouchEvent.Type.UP;
						break;
					case DtlibInputTouch.MOVE:
						type = TouchEvent.Type.MOVE;
						break;
				}
				
				TouchEvent ev = new TouchEvent(user, type, xSpans, ySpans);

				for (TouchListener listener : listeners)
					listener.handleTouch(ev);

				// You can wait for garbage collection, but...
				// [is this even necessary? -Marcello]
				input.free();
				
			}
		}

		
	}

	/**
	 * Adds a new listener to this dispatcher
	 * @param listener
	 */
	public void addTouchListener(TouchListener listener) {
		listeners.add(listener);
	}
	/**
	 * Removes a listener from this dispatcher
	 * @param listener
	 */
	public void removeTouchListener(TouchListener listener) {
		listeners.remove(listener);
	}
}

