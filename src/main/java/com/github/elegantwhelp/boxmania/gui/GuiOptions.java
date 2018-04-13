package com.github.elegantwhelp.boxmania.gui;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.BoxMania;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetButton;
import com.github.elegantwhelp.boxmania.rendering.Sprite;

public class GuiOptions extends Gui {
	private BoxMania game;
	
	private Gui previousGui;
	
	private WidgetButton buttonBack;
	
	private WidgetButton buttonPlayerSkinSelectLeft;
	private WidgetButton buttonPlayerSkinSelectRight;
	
	public GuiOptions(BoxMania game, Gui previousGui) {
		super();
		this.game = game;
		this.previousGui = previousGui;
	}

	@Override
	public void closeGui() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openGui() {
		buttonBack = new WidgetButton(new Vector2f(0,300), new Vector2f(256, 16), "Back", 32, 0);
		buttonPlayerSkinSelectLeft = new WidgetButton(new Vector2f(-52, 0), new Vector2f(19, 19), "<", 32, 0);
		buttonPlayerSkinSelectRight = new WidgetButton(new Vector2f(52, 0), new Vector2f(19, 19), ">", 32, 0);
		addWidget(buttonBack);
		addWidget(buttonPlayerSkinSelectLeft);
		addWidget(buttonPlayerSkinSelectRight);
	}

	public void render(Sprite guiAssets) {
		super.render(guiAssets);
		
		game.getBoxSkins().drawSprite(-32f, -32f, 32f, 32f, game.getPlayerSelectedSkinIndex());
	}
	
	@Override
	public void updateGui() {
		if (buttonPlayerSkinSelectLeft.isClicked()) {
			int index = game.getPlayerSelectedSkinIndex()-1;
			if (index < 0) index = 0;
			game.setPlayerSelectedSkinIndex(index);
		}
		
		if (buttonPlayerSkinSelectRight.isClicked()) {
			int index = game.getPlayerSelectedSkinIndex()+1;
			if (index > 3) index = 3;
			game.setPlayerSelectedSkinIndex(index);
		}
		
		if (buttonBack.isClicked()) {
			guiHandler.setActiveGUI(previousGui);
		}
	}
}
