package com.github.elegantwhelp.boxmania.entities;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.util.Timer;

public class Point extends Entity {
	private int score;
	private double decayTime;
	private Timer decayTimer;
	
	public Point(Vector2f position, int score, double decayTime) {
		super(position);
		this.score = score;
		this.decayTime = decayTime;
		this.decayTimer = new Timer();
		this.decayTimer.start();
	}

	@Override
	public void input(Input input) {} // Points will not use an input.

	@Override
	public void update(float delta) {} // Points don't have an update either.
	
	public int getScore() { return score; }
	public double getDecayTime() { return decayTime; }
	public boolean shouldDecay() { return decayTimer.getElapsedTime() >= decayTime; }
}
