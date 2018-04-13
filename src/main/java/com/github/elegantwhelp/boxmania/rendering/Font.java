package com.github.elegantwhelp.boxmania.rendering;

import java.util.Map;

import com.github.elegantwhelp.boxmania.rendering.font.CharData;
import com.github.elegantwhelp.boxmania.rendering.font.FontLoader;

public class Font {
	private FontLoader fontLoader;
	private VertexBatcher batcher;
	
	private Texture texture; // Although the fonts can possibly be on another sheet, I am only going to be using the one at the index of 0.
	private Map<Character, CharData> charMap;
	private int textureIndex;
	
	private float textureWidth;
	private float textureHeight;
	
	private float fontScale;
	
	public Font(VertexBatcher batcher, String font) {
		fontLoader = new FontLoader("/fonts/" + font + ".fnt");
		
		this.batcher = batcher;
		
		texture = new Texture("/fonts/" + fontLoader.getFontTextureNames()[0], true);
		textureIndex = 0;
		
		textureWidth = texture.getWidth();
		textureHeight = texture.getHeight();
		
		charMap = fontLoader.getCharDataMap();
		
		fontScale = 1.0f / (float)fontLoader.getFontSize();
	}
	
	public void bindTexture(int textureIndex) {
		this.textureIndex = textureIndex;
		texture.bind(textureIndex);
	}
	
	/**
	 * Returns the width of the entire text field.
	 * @param string
	 * @param charScale
	 * @return
	 */
	public float getWidthOfText(String string, float charScale) {
		float size = 0;
		float biggestSize = 0;
		
		for (int i = 0; i < string.length(); i++) {
			char character = string.charAt(i);
			
			if (character == '\n') {
				if (size > biggestSize)
					biggestSize = size;
				size = 0;
				continue;
			}
			
			if (!charMap.containsKey(character))
				continue;
			
			CharData data = charMap.get(character);
			
			size += data.xAdvance;
		}
		if (size > biggestSize)
			return size * fontScale * charScale;
		return biggestSize * fontScale * charScale;
	}
	
	public void drawString(String string, float x, float y, float charScale) {
		float xPointer = 0;
		float yPointer = 0;
		float scale = fontScale * charScale;
		for (int i = 0; i < string.length(); i++) {
			char character = string.charAt(i);
			
			if (character == '\n') {
				yPointer += charScale;
				xPointer = 0;
				continue;
			}
			
			if (!charMap.containsKey(character))
				continue;
			
			CharData data = charMap.get(character);
			
			float u = (float)data.x / textureWidth;
			float v = (float)data.y / textureHeight;
			float u2 = u + ((float)data.width / textureWidth);
			float v2 = v + ((float)data.height / textureHeight);

			float xOff = data.xOffset * scale;
			float yOff = data.yOffset * scale;
			float width = data.width * scale;
			float height = data.height * scale;
			
			float quadX = x + xOff + xPointer;
			float quadY = y + yOff + yPointer;
			
			batcher.addQuad(
					quadX, quadY,
					quadX+width, quadY+height,
					u, v,
					u2, v2,
					textureIndex,
					1, 1, 1);
			xPointer += (data.xAdvance * scale);
		}
	}
}
