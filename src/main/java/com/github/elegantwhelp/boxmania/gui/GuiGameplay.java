package com.github.elegantwhelp.boxmania.gui;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.BoxMania;
import com.github.elegantwhelp.boxmania.gameplay.Scene;
import com.github.elegantwhelp.boxmania.gui.widget.WidgetLabel;

public class GuiGameplay extends Gui {
	private BoxMania game;
	private Scene gameplay;
	
	private WidgetLabel countDownLabel;
	private WidgetLabel timeLeftLabel;
	private WidgetLabel scoreLabel;
	
	public GuiGameplay(BoxMania game, Scene gameplay) {
		this.game = game;
		this.gameplay = gameplay;
	}

	@Override
	public void closeGui() {
		
	}

	@Override
	public void openGui() {
		countDownLabel = new WidgetLabel(new Vector2f(0,-31.5f), "", 64, WidgetLabel.ALIGN_CENTER, 0);
		timeLeftLabel = new WidgetLabel(new Vector2f(0, -350), "Time Left: 10 Seconds", 32, WidgetLabel.ALIGN_CENTER, 0);
		scoreLabel = new WidgetLabel(new Vector2f(-630, -350), "Your Score: 0", 32, WidgetLabel.ALIGN_LEFT, 0);
		
		addWidget(countDownLabel);
		addWidget(timeLeftLabel);
		addWidget(scoreLabel);
	}

	@Override
	public void updateGui() {
		scoreLabel.setText("Your Score: " + gameplay.getPlayersScore());
	}
	
	public void setTimeLeftTime(int time) {
		timeLeftLabel.setText("Time Left: " + time + " Seconds");
	}
	
	public void setCountDownTime(int time) {
		if (time < -1)
			countDownLabel.setText("Time's Up!");
		else if (time == -1) 
			countDownLabel.setText("");
		else if (time == 0)
			countDownLabel.setText("GO!");
		else
			countDownLabel.setText(Integer.toString(time));
	}
}
