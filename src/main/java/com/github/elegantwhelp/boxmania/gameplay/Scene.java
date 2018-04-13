package com.github.elegantwhelp.boxmania.gameplay;

import java.util.Random;

import org.joml.Vector2f;

import com.github.elegantwhelp.boxmania.BoxMania;
import com.github.elegantwhelp.boxmania.entities.Player;
import com.github.elegantwhelp.boxmania.entities.Point;
import com.github.elegantwhelp.boxmania.entities.PointBig;
import com.github.elegantwhelp.boxmania.entities.PointSmall;
import com.github.elegantwhelp.boxmania.gui.GuiGameOver;
import com.github.elegantwhelp.boxmania.gui.GuiGameplay;
import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.io.KeyState;
import com.github.elegantwhelp.boxmania.rendering.Sprite;
import com.github.elegantwhelp.boxmania.rendering.VertexBatcher;
import com.github.elegantwhelp.boxmania.util.Timer;

/*
 * Game Ideas
 * 
 * Boxes (points) spawn and decay
 * 
 * Big boxes give 10
 * Small boxes give 20
 * 
 * Small boxes decay faster
 * 
 * Power Ups
 * Single/Multiplayer
 * 	Collector - Slightly bigger radius to collect boxes
 * 	Time Freeze/Increase - Time will freeze/increase for a couple of seconds.
 * 
 * Multiplayer Only
 * 	Boom - Blows up entire board removing ALL point objects from the scene and removing 50-100 score from other players? Needs actual testing for value to be considered.
 * 	
 */

public class Scene {
	private BoxMania game;
	private GuiGameplay gameplayGui;
	
	private Sprite sceneAssets;	// Contains sprites for points/powerups/etc.
	private Sprite playerSkins; // Contains skins the player can choose from
	
	private Random random;
	
	private Player player;

	private int playerSkinIndex;
	private int playerScore;
	private int countDownToStart;
	private int timeLeft;
	private int timeBeforeGameOverScreen;
	
	private boolean timesUp;
	
	private Point[][] points;
	
	private Timer pointsAdderTimer;
	private Timer countDownTimer;
	private Timer timeLeftTimer;
	private Timer timeBeforeGameOverScreenTimer;
	
	public Scene(BoxMania game, Sprite sceneAssets, Sprite playerSkins) {
		this.game = game;
		random = new Random();
		
		this.sceneAssets = sceneAssets;
		this.playerSkins = playerSkins;
		
		points = new Point[11][11];
		points = new Point[11][11];
		
		int playerX = random.nextInt(10) - 5;
		int playerY = random.nextInt(10) - 5;
		
		player = new Player(new Vector2f(playerX, playerY));
		playerScore = 0;
		playerSkinIndex = game.getPlayerSelectedSkinIndex();

		countDownToStart = 4;
		countDownTimer = new Timer();
		countDownTimer.start();
		
		timeLeftTimer = new Timer();
		timeLeft = 10;
		timesUp = false;
		
		pointsAdderTimer = new Timer();
		pointsAdderTimer.start();
		
		timeBeforeGameOverScreenTimer = new Timer();
		timeBeforeGameOverScreen = 2;
	}
	
	public void generatePoint() {
		// The chance of a point being 20 points is supposed to be a low chance. 
		boolean bigPoints = random.nextInt(10) > 7;
		
		int x = random.nextInt(11);
		int y = random.nextInt(11);
		
		Vector2f playerPos = player.getForcedPosition();
		
		while (points[x][y] != null || ((playerPos.x+5) == x && (playerPos.y+5) == y)) {
			x = random.nextInt(11);
			y = random.nextInt(11);
		}
		
		if (bigPoints)
			points[x][y] = new PointBig(new Vector2f());
		else
			points[x][y] = new PointSmall(new Vector2f());
	}
	
	public void setGameplayGui(GuiGameplay gameplayGui) {
		this.gameplayGui = gameplayGui;
	}
	
	public void renderScene(VertexBatcher batcher) {
		
		Vector2f positions = player.getPosition();
		
		float playerX = positions.x * 64;
		float playerY = positions.y * 64;
		
		playerSkins.drawSprite(
				playerX - 31f, playerY - 31f,
				playerX + 32f, playerY + 32f,
				playerSkinIndex);
		
		for (int j = 0; j < 11; j++) {
			for (int i = 0; i < 11; i++) {
				Point point = points[i][j];
				if (point != null) {
					float pointX = (i - 5) * 64;
					float pointY = (j - 5) * 64;
					
					sceneAssets.drawSprite(
							pointX - 31f, pointY - 31f,
							pointX + 32f, pointY + 32f,
							point instanceof PointSmall ? 1 : 0);
				}
			}
		}
	}
	
	public int getPlayersScore() {
		return playerScore;
	}
	
	public void updateScene(Input input, float delta) {
		if (countDownToStart > 0) { // The game is in a state of count down. The player may NOT be able to move during this moment. But points may continue to spawn and decay as they please.
			int time = (int)countDownTimer.getElapsedTime();
			
			gameplayGui.setCountDownTime(countDownToStart - time - 1);
			
			if (time > countDownToStart - 1) {
				timeLeftTimer.start();
				countDownToStart = 0;
			}
		} else if (!timesUp) {
			int timeLeft = (int)timeLeftTimer.getElapsedTime();
			
			int time = this.timeLeft - timeLeft;
			gameplayGui.setTimeLeftTime(time);
			if (time > 0 && time < 4)
				gameplayGui.setCountDownTime(time);
			if (time == 0) {
				gameplayGui.setCountDownTime(-2);
				timesUp = true;
				timeBeforeGameOverScreenTimer.start();
			}
			
			if (input.getKey("give10") == KeyState.PRESSED)
				playerScore += 10;
			if (input.getKey("give20") == KeyState.PRESSED) 
				playerScore += 20;
			
			player.input(input);
			
			Vector2f playerPos = player.getForcedPosition();
			if (playerPos.x < -5)
				playerPos.x = -5;
			if (playerPos.x > 5)
				playerPos.x = 5;
			if (playerPos.y < -5)
				playerPos.y = -5;
			if (playerPos.y > 5)
				playerPos.y = 5;
			
			int playerPosPointX = (int)playerPos.x + 5; // Offsets the player position to be used for the array
			int playerPosPointY = (int)playerPos.y + 5;
			
			Point point = points[playerPosPointX][playerPosPointY];
			if (point != null) {
				playerScore += point.getScore();
				points[playerPosPointX][playerPosPointY] = null;
			}
			
		} else {	// Times Up
			int time = (int)timeBeforeGameOverScreenTimer.getElapsedTime();
			if (time > timeBeforeGameOverScreen) {
				game.endGame(playerScore);
				return;
			}
		}
		player.update(delta);
		
		for (int j = 0; j < 11; j++) {
			for (int i = 0; i < 11; i++) {
				Point point = points[i][j];
				if (point != null) {
					if (point.shouldDecay()) {
						points[i][j] = null;
					}
				}
			}
		}
		
		if (pointsAdderTimer.getElapsedTime() > .25) {
			pointsAdderTimer.start();
			generatePoint();
		}
	}
}
