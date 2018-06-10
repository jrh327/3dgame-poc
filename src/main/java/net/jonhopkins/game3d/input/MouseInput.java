package net.jonhopkins.game3d.input;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//import javax.swing.SwingUtilities;

public class MouseInput implements MouseListener, MouseMotionListener {
	public static final int LEFT_BUTTON = 1;
	public static final int MIDDLE_BUTTON = 2;
	public static final int RIGHT_BUTTON = 3;
	private static final int BUTTON_COUNT = 3;
	// Used for relative movement
	private int dx, dy;
	// Used to re-center the mouse
	//private Robot robot = null;
	// Convert coordinates from component to screen
	//private Component component;
	// The center of the component
	private Point center;
	// Is this relative or absolute
	private boolean relative;
	// Polled position of the mouse cursor
	private Point mousePos = null;
	// Current position of the mouse cursor
	private Point currentPos = null;
	// Current state of mouse buttons
	private boolean[] state = null;
	// Polled mouse buttons
	private MouseState[] poll = null;
	
	private static final MouseInput instance = new MouseInput();
	
	private enum MouseState {
		RELEASED, // Not down
		PRESSED,  // Down, but not the first time
		ONCE      // Down for the first time
	}
	
	private MouseInput() {
		/*try {
			robot = new Robot();
		} catch (Exception e) {
			// Handle exception [game specific]
		}
		*/
		// Create default mouse positions
		mousePos = new Point(0, 0);
		currentPos = new Point(0, 0);
		// Setup initial button states
		state = new boolean[BUTTON_COUNT];
		poll = new MouseState[BUTTON_COUNT];
		for (int i = 0; i < BUTTON_COUNT; i++) {
			poll[i] = MouseState.RELEASED;
		}
	}
	
	public static void setComponent(Component component) {
		// Need the component object to convert screen coordinates 
		//this.component = component;
		// Calculate the component center
		int w = component.getBounds().width;
		int h = component.getBounds().height;
		instance.center = new Point(w / 2, h / 2);
	}
	
	public static MouseInput getInstance() {
		return instance;
	}
	
	public static void poll() {
		synchronized (instance) {
			if (isRelative()) {
				instance.mousePos = new Point(instance.dx, instance.dy);
				instance.dx = 0;
				instance.dy = 0;
			} else {
				instance.mousePos = new Point(instance.currentPos);
			}
			
			for (int i = 0; i < BUTTON_COUNT; i++) {
				if (instance.state[i]) {
					if (instance.poll[i] == MouseState.RELEASED) {
						instance.poll[i] = MouseState.ONCE;
					} else {
						instance.poll[i] = MouseState.PRESSED;
					}
				} else {
					instance.poll[i] = MouseState.RELEASED;
				}
			}
		}
	}
	
	public static boolean isRelative() {
		return instance.relative;
	}
	
	public static void setRelative(boolean relative) {
		instance.relative = relative;
		if (relative) {
			centerMouse();
			instance.dx = 0;
			instance.dy = 0;
		}
	}
	
	public static Point getPosition() {
		return instance.mousePos;
	}
	
	public static boolean buttonDownOnce(int button) {
		return instance.poll[button - 1] == MouseState.ONCE;
	}
	
	public static boolean buttonDown(int button) {
		return instance.poll[button - 1] == MouseState.ONCE ||
				instance.poll[button - 1] == MouseState.PRESSED;
	}
	
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		state[e.getButton() - 1] = true;
	}
	
	@Override
	public synchronized void mouseReleased(MouseEvent e) {
		state[e.getButton() - 1] = false;
	}
	
	@Override
	public synchronized void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public synchronized void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public synchronized void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		if (isRelative()) {
			Point p = e.getPoint();
			dx += p.x - center.x;
			dy += p.y - center.y;
			centerMouse();
		} else {
			currentPos = e.getPoint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// Not needed
	}
	
	private static void centerMouse() {
		/*if (robot != null && component.isShowing()) {
			// Because the convertPointToScreen method 
			// changes the object, make a copy!
			Point copy = new Point(center.x, center.y);
			SwingUtilities.convertPointToScreen(copy, component);
			robot.mouseMove(copy.x, copy.y);
		}
		*/
	}
	
	public static void disableCursor() {
		/*Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Point point = new Point(0, 0);
		String name = "CanBeAnything";
		Cursor cursor = tk.createCustomCursor(image, point, name); 
		component.setCursor(cursor);
		*/
	}
	
	public static void enableCursor() {
		//component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
