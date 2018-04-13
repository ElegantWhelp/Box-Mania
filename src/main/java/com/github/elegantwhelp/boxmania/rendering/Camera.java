package com.github.elegantwhelp.boxmania.rendering;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
	private Matrix4f projection;
	private Matrix4f transform;
	
	private Vector2f position;
	
	public Camera() {
		projection = new Matrix4f();
		transform = new Matrix4f();
		position = new Vector2f();
	}
	
	public void setProjection(float left, float right, float bottom, float top) {
		projection.setOrtho(left, right, bottom, top, 1, -1);
	}
	
	public void setPosition(float x, float y) { position.set(x, y); }
	public void setPosition(Vector2f vector) { position.set(vector); }
	public Vector2f getPosition() { return position; }
	
	public Matrix4f getProjection() { return projection; }
	public Matrix4f getTransform() { return transform.setTranslation(position.x, position.y, 0); }
}
