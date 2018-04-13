package com.github.elegantwhelp.boxmania.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Input {
	// Info regarding the window
	Vector2f halfWindowSize;
	
	// Keyboard Input
	private KeyCallback keyCallback;
	
	private Map<String, Integer> stringToKey;
	Map<Integer, KeyReference> keyToKeyState;
	
	private List<Integer> keyHoldQueue;
	
	// Mouse Button Input
	private MouseButtonCallback mouseButtonCallback;
	
	List<KeyState> mouseButtonStates;
	private List<Integer> mouseButtonHoldQueue;
	
	// Cursor Position
	private CursorPositionCallback cursorPositionCallback;
	Vector2f cursorPosition;
	
	public Input(Vector2f windowSize) {
		this.halfWindowSize = windowSize;
		// Keyboard Input
		keyCallback = new KeyCallback(this);
		
		stringToKey = new HashMap<String, Integer>();
		keyToKeyState = new HashMap<Integer, KeyReference>();
		
		keyHoldQueue = new ArrayList<Integer>();
		
		
		// Mouse Button Input
		mouseButtonCallback = new MouseButtonCallback(this);
		
		mouseButtonStates = new ArrayList<KeyState>();
		
		for (int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			mouseButtonStates.add(KeyState.IDLE);
		}
		
		mouseButtonHoldQueue = new ArrayList<Integer>();
		
		// Cursor Position
		cursorPositionCallback = new CursorPositionCallback(this);
		cursorPosition = new Vector2f();
	}
	
	// Keyboard Input
	public KeyCallback getKeyCallback() { return keyCallback; }
	
	public void registerKey(String action, int key) {
		if (!stringToKey.containsKey(action)) {
			stringToKey.put(action, key);
			
			KeyReference keyRef;
			
			if (!keyToKeyState.containsKey(key)) {
				keyRef = new KeyReference();
				keyToKeyState.put(key, keyRef);
			} else {
				keyRef = keyToKeyState.get(key);
			}
			
			keyRef.keyReference++;
		}
	}
	
	public KeyState getKey(String action) {
		if (stringToKey.containsKey(action)) {
			int key = stringToKey.get(action);
			KeyState state =  keyToKeyState.get(key).keyState;

			if (state == KeyState.PRESSED) {
				if (!keyHoldQueue.contains(key)) {
					keyHoldQueue.add(key);
				}
			}
			
			return state;
		}
		return KeyState.IDLE;
	}
	
	public void unregisterKey(String action) {
		if (stringToKey.containsKey(action)) {
			int key = stringToKey.get(action);
			stringToKey.remove(action);
			
			KeyReference ref = keyToKeyState.get(key);
			ref.keyReference--;
			if (ref.keyReference <= 0)
				keyToKeyState.remove(key);
		}
	}
	
	public boolean isActionRegistered(String action) {
		return stringToKey.containsKey(action);
	}

	// Mouse Button Input
	
	public MouseButtonCallback getMouseButtonCallback() { return mouseButtonCallback; }
	
	public KeyState getMouseButton(int button) {
		if (button > 7 || button < 0)
			return KeyState.IDLE;
		
		KeyState state = mouseButtonStates.get(button);
		
		if (state == KeyState.PRESSED) {
			if (!mouseButtonHoldQueue.contains(button)) {
				mouseButtonHoldQueue.add(button);
			}
		}
		
		return state;
	}
	
	// Cursor Position
	
	public CursorPositionCallback getCursorPositionCallback() { return cursorPositionCallback; }
	
	public Vector2f getCursorPosition() { return cursorPosition; }
	
	public void update() {
		for (int key : keyHoldQueue) {
			keyToKeyState.get(key).keyState = KeyState.HOLD;
		}
		keyHoldQueue.clear();
		
		for (int button : mouseButtonHoldQueue) {
			mouseButtonStates.set(button, KeyState.HOLD);
		}
		mouseButtonHoldQueue.clear();
	}
	
}
