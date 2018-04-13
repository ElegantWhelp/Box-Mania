package com.github.elegantwhelp.boxmania.gui;

import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.rendering.Font;
import com.github.elegantwhelp.boxmania.rendering.Sprite;

public class GuiHandler {
	private Gui guiActive;
	
	private Sprite guiAssets;
	private Font gameFont;
	private Input input;
	
	public GuiHandler(Input input, Sprite guiAssets, Font gameFont) {
		this.guiAssets = guiAssets;
		this.gameFont = gameFont;
		this.input = input;
	}
	
	public void setActiveGUI(Gui gui) {
		if (guiActive != null) guiActive.closeGui();
		guiActive = gui;
		if (guiActive != null) {
			guiActive.setHandler(this);
			guiActive.openGui();
		}
	}
	
	public void updateGui(float delta) {
		if (guiActive != null) guiActive.update(delta);
	}
	
	public void renderGui() {
		if (guiActive != null) guiActive.render(guiAssets);
	}
	
	public Sprite getGuiAssets() { return guiAssets; }
	public Font getGameFont() { return gameFont; }
	public Input getInput() { return input; }
}
