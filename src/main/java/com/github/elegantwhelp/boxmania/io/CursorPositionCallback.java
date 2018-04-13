package com.github.elegantwhelp.boxmania.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPositionCallback extends GLFWCursorPosCallback {
	private Input input;
	public CursorPositionCallback(Input input) {
		this.input = input;
	}
	@Override
	public void invoke(long window, double xpos, double ypos) {
		input.cursorPosition.set((float)xpos, (float)ypos);
		input.cursorPosition.sub(input.halfWindowSize);
	}
}
