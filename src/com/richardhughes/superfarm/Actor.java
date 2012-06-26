package com.richardhughes.superfarm;

import android.graphics.Point;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.game.framework.Sprite;
import com.richardhughes.jx.util.FileHelper;
import com.richardhughes.jx.util.XMLHelper;

public class Actor {

	public ActorDirection _direction = ActorDirection.South;

	private Sprite _spriteIdleN = new Sprite();
	private Sprite _spriteWalkingN = new Sprite();

	private Sprite _spriteIdleS = new Sprite();
	private Sprite _spriteWalkingS = new Sprite();

	private Sprite _spriteIdleE = new Sprite();
	private Sprite _spriteWalkingE = new Sprite();

	private Sprite _spriteIdleW = new Sprite();
	private Sprite _spriteWalkingW = new Sprite();

	private Sprite _currentSprite = null;
	
	private Point _pos = new Point();

	private float _speed = 0;

	private boolean _isWalking = false;
	
	public boolean Load(String fileName, GameBase game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(fileName, game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		// N
		String animationIdleFileName = xhelp.GetValue("/actor/animationidlenfilename/text()");
		this._spriteIdleN.Load(animationIdleFileName, game);

		animationIdleFileName = xhelp.GetValue("/actor/animationwalkingnfilename/text()");
		this._spriteWalkingN.Load(animationIdleFileName, game);

		// S
		animationIdleFileName = xhelp.GetValue("/actor/animationidlesfilename/text()");
		this._spriteIdleS.Load(animationIdleFileName, game);

		animationIdleFileName = xhelp.GetValue("/actor/animationwalkingsfilename/text()");
		this._spriteWalkingS.Load(animationIdleFileName, game);

		// E
		animationIdleFileName = xhelp.GetValue("/actor/animationidleefilename/text()");
		this._spriteIdleE.Load(animationIdleFileName, game);

		animationIdleFileName = xhelp.GetValue("/actor/animationwalkingefilename/text()");
		this._spriteWalkingE.Load(animationIdleFileName, game);

		// W
		animationIdleFileName = xhelp.GetValue("/actor/animationidlewfilename/text()");
		this._spriteIdleW.Load(animationIdleFileName, game);

		animationIdleFileName = xhelp.GetValue("/actor/animationwalkingwfilename/text()");
		this._spriteWalkingW.Load(animationIdleFileName, game);

		this._speed = Float.parseFloat(xhelp.GetValue("/actor/speed/text()"));

		return true;
	}

	public void Render(GameBase game, Camera camera) {

		this._currentSprite.Position = this._pos;

		// the camera is not implemented in the library, only the game, so revert afterwards
		this._currentSprite.Position.x -= camera._position.x;
		this._currentSprite.Position.y -= camera._position.y;

		this._currentSprite.Render(game);

		this._currentSprite.Position.x += camera._position.x;
		this._currentSprite.Position.y += camera._position.y;

		// call this here as this should be called after any 'Move' call is made
		this._isWalking = false;
	}

	public void Update(GameBase game) {

		if(this._isWalking) {

			if(this._direction == ActorDirection.North)
				this._currentSprite = this._spriteWalkingN;
			else if(this._direction == ActorDirection.South)
				this._currentSprite = this._spriteWalkingS;
			else if(this._direction == ActorDirection.East)
				this._currentSprite = this._spriteWalkingE;
			else if(this._direction == ActorDirection.West)
				this._currentSprite = this._spriteWalkingW;
		}
		else {

			if(this._direction == ActorDirection.North)
				this._currentSprite = this._spriteIdleN;
			else if(this._direction == ActorDirection.South)
				this._currentSprite = this._spriteIdleS;
			else if(this._direction == ActorDirection.East)
				this._currentSprite = this._spriteIdleE;
			else if(this._direction == ActorDirection.West)
				this._currentSprite = this._spriteIdleW;
		}

		this._currentSprite.Update(game);
	}

	public void Move(ActorDirection dir, SuperFarmGame game) {

		this._direction = dir;

		this._isWalking = true;
		
		float leveler = 1.0f / game.TimeSinceLastFrame;
		float distance = this._speed * leveler;
		int pixelsToMove = game.MetersToPixels(distance);

		if(this._direction == ActorDirection.North) {

			this._pos.y -= pixelsToMove;
		}
		else if(this._direction == ActorDirection.South) {

			this._pos.y += pixelsToMove;
		}
		else if(this._direction == ActorDirection.East) {

			this._pos.x += pixelsToMove;
		}
		else if(this._direction == ActorDirection.West) {

			this._pos.x -= pixelsToMove;
		}
	}

	public Point GetPosition() {

		return this._pos;
	}

	public int GetWidth(SuperFarmGame game) {

		return this._currentSprite.GetTileWidth(game);
	}

	public int GetHeight(SuperFarmGame game) {

		return this._currentSprite.GetTileHeight(game);
	}
}
