package com.github.elegantwhelp.boxmania;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.github.elegantwhelp.boxmania.gameplay.GameState;
import com.github.elegantwhelp.boxmania.gameplay.Scene;
import com.github.elegantwhelp.boxmania.gui.GuiGameOver;
import com.github.elegantwhelp.boxmania.gui.GuiGameplay;
import com.github.elegantwhelp.boxmania.gui.GuiHandler;
import com.github.elegantwhelp.boxmania.gui.GuiMain;
import com.github.elegantwhelp.boxmania.io.Input;
import com.github.elegantwhelp.boxmania.rendering.Camera;
import com.github.elegantwhelp.boxmania.rendering.Font;
import com.github.elegantwhelp.boxmania.rendering.Shader;
import com.github.elegantwhelp.boxmania.rendering.Sprite;
import com.github.elegantwhelp.boxmania.rendering.VertexBatcher;
import com.github.elegantwhelp.boxmania.util.Timer;

public class BoxMania {
	private long window;
	private Vector2f windowSize;
	
	private Input input;
	
	private Shader shader;
	private VertexBatcher vertexBatcher;
	private Sprite guiAssets;
	private Sprite sceneAssets;
	private Sprite boxSkins;
	private Font font;
	
	private Camera gameCamera;
	
	private GuiHandler guiHandler;
	
	private int targetFPS;
	
	private GameState gameState;
	private Scene scene;
	
	private int playerSelectedSkin;
	
	public BoxMania() {
		window = 0;
		windowSize = new Vector2f(1280, 720); // Default window size
		gameState = GameState.MENUS;
		playerSelectedSkin = 0; // Default is 0 (or white)
	}
	
	public void init() {
		if (!glfwInit()) {
			System.err.println("Failed to initialize GLFW!");
			System.exit(1);
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		window = glfwCreateWindow((int)windowSize.x, (int)windowSize.y, "Box Mania", 0, 0);
		
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (int)((vidMode.width() - (int)windowSize.x) * 0.5f), (int)((vidMode.height() - (int)windowSize.y) * 0.5f));
		
		input = new Input(windowSize.mul(0.5f, new Vector2f()));
		glfwSetKeyCallback(window, input.getKeyCallback());
		glfwSetMouseButtonCallback(window, input.getMouseButtonCallback());
		glfwSetCursorPosCallback(window, input.getCursorPositionCallback());
		
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		gameCamera = new Camera();
		float halfWinWidth = windowSize.x * 0.5f;
		float halfWinHeight = windowSize.y * 0.5f;
		
		gameCamera.setProjection(-halfWinWidth, halfWinWidth, halfWinHeight, -halfWinHeight);
		
		shader = new Shader();
		shader.init();
		shader.bind();
		shader.setUpSamplers();

		vertexBatcher = new VertexBatcher();
		vertexBatcher.init();
		
		boxSkins = new Sprite(vertexBatcher, "/textures/boxSkins.png", 16);
		sceneAssets = new Sprite(vertexBatcher, "/textures/sceneAssets.png", 8);
		guiAssets = new Sprite(vertexBatcher, "/textures/guiAssets.png", 16);
		font = new Font(vertexBatcher, "arial");
		
		boxSkins.bindTexture(0);
		font.bindTexture(1);
		guiAssets.bindTexture(2);
		sceneAssets.bindTexture(3);
		
		// Setting up the blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Initialize Gui System
		
		guiHandler = new GuiHandler(input, guiAssets, font);
		guiHandler.setActiveGUI(new GuiMain(this));
		
		
		targetFPS = 120;
		
		
		glfwSwapInterval(0);				// No VSync
		
		render();
		
		glfwShowWindow(window);
		
		input.registerKey("quit", GLFW_KEY_ESCAPE);
		input.registerKey("moveUp", GLFW_KEY_W);
		input.registerKey("moveLeft", GLFW_KEY_A);
		input.registerKey("moveRight", GLFW_KEY_D);
		input.registerKey("moveDown", GLFW_KEY_S);
		input.registerKey("give10", GLFW_KEY_Q);
		input.registerKey("give20", GLFW_KEY_E);
	}
	
	public void startGame() {
		gameState = GameState.GAMEPLAY;
		scene = new Scene(this, sceneAssets, boxSkins);
		GuiGameplay gui = new GuiGameplay(this, scene);
		scene.setGameplayGui(gui);
		guiHandler.setActiveGUI(gui);
	}
	
	public void endGame(int playerScore) {
		gameState = GameState.MENUS;
		scene = null;
		guiHandler.setActiveGUI(new GuiGameOver(this, playerScore));
	}
	
	public void run() {
		boolean isRunning = true;
		
		double fps = 1.0f / targetFPS;
		
		double lastTime = Timer.getTimeInSeconds(); // Get current time in seconds, then cast to a double.
		double updateTime = 0;
		
		while (isRunning) {
			double currentTime = Timer.getTimeInSeconds();
			double passed = currentTime - lastTime;
			updateTime += passed;
			lastTime = currentTime;

			boolean allowRendering = false;
			
			while (updateTime >= fps) {
				updateTime -= fps;
				allowRendering = true;
				glfwPollEvents();
				
				if (glfwWindowShouldClose(window)) {
					isRunning = false;
					break; // Break out of the loop. No need to continue.
				}
				update((float)fps);
				
				input.update();
			}
			if (allowRendering) {
				render();
			}
			
			try {
				Thread.sleep(1); // There will be times where the game won't even update since the time hasn't gone passed the FPS value. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update(float delta) {
		guiHandler.updateGui(delta);
		
		switch (gameState) {
		case MENUS:
			
			break;
		case GAMEPLAY:
			scene.updateScene(input, delta);
			break;
		}
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);

		shader.updateCameraVariables(gameCamera);
		
		switch (gameState) {
		case MENUS:
			
			break;
		case GAMEPLAY:
			scene.renderScene(vertexBatcher);
			break;
		}
		
		guiHandler.renderGui();
		
		vertexBatcher.draw();
		
		glfwSwapBuffers(window);
	}
	
	public void destroy() {
		glDisable(GL_BLEND);
		
		vertexBatcher.destroy();
		shader.destroy();
		
		glfwDestroyWindow(window);
		
		glfwTerminate();
	}
	


	public Sprite getBoxSkins() { return boxSkins; }
	public int getPlayerSelectedSkinIndex() { return playerSelectedSkin; }
	public void setPlayerSelectedSkinIndex(int skin) { this.playerSelectedSkin = skin; }
	public GuiHandler getGuiHandler() { return guiHandler; }
}
