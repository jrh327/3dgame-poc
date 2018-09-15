package net.jonhopkins.game3d.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	private static final int KEY_COUNT = 256;
	private static final KeyboardInput instance = new KeyboardInput();
	
	private enum KeyState {
		RELEASED, // Not down
		PRESSED,  // Down, but not the first time
		ONCE      // Down for the first time
	}
	
	// Current state of the keyboard
	private boolean[] currentKeys = null;
	
	// Polled keyboard state
	private KeyState[] keys = null;
	
	private KeyboardInput() {
		currentKeys = new boolean[KEY_COUNT];
		keys = new KeyState[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; i++) {
			keys[i] = KeyState.RELEASED;
		}
	}
	
	public static KeyboardInput getInstance() {
		return instance;
	}
	
	public static void poll() {
		synchronized (instance) {
			for (int i = 0; i < KEY_COUNT; i++) {
				// Set the key state 
				if (instance.currentKeys[i]) {
					// If the key is down now, but was not
					// down last frame, set it to ONCE,
					// otherwise, set it to PRESSED
					if (instance.keys[i] == KeyState.RELEASED) {
						instance.keys[i] = KeyState.ONCE;
					} else {
						instance.keys[i] = KeyState.PRESSED;
					}
				} else {
					instance.keys[i] = KeyState.RELEASED;
				}
			}
		}
	}
	
	public static boolean keyDown(int keyCode) {
		if (keyCode < 0 || keyCode >= KEY_COUNT) {
			return false;
		}
		KeyState state = instance.keys[keyCode];
		return state == KeyState.ONCE || state == KeyState.PRESSED;
	}
	
	public static boolean keyDownOnce(int keyCode) {
		if (keyCode < 0 || keyCode >= KEY_COUNT) {
			return false;
		}
		return instance.keys[keyCode] == KeyState.ONCE;
	}
	
	@Override
	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode < 0 && keyCode >= KEY_COUNT) {
			return;
		}
		currentKeys[keyCode] = true;
	}
	
	@Override
	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode < 0 || keyCode >= KEY_COUNT) {
			return;
		}
		currentKeys[keyCode] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// Not needed
	}
}
