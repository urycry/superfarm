package com.richardhughes.superfarm;

import java.io.*;
import java.util.*;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.util.FileHelper;

public class BMFont {

	public String m_FileName = "";
	public Hashtable<Character, BMFontInfo> m_Characters = new Hashtable<Character, BMFontInfo>();

	public int ImageWidth = 0;
	public int ImageHeight = 0;

	public int CharacterHeight = 0;
	
	public void Load(GameBase game, String fileName) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(fileName, game.CurrentApplicationContext);

		StringReader reader = new StringReader(fileData);
		BufferedReader br = new BufferedReader(reader);

		String info = "";
		String common = "";
		String page = "";
		String chars = "";

		int numberOfCharacters = 0;
		
		try {

			// info
			info = br.readLine();

			// common
			common = br.readLine();
			this.ImageWidth = Integer.parseInt(this.GetValue(common, "scaleW"));
			this.ImageHeight = Integer.parseInt(this.GetValue(common, "scaleH"));

			// page
			page = br.readLine();
			this.m_FileName = this.GetValue(page, "file");

			// chars
			chars = br.readLine();
			numberOfCharacters = Integer.parseInt(this.GetValue(chars, "count"));

			// char
			for(int i = 0; i < numberOfCharacters; i++) {

				String line = br.readLine();

				this.ReadChar(line);
			}

			reader.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void ReadChar(String line) {

		Integer code = Integer.parseInt(this.GetValue(line, "id"));
		Integer x = Integer.parseInt(this.GetValue(line, "x"));
		Integer y = Integer.parseInt(this.GetValue(line, "y"));
		Integer width = Integer.parseInt(this.GetValue(line, "width"));
		Integer height = Integer.parseInt(this.GetValue(line, "height"));
		Integer xOffset = Integer.parseInt(this.GetValue(line, "xoffset"));
		Integer yOffset = Integer.parseInt(this.GetValue(line, "yoffset"));

		if(!this.m_Characters.containsKey(code)) {

			BMFontInfo info = new BMFontInfo();
			info.m_Code = code;
			info.m_X = x;
			info.m_Y = y;
			info.m_Width = width;
			info.m_Height = height;
			info.m_XOffset = xOffset;
			info.m_YOffset = yOffset;

			if(height * 1.2 > this.CharacterHeight) {

				this.CharacterHeight = height;
				this.CharacterHeight += height * 0.2; // add a bit of spacing
			}
			
			this.m_Characters.put(Character.toChars(code)[0], info);
		}
	}

	public String GetValue(String line, String key) {

		String value = "";

		String keys [] = line.split(" ");

		key = key.toLowerCase().trim();

		for(String s : keys) {

			String pairs [] = s.split("=");

			value = key;

			if(pairs.length == 2) {

				if(pairs[0].toLowerCase().trim().equals(key)) {

					value = pairs[1];
					break;
				}
			}
		}

		// if value is enclosed in quotes, remove them
		if(value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {

			value = value.substring(1, value.length() - 1);
		}
		
		return value;
	}
}
