package com.github.elegantwhelp.boxmania.io;

import org.lwjgl.glfw.GLFW;

public class CallbackUtil {
	public static KeyState getKeyState(int action, KeyState currentState) {
		if (action == GLFW.GLFW_PRESS && currentState == KeyState.IDLE) {
			return KeyState.PRESSED;
		}
		if (action == GLFW.GLFW_RELEASE)
			return KeyState.IDLE;
		return currentState;
	}
}
