package com.github.elegantwhelp.boxmania.gui.widget;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.gui.Gui;

public abstract class Widget {
	// TODO Implement Anchoring Later
	public static final int ANCHOR_LEFT = (1 << 0);
	public static final int ANCHOR_RIGHT = (1 << 1);
	public static final int ANCHOR_UP = (1 << 2);
	public static final int ANCHOR_DOWN = (1 << 3);
	
	protected Vector2f location;
	protected Gui guiParent;
	private int anchorPoints;
	
	/**
	 * Creates the base of the widget and it's location.
	 * Anchor uses a maximum of two anchor flags (any more will cause glitches) and
	 * anchors the widget to a side of the screen.
	 * @param location
	 * @param anchor
	 */
	public Widget(Vector2f location, int anchor) {
		this.anchorPoints = anchor;
		this.location = new Vector2f(location);
	}
	
	public void setGui(Gui gui) {
		this.guiParent = gui;
	}
	
	public abstract void renderWidget();
	public abstract void updateWidget(float delta);
	
}
