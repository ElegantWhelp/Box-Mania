package com.github.elegantwhelp.boxmania.entities;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.io.KeyState;

public class Player extends Entity {
	private Vector2f forcedPosition;
	public Player(Vector2f spawningPosition) {
		super(spawningPosition);
		forcedPosition = new Vector2f(spawningPosition);
	}
	@Override
	public void input(Input input) {
		if (input.getKey("moveUp") == KeyState.PRESSED) {
			forcedPosition.y--;
		}
		if (input.getKey("moveDown") == KeyState.PRESSED) {
			forcedPosition.y++;
		}
		if (input.getKey("moveLeft") == KeyState.PRESSED) {
			forcedPosition.x--;
		}
		if (input.getKey("moveRight") == KeyState.PRESSED) {
			forcedPosition.x++;
		}
	}

	@Override
	public void update(float delta) {
		position.lerp(forcedPosition, 20 * delta);
	}
	public Vector2f getForcedPosition() {
		return forcedPosition;
	}

}
