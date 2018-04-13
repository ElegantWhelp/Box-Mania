package com.github.elegantwhelp.boxmania.gui;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.gui.widget.WidgetButton;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetLabel;

public class GuiTest extends Gui {
	private WidgetButton button;
	
	public GuiTest() { 
		super();
	}
	@Override
	public void closeGui() {
	}

	@Override
	public void openGui() {
		button = new WidgetButton(new Vector2f(-300,0), new Vector2f(128,32), "Hell Naw!", 32, 0);
		addWidget(new WidgetLabel(new Vector2f(0, -350), "BoxMania\nVersion 0.0.1\nTest GUI", 32, WidgetLabel.ALIGN_RIGHT, 0));
		addWidget(button);
	}
	@Override
	public void updateGui() {
		if (button.isClicked()) {
			button.setText("Hell Yea!");
		}
	}
	
}
