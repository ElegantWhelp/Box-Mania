package com.github.elegantwhelp.boxmania.gui.widget;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.rendering.Font;

public class WidgetLabel extends Widget {
	public static final int ALIGN_CENTER = (1 << 0);
	public static final int ALIGN_RIGHT = (1 << 2);
	public static final int ALIGN_LEFT = (1 << 1);
	
	private String label;
	private float scale;
	private int align;
	
	public WidgetLabel(Vector2f location, String label, float scale, int align, int anchor) {
		super(location, anchor);
		
		this.label = label;
		this.scale = scale;
		this.align = align;
	}
	
	@Override
	public void renderWidget() {
		Font font = guiParent.getHandler().getGameFont();
		if ((align & ALIGN_CENTER) != 0) {
			float locationModifier = font.getWidthOfText(label, scale) * 0.5f;
			
			font.drawString(label, location.x - locationModifier, location.y, scale);
		}
		else if ((align & ALIGN_RIGHT) != 0) {
			float locationModifier = font.getWidthOfText(label, scale);
			
			font.drawString(label, location.x - locationModifier, location.y, scale);
		} else {
			font.drawString(label, location.x, location.y, scale);
		}
	}

	@Override
	public void updateWidget(float delta) { }
	
	public void setText(String label) {
		this.label = label;
	}
	
	public void setAlign(int align) {
		this.align = align;
	}
}
