package com.github.elegantwhelp.boxmania.gui;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.BoxMania;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetButton;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetLabel;
import com.github.elegantwhelp.boxmania.io.KeyState;

public class GuiMain extends Gui {
	private WidgetButton buttonPlay;
	private WidgetButton buttonMultiplayer;
	private WidgetButton buttonOptions;
	private WidgetButton buttonQuit;
	
	private BoxMania game;
	
	public GuiMain(BoxMania game) {
		super();
		this.game = game;
	}
	
	
	@Override
	public void closeGui() {
		
	}

	@Override
	public void openGui() {
		buttonPlay = new WidgetButton(new Vector2f(0, 0), new Vector2f(256, 16), "Play", 32, 0);
		buttonMultiplayer = new WidgetButton(new Vector2f(0, 50), new Vector2f(256, 16), "Multiplayer", 32, 0);
		buttonOptions = new WidgetButton(new Vector2f(0, 100), new Vector2f(256, 16), "Options", 32, 0);
		buttonQuit = new WidgetButton(new Vector2f(0, 150), new Vector2f(256, 16), "Quit", 32, 0);
		addWidget(new WidgetLabel(new Vector2f(0, -200), "Box Mania", 128, WidgetLabel.ALIGN_CENTER, 0));
		addWidget(new WidgetLabel(new Vector2f(0, -100), "A Game By Richard Olsen", 32, WidgetLabel.ALIGN_CENTER, 0));
		addWidget(buttonPlay);
		addWidget(buttonMultiplayer);
		addWidget(buttonOptions);
		addWidget(buttonQuit);
	}

	@Override
	public void updateGui() {
		if (buttonPlay.isClicked())
			game.startGame();
		
		if (buttonOptions.isClicked()) 
			guiHandler.setActiveGUI(new GuiOptions(game, this));
		
		if (buttonQuit.isClicked() || guiHandler.getInput().getKey("quit") == KeyState.PRESSED)
			System.exit(0);
	}

}
