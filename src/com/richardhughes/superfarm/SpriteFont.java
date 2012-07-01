package com.richardhughes.superfarm;

import com.richardhughes.jx.game.framework.*;

import android.graphics.Point;
import android.graphics.PointF;

public class SpriteFont {

	public final static int CHARACTER_COUNT = 256;
	
	private BMFont m_BMFont = new BMFont();

	private Quad[] _characters = new Quad[SpriteFont.CHARACTER_COUNT];
	
	public void Load(GameBase game, String fontFileName) {

		this.m_BMFont.Load(game, fontFileName);

		for(int i = 0; i < SpriteFont.CHARACTER_COUNT; i++) {

			this._characters[i] = new Quad();

			this._characters[i].TextureId = game.TextureManager.LoadTexture(SuperFarmGame.PATH_FONTS + this.m_BMFont.m_FileName,
					game.CurrentApplicationContext);
		}

		for(int i = 0; i < SpriteFont.CHARACTER_COUNT; i++) {

			char c = (char)i;
			if(!this.m_BMFont.m_Characters.containsKey(c))
				continue;

			BMFontInfo info = this.m_BMFont.m_Characters.get(c);

			float xl = (float)info.m_X / (float)this.m_BMFont.ImageWidth; // x left
			float yt = (float)info.m_Y / (float)this.m_BMFont.ImageHeight; // y top

			float xr = (float)(info.m_X + info.m_Width) / (float)this.m_BMFont.ImageWidth; // x right 
			float yb = (float)(info.m_Y + info.m_Height) / (float)this.m_BMFont.ImageHeight; // y bottom

			TexCoordPoints texCoords = new TexCoordPoints();

			texCoords.TopLeft.x = xl;
			texCoords.TopLeft.y = yt;

			texCoords.TopRight.x = xr;
			texCoords.TopRight.y = yt;

			texCoords.BottomLeft.x = xl;
			texCoords.BottomLeft.y = yb;

			texCoords.BottomRight.x = xr;
			texCoords.BottomRight.y = yb;

			this._characters[i].SetTextureCoords(texCoords);
		}
	}

	public void Unload(GameBase game) {

		for(int i = 0; i < SpriteFont.CHARACTER_COUNT; i++) {

			this._characters[i].TextureId = -1;
		}

		game.TextureManager.FreeTexture(this.m_BMFont.m_FileName);
	}

	public void DrawText(GameBase game, String text, Point pos, float size, int color) {

		// at size 1, it will be roughly 15 characters per line to fit a screen width, 8 for height
		// assuming that each character is the same width...
		float sizeModifierX = 100.0f / (game.ScreenWidth / 15.0f * size);
		float sizeModifierY = 100.0f / (game.ScreenHeight / 8.0f * size);

		// allows for a little bit of white space between each letter to make them a little bit more
		// legible
		float widthPad = 0.1f;
		float heightPad = 0.1f; // this should really work off of the highest character in the current line

		float x = 0.0f;

		for(int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);
			if(!this.m_BMFont.m_Characters.containsKey(c))
				continue;

			BMFontInfo info = this.m_BMFont.m_Characters.get(c);

			Quad quad = this._characters[c];

			quad.Width = (int)(sizeModifierX * info.m_Width);
			quad.Height = (int)(sizeModifierY * info.m_Height);

			quad.ColorOfQuad = color;
			
			quad.Position.x = (int)(pos.x + x + info.m_XOffset) + (int)(quad.Width * widthPad);
			quad.Position.y = pos.y + info.m_YOffset + (int)(quad.Height * heightPad);

			quad.Render(game.GLContext);

			x += (sizeModifierX * info.m_Width + (quad.Width * widthPad));
		}
	}

	public float GetCharacterHeight(GameBase game, float size) {

		float sizeModifierY = game.ScreenHeight / 20.0f * size;
		sizeModifierY *= 1.2; // taken from BMFront - when creating my engine/api, consolodate this code...

		return sizeModifierY;
	}
}
