package com.github.elegantwhelp.boxmania.io;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonCallback extends GLFWMouseButtonCallback {
	private Input input;
	public MouseButtonCallback(Input input) {
		this.input = input;
	}
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		input.mouseButtonStates.set(button, CallbackUtil.getKeyState(action, input.mouseButtonStates.get(button)));
	}
}
