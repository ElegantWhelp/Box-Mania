package com.github.elegantwhelp.boxmania.gui.widget;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.gui.GuiHandler;
import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.io.KeyState;
import com.github.elegantwhelp.boxmania.util.BoundingBox;

public class WidgetButton extends Widget {
	private BoundingBox boundingBox;
	private int tile;
	private String label;
	private float labelScale;
	
	private boolean clicked;
	
	public WidgetButton(Vector2f location, Vector2f halfExtents, String label, float labelScale, int anchor) {
		super(location, anchor);
		boundingBox = new BoundingBox(location, halfExtents);
		tile = 0;
		clicked = false;
		this.label = label;
		this.labelScale = labelScale;
	}

	@Override
	public void renderWidget() {
		Vector2f halfExtents = boundingBox.getHalfExtents();
		float x = location.x - halfExtents.x;
		float y = location.y - halfExtents.y;
		float width = location.x + halfExtents.x;
		float height = location.y + halfExtents.y;
		
		GuiHandler handler = guiParent.getHandler();
		
		handler.getGuiAssets().drawSprite(x, y, width, height, tile, 0);
		handler.getGameFont().drawString(label, location.x - (handler.getGameFont().getWidthOfText(label, labelScale) * 0.5f), location.y - (labelScale * 0.5f), labelScale);
	}

	@Override
	public void updateWidget(float delta) {
		Input input = guiParent.getHandler().getInput();
		Vector2f cursorPos = input.getCursorPosition();
		
		if (boundingBox.isIntersecting(cursorPos)) {
			tile = 1;
			
			if (input.getMouseButton(0) == KeyState.PRESSED) {
				clicked = true;
			} else {
				clicked = false;
			}
		} else {
			tile = 0;

			if (clicked)
				clicked = false;
		}
	}
	
	public void setText(String label) { this.label = label; }
	public void setTextScale(float labelScale) { this.labelScale = labelScale; }
	public boolean isClicked() { return clicked; }
}
