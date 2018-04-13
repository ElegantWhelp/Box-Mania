package com.github.elegantwhelp.boxmania.rendering;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class VertexBatcher {
	public static final int MAX_VERTICES = 30000; // 30000 vertices. 10000 triangles (3 vertices per triangle). 5000 quads (2 triangles per quad).
	public static final int VERTEX_SIZE = 8;
	private FloatBuffer buffer;
	
	private int vbo;
	private int drawCount;
	
	public VertexBatcher() {
	}
	
	public void init() {
		buffer = BufferUtils.createFloatBuffer(MAX_VERTICES * VERTEX_SIZE);
		
		vbo = glGenBuffers();
	}
	
	public void destroy() {
		glDeleteBuffers(vbo);
	}
	
	public void addVertex(float x, float y, float u, float v, int sampler, float r, float g, float b) {
		if (drawCount < MAX_VERTICES) {
			buffer.put(x);
			buffer.put(y);
			
			buffer.put(u);
			buffer.put(v);
			
			buffer.put((float)sampler);
			
			buffer.put(r);
			buffer.put(g);
			buffer.put(b);
			
			drawCount++;
		}
	}
	
	public void addQuad(float x1, float y1, float x2, float y2, int textureIndex, float r, float g, float b) {
		addVertex(x1, y1,		0,0,		textureIndex,		r,g,b); // Top Left
		addVertex(x2, y1,		1,0,		textureIndex,		r,g,b); // Top Right
		addVertex(x2, y2,		1,1,		textureIndex,		r,g,b); // Bottom Right
		
		addVertex(x2, y2,		1,1,		textureIndex,		r,g,b); // Bottom Right
		addVertex(x1, y2,		0,1,		textureIndex,		r,g,b); // Bottom Left
		addVertex(x1, y1,		0,0,		textureIndex,		r,g,b); // Top Left
	}
	
	public void addQuad(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2, int textureIndex, float r, float g, float b) {
		addVertex(x1, y1,		u1,v1,		textureIndex,		r,g,b); // Top Left
		addVertex(x2, y1,		u2,v1,		textureIndex,		r,g,b); // Top Right
		addVertex(x2, y2,		u2,v2,		textureIndex,		r,g,b); // Bottom Right
		
		addVertex(x2, y2,		u2,v2,		textureIndex,		r,g,b); // Bottom Right
		addVertex(x1, y2,		u1,v2,		textureIndex,		r,g,b); // Bottom Left
		addVertex(x1, y1,		u1,v1,		textureIndex,		r,g,b); // Top Left
	}
	
	public void draw() {
		buffer.flip();
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEX_SIZE * 4, 0);		// Position
		glVertexAttribPointer(1, 2, GL_FLOAT, false, VERTEX_SIZE * 4, 8);		// Texture
		glVertexAttribPointer(2, 1, GL_FLOAT, false, VERTEX_SIZE * 4, 16);		// Texture Index
		glVertexAttribPointer(3, 3, GL_FLOAT, false, VERTEX_SIZE * 4, 20);		// Color (excludes alpha)
		
		glDrawArrays(GL_TRIANGLES, 0, drawCount);
		
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		
		drawCount = 0;
		buffer.clear();
	}
}
