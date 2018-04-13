package com.github.elegantwhelp.boxmania.entities;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.io.Input;

public abstract class Entity {
	protected Vector2f position;
	
	public Entity(Vector2f spawningPosition) {
		position = new Vector2f(spawningPosition);
	}
	
	public abstract void input(Input input);
	public abstract void update(float delta);
	
	public Vector2f getPosition() { return this.position; }
	public void setPosition(float x, float y) { this.position.set(x, y); }
	public void setPosition(Vector2f position) { this.position.set(position); }
}
