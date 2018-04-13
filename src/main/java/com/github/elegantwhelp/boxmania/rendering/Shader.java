package com.github.elegantwhelp.boxmania.rendering;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Shader {
	// Modifiable Shader names/values
	private static final String UNIFORM_VERT_CAM_PROJECTION = "cam_projection";
	private static final String UNIFORM_VERT_CAM_TRANSFORM = "cam_transform";
	private static final String UNIFORM_FRAG_SAMPLERS = "samplers";
	private static final int	UNIFORM_FRAG_SAMPLERS_SIZE = 4;
	
//	private static final String vertexSource = 
//					"#version 330 \n" +
//	
//					"layout (location = 0) in vec2 position; \n" +
//					"layout (location = 1) in vec2 texcoord; \n" +
//					"layout (location = 2) in float texsample; \n" +
//					"layout (location = 3) in vec3 color; \n" +
//					
//					"uniform mat4 "+UNIFORM_VERT_CAM_PROJECTION+"; \n" +
//					"uniform mat4 "+UNIFORM_VERT_CAM_TRANSFORM+"; \n" +
//					
//					"out vec3 fcolor; \n" +
//					"out vec2 tex; \n" +
//					"flat out int sample; \n" +
//					
//					"void main() { \n" +
//					"tex = texcoord; \n" + 
//					"fcolor = color; \n" +
//					"sample = int(texsample); \n" +
//					"gl_Position = "+UNIFORM_VERT_CAM_PROJECTION+" * "+UNIFORM_VERT_CAM_TRANSFORM+" * vec4(position,0,1); \n" +
//					"} \n";
//	
//	private static final String fragmentSource = 
//					"#version 330 \n" +
//	
//					"uniform sampler2D "+UNIFORM_FRAG_SAMPLERS+"["+UNIFORM_FRAG_SAMPLERS_SIZE+"]; \n" +
//					
//					"in vec2 tex; \n" +
//					"in vec3 fcolor; \n" +
//					"flat in int sample; \n" +
//					
//					"out vec4 fragColor; \n" +
//					
//					"void main() { \n" +
//					"fragColor = texture2D("+UNIFORM_FRAG_SAMPLERS+"[sample], tex) * vec4(fcolor, 1); \n" +
//					"} \n";
	
	private static final String vertexSource = 
				"#version 120 \n" +
	
				"attribute vec2 position; \n" +
				"attribute vec2 texcoord; \n" +
				"attribute float texsample; \n" +
				"attribute vec3 color; \n" +
				
				"uniform mat4 "+UNIFORM_VERT_CAM_PROJECTION+"; \n" +
				"uniform mat4 "+UNIFORM_VERT_CAM_TRANSFORM+"; \n" +
				
				"varying vec3 fcolor; \n" +
				"varying vec2 tex; \n" +
				"flat varying float sample; \n" +
				
				"void main() { \n" +
				"tex = texcoord; \n" + 
				"fcolor = color; \n" +
				"sample = texsample; \n" +
				"gl_Position = "+UNIFORM_VERT_CAM_PROJECTION+" * "+UNIFORM_VERT_CAM_TRANSFORM+" * vec4(position,0,1); \n" +
				"} \n";

	private static final String fragmentSource = 
				"#version 120 \n" +
	
				"uniform sampler2D "+UNIFORM_FRAG_SAMPLERS+"["+UNIFORM_FRAG_SAMPLERS_SIZE+"]; \n" +
				
				"varying vec2 tex; \n" +
				"varying vec3 fcolor; \n" +
				"flat varying float sample; \n" +
				
				//"out vec4 fragColor; \n" +
				
				"void main() { \n" +
				"gl_FragColor = texture2D("+UNIFORM_FRAG_SAMPLERS+"[int(sample)], tex) * vec4(fcolor, 1); \n" +
				"} \n";
	
	private int programObject;
	private int vertexShaderObject;
	private int fragmentShaderObject;
	
	private int uniformCamProjection;
	private int uniformCamTransform;
	
	public Shader() {
	}
	
	public void init() {
		programObject = glCreateProgram();
		vertexShaderObject = glCreateShader(GL_VERTEX_SHADER);
		fragmentShaderObject = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertexShaderObject, vertexSource);
		glCompileShader(vertexShaderObject);
		if (glGetShaderi(vertexShaderObject, GL_COMPILE_STATUS) == 0) {
			System.err.println("[SHADER] Vertex Failed: " + glGetShaderInfoLog(vertexShaderObject));
		}
		glShaderSource(fragmentShaderObject, fragmentSource);
		glCompileShader(fragmentShaderObject);
		if (glGetShaderi(fragmentShaderObject, GL_COMPILE_STATUS) == 0) {
			System.err.println("[SHADER] Fragment Failed: " + glGetShaderInfoLog(fragmentShaderObject));
		}
		
		glAttachShader(programObject, vertexShaderObject);
		glAttachShader(programObject, fragmentShaderObject);
		
		glBindAttribLocation(programObject, 0, "position");
		glBindAttribLocation(programObject, 1, "texcoord");
		glBindAttribLocation(programObject, 2, "texsample");
		glBindAttribLocation(programObject, 3, "color");
		
		glLinkProgram(programObject);
		if (glGetProgrami(programObject, GL_LINK_STATUS) == 0) {
			System.err.println("[SHADER] Program Link Failed: " + glGetProgramInfoLog(programObject));
		}
		glValidateProgram(programObject);
		if (glGetProgrami(programObject, GL_VALIDATE_STATUS) == 0) {
			System.err.println("[SHADER] Program Validate Failed: " + glGetProgramInfoLog(programObject));
		}
		
		uniformCamProjection = glGetUniformLocation(programObject, UNIFORM_VERT_CAM_PROJECTION);
		uniformCamTransform = glGetUniformLocation(programObject, UNIFORM_VERT_CAM_TRANSFORM);
	}
	
	public void destroy() {
		glDetachShader(programObject, vertexShaderObject);
		glDetachShader(programObject, fragmentShaderObject);
		glDeleteShader(vertexShaderObject);
		glDeleteShader(fragmentShaderObject);
		glDeleteProgram(programObject);
	}
	
	public void bind() {
		glUseProgram(programObject);
	}
	
	public void setUpSamplers() {
		for (int i = 0; i < UNIFORM_FRAG_SAMPLERS_SIZE; i++) {
			int location = glGetUniformLocation(programObject, UNIFORM_FRAG_SAMPLERS + "[" + i + "]");
			glUniform1i(location, i);
		}
	}
	
	public void updateCameraVariables(Camera camera) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		
		camera.getProjection().get(buffer);
		glUniformMatrix4fv(uniformCamProjection, false, buffer);
		
		camera.getTransform().get(buffer);
		glUniformMatrix4fv(uniformCamTransform, false, buffer);
	}
}
