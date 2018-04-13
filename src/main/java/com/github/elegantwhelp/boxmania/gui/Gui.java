package com.github.elegantwhelp.boxmania.gui;

import java.util.ArrayList;
import java.util.List;

import com.github.elegantwhelp.boxmania.gui.widget.Widget;
import com.github.elegantwhelp.boxmania.rendering.Sprite;

public abstract class Gui {
	protected GuiHandler guiHandler;
	
	private List<Widget> widgets;
	
	public Gui() {
		widgets = new ArrayList<Widget>();
	}
	
	public void update(float delta) {
		for (Widget widget : widgets) {
			widget.updateWidget(delta);
		}
		updateGui();
	}
	
	public void render(Sprite guiAssets) {
		for (Widget widget : widgets) {
			widget.renderWidget();
		}
	}
	
	public void close() {
		widgets.clear();
	}
	
	public abstract void closeGui();
	public abstract void openGui();
	public abstract void updateGui();
	
	public void addWidget(Widget widget) {
		widget.setGui(this);
		widgets.add(widget);
	}
	
	public List<Widget> getWidgets() { return widgets; }
	public GuiHandler getHandler() { return guiHandler; }

	public void setHandler(GuiHandler guiHandler) { this.guiHandler = guiHandler; }
}
