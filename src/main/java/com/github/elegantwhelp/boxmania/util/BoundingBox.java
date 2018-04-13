package com.github.elegantwhelp.boxmania.util;

import org.joml.Vector2f;

public class BoundingBox {
	private Vector2f center;
	private Vector2f halfExtents;
	private Vector2f temporary; // Used for calculations
	
	public BoundingBox(Vector2f center, Vector2f halfExtents) {
		this.center = center;
		this.halfExtents = halfExtents;
		this.temporary = new Vector2f();
	}
	
	public boolean isIntersecting(BoundingBox other) {
		temporary.set(other.center);
		temporary.sub(center);
		
		float distance = temporary.lengthSquared() - halfExtents.lengthSquared() - other.halfExtents.lengthSquared();
		if (distance <= 0)
			return true;
		return false;
	}
	
	public boolean isIntersecting(Vector2f point) {
		temporary.set(point);
		temporary.sub(center);
		
		temporary.x = (float) Math.abs(temporary.x);
		temporary.y = (float) Math.abs(temporary.y);
		
		if (temporary.x <= halfExtents.x && temporary.y <= halfExtents.y)
			return true;
		return false;
	}

	public Vector2f getCenter() { return center; }
	public Vector2f getHalfExtents() { return halfExtents; }
}
