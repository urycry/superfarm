package com.richardhughes.superfarm;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.game.framework.Sprite;

import android.graphics.Point;

public class Camera {

	public Point _position = new Point();
	
	public int _screenWidth = 0;
	public int _screenHeight = 0;

	public boolean Load(GameBase game) {

		this._screenWidth = game.ScreenWidth;
		this._screenHeight = game.ScreenHeight;

		return true;
	}

	public void Focus(SuperFarmGame game, Actor a) {

		// we'll set the sprite in the center of the screen
		int halfScreenW = game.ScreenWidth / 2;
		int halfScreenH = game.ScreenHeight / 2;

		int halfSpriteW = a.GetWidth(game) / 2;
		int halfSpriteH = a.GetHeight(game) / 2;

		this._position.x = a.GetPosition().x - halfSpriteW - halfScreenW;
		this._position.y = a.GetPosition().y - halfSpriteH - halfScreenH;
	}
}
