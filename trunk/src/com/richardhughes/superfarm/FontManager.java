package com.richardhughes.superfarm;

import java.util.Hashtable;

public class FontManager {

	private Hashtable<String, SpriteFont> _fonts = new Hashtable<String, SpriteFont>();
	public Hashtable<String, SpriteFont> GetFonts() { return this._fonts; }

	public SpriteFont GetFont(String name, SuperFarmGame game) {

		name = name.trim().toLowerCase();

		if(!this._fonts.containsKey(name)) {

			SpriteFont font = new SpriteFont();
			font.Load(game, SuperFarmGame.PATH_FONTS + name + ".fnt");

			this._fonts.put(name, font);
		}

		return this._fonts.get(name);
	}
}
