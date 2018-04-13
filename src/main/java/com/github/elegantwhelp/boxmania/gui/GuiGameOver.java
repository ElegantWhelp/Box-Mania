package com.github.elegantwhelp.boxmania.gui;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.BoxMania;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetButton;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetLabel;

public class GuiGameOver extends Gui {
	private BoxMania game;
	private int finalScore;
	
	private WidgetButton buttonRetry;
	private WidgetButton buttonOptions;
	private WidgetButton buttonBackToMainMenu;
	
	public GuiGameOver(BoxMania game, int score) {
		super();
		this.game = game;
		this.finalScore = score;
	}

	@Override
	public void closeGui() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openGui() {
		addWidget(new WidgetLabel(new Vector2f(0, -200), "Time's Up!", 32, WidgetLabel.ALIGN_CENTER, 0));
		addWidget(new WidgetLabel(new Vector2f(0, -174), "Your Score Was " + finalScore, 32, WidgetLabel.ALIGN_CENTER, 0));
		
		buttonRetry = new WidgetButton(new Vector2f(0, 100), new Vector2f(256, 16), "Retry", 32, 0);
		buttonOptions = new WidgetButton(new Vector2f(0, 150), new Vector2f(256, 16), "Options", 32, 0);
		buttonBackToMainMenu = new WidgetButton(new Vector2f(0, 200), new Vector2f(256, 16), "Main Menu", 32, 0);
		
		addWidget(buttonRetry);
		addWidget(buttonOptions);
		addWidget(buttonBackToMainMenu);
	}

	@Override
	public void updateGui() {
		if (buttonRetry.isClicked())
			game.startGame();
		
		if (buttonOptions.isClicked())
			guiHandler.setActiveGUI(new GuiOptions(game, this));
		
		if (buttonBackToMainMenu.isClicked())
			guiHandler.setActiveGUI(new GuiMain(game));
	}
}
