package com.github.elegantwhelp.boxmania.io;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyCallback extends GLFWKeyCallback {
	private Input input;
	
	public KeyCallback(Input input) {
		this.input = input;
	}
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (input.keyToKeyState.containsKey(key)) {
			input.keyToKeyState.get(key).keyState = CallbackUtil.getKeyState(action, input.keyToKeyState.get(key).keyState);
		}
	}
}
