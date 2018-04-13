package com.github.elegantwhelp.boxmania.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	private int textureObject;
	
	private int width;
	private int height;
	
	/**
	 * Creates a texture with without linear filtering/
	 * @param textureName
	 */
	public Texture(String textureName) { this(textureName, false); }
	
	/**
	 * Creates a texture with the choice of having linear filtering.
	 * @param textureName
	 * @param useLinearFiltering
	 */
	public Texture(String textureName, boolean useLinearFiltering) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(getClass().getResource(textureName).toURI()));
			width = bufferedImage.getWidth();
			height = bufferedImage.getHeight();
			
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					int pixel = pixels_raw[j * width + i];
					pixels.put((byte) ((pixel >> 16) & 0xFF));	// RED
					pixels.put((byte) ((pixel >> 8) & 0xFF));	// GREEN
					pixels.put((byte) (pixel & 0xFF));			// BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF));	// ALPHA
				}
			}
			
			pixels.flip();
			
			textureObject = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, textureObject);
			
			int filtering = useLinearFiltering ? GL_LINEAR : GL_NEAREST;
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filtering);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filtering);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
			glBindTexture(GL_TEXTURE_2D, 0);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a 1x1 white texture.
	 */
	public Texture() {
		textureObject = glGenTextures();
		width = 1;
		height = 1;
		
		glBindTexture(GL_TEXTURE_2D, textureObject);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(4);
		pixels.put((byte)255);
		pixels.put((byte)255);
		pixels.put((byte)255);
		pixels.put((byte)255);
		pixels.flip();
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void destroy() {
		
	}
	
	public void bind(int sampler) {
		glActiveTexture(GL_TEXTURE0+sampler);
		glBindTexture(GL_TEXTURE_2D, textureObject);
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
