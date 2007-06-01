package cello.papertable.dt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.merl.diamondtouch.DtioDeviceId;
import com.merl.diamondtouch.DtlibDevice;
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
		initialize();

		while (running) {
			pollTable();
			Thread.yield();
		}
	}


	/**
	 * Stops 
	 */
	public void stop() {
		running = false;
	}

	private void initialize() {
		try {
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
			if (!found) {
				System.out.println("JdtTest.runapp: no device");
				return;
			}
			System.out.println("JdtTest.runapp: using device " + 
					DtioDeviceId.toString(device.getId()));

			// Do automatic coordinate transformation
			device.setApplyTransform(true);
			
			// We want to read DtlibInputTouch objects.
			if (device.start(DtlibInputTouch.getClassInputclass()) != 0)
				System.err.println("JdtTest.runapp: start failed");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pollTable() {
		// We know that we're reading DtlibInputTouch objects.
		DtlibInputTouch[] inputs = null;
		
		try {
			inputs = (DtlibInputTouch[]) device.read();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (inputs == null) {
			// Does this even happen?
			System.err.println("input error (Does this happen?)");
			return;
		}

		// for each user (four)
		for (int user = 0; user < inputs.length; user++) {
			DtlibInputTouch input = inputs[user];
			if (input != null) {
				List<DTSpan> xSpans = new ArrayList<DTSpan>(),
							 ySpans = new ArrayList<DTSpan>();
				
				for (DtlibSegment x : input.getXSegments())
					xSpans.add(new DTSpan(x.startPos,x.stopPos,x.maxSignalPos));
				
				for (DtlibSegment y : input.getYSegments())
					ySpans.add(new DTSpan(y.startPos,y.stopPos,y.maxSignalPos));

				// You can wait for garbage collection, but...
				// [is this even necessary? -Marcello]
				input.free();
				
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

