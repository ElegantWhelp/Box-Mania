package com.github.elegantwhelp.boxmania.rendering.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontLoader {
	// TODO Implement kerning later
	
	private Map<Character, CharData>	charDataMap;
	private String[]					fontTextureNames;
	
	private int							fontSize;
	
	/**
	 * This class loads and parses the .fnt files of the font.
	 * @param fontFile
	 */
	public FontLoader(String fontFile) {
		BufferedReader reader = null;
		
		charDataMap = new HashMap<Character, CharData>();
		
		fontSize = 0;
		
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fontFile)));
			
			String line = "";
			
			while ((line = reader.readLine()) != null) {
				simpleParse(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Map<Character, CharData> getCharDataMap() { return charDataMap; }
	public String[] getFontTextureNames() { return fontTextureNames; }
	public int getFontSize() { return fontSize; }
	
	/**
	 * Simply parses one line. In another function because I plan to rewrite this and make it better later on.
	 * @param line
	 */
	public void simpleParse(String line) {
		//info face="Arial" size=64 bold=0 italic=0 charset="ANSI" unicode=0 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1 outline=0
		if (line.startsWith("info")) {
			String[] tokens = removeEmptyStrings(line.split(" "));
			
			for (int i = 1; i < tokens.length; i++) { // Set i to one to skip 'info'
				String[] obj = tokens[i].split("=");
				
				if (obj.length > 1) { // Some font names (faces) end up having more than one word. Turns out I accidentally split the name as well. I'm not looking to ever use the name, so no need to parse it... Yet
					String id = obj[0];
					String data = obj[1];
					
					if (id.equalsIgnoreCase("size"))
						fontSize = Integer.parseInt(data);
				}
			}
			return;
		}
		
		//common lineHeight=63 base=51 scaleW=512 scaleH=512 pages=1 packed=0 alphaChnl=0 redChnl=4 greenChnl=4 blueChnl=4
		if (line.startsWith("common")) {
			String[] tokens = removeEmptyStrings(line.split(" "));
			
			for (int i = 1; i < tokens.length; i++) { // Set i to one to skip 'common'
				String[] obj = tokens[i].split("=");
				
				String id = obj[0];
				String data = obj[1];
				
				if (id.equalsIgnoreCase("pages"))
					fontTextureNames = new String[Integer.parseInt(data)];
			}
			return;
		}
		
		//page id=0 file="arial_0.png"
		if (line.startsWith("page")) {
			String[] tokens = removeEmptyStrings(line.split(" "));
			
			int pageID = 0;
			
			for (int i = 1; i < tokens.length; i++) { // Set i to one to skip 'page'
				String[] obj = tokens[i].split("=");
				
				String id = obj[0];
				String data = obj[1];
				
				if (id.equalsIgnoreCase("id"))
					pageID = Integer.parseInt(data);
				else if (id.equalsIgnoreCase("file"))
					fontTextureNames[pageID] = (data.replaceAll("\"", ""));
			}
			return;
		}
		
		//chars count=218
		if (line.startsWith("chars")) {
			
			return;
		}
		
		//char id=32   x=506   y=41    width=3     height=1     xoffset=-1    yoffset=62    xadvance=15    page=0  chnl=15
		if (line.startsWith("char")) {
			String[] tokens = removeEmptyStrings(line.split(" "));
			
			CharData charData = new CharData();
			char charID = 0;
			
			for (int i = 1; i < tokens.length; i++) { // Set i to one to skip 'char'
				String[] obj = tokens[i].split("=");
				
				String id = obj[0];
				int data = Integer.parseInt(obj[1]);
				
				if (id.equalsIgnoreCase("id"))
					charID = (char)data;
				else if (id.equalsIgnoreCase("x"))
					charData.x = data;
				else if (id.equalsIgnoreCase("y"))
					charData.y = data;
				else if (id.equalsIgnoreCase("width"))
					charData.width = data;
				else if (id.equalsIgnoreCase("height"))
					charData.height = data;
				else if (id.equalsIgnoreCase("xoffset"))
					charData.xOffset = data;
				else if (id.equalsIgnoreCase("yoffset"))
					charData.yOffset = data;
				else if (id.equalsIgnoreCase("xadvance"))
					charData.xAdvance = data;
				else if (id.equalsIgnoreCase("page"))
					charData.page = data;
				else if (id.equalsIgnoreCase("chnl"))
					charData.channel = data;
			}
			
			charDataMap.put(charID, charData);
			return;
		}
		
		if (line.startsWith("kerning")) {
			
			return;
		}
		
	}
	
	private String[] removeEmptyStrings(String[] strings) {
		List<String> fixedArray = new ArrayList<String>();
		for(int i = 0; i < strings.length; i++) {
			if(!strings[i].isEmpty())
				fixedArray.add(strings[i]);
		}
		
		String[] returnStrings = new String[fixedArray.size()];
		fixedArray.toArray(returnStrings);
		
		return returnStrings;
	}
}
