package com.richardhughes.superfarm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.Point;

import com.richardhughes.jx.game.framework.*;

public class FarmTileMap {

	private int _widthInTiles = 0;
	private int _heightInTiles = 0;

	private FloatBuffer _vertBuffer;
	private FloatBuffer _texCoordBuffer;
	private float[] _texCoords = null;

	private FloatBuffer _gridModeVertexBuffer;
	private int _numberOfGridLines = 0;

	private Sprite _groundBasicSpring = new Sprite();
	private Sprite _groundBasicSummer = new Sprite();
	private Sprite _groundBasicAutumn = new Sprite();
	private Sprite _groundBasicWinter = new Sprite();
	private Sprite _groundBasic = null;

	private boolean _isGridMode = false;
	
	public boolean Load(SuperFarmGame game) {

		this._groundBasicSpring.Load("World/ground_basic_spring.xml", game);
		this._groundBasicSummer.Load("World/ground_basic_summer.xml", game);
		this._groundBasicAutumn.Load("World/ground_basic_autumn.xml", game);
		this._groundBasicWinter.Load("World/ground_basic_winter.xml", game);
		this._groundBasic = this._groundBasicSpring;

		this._widthInTiles = game.ScreenWidth / game.MetersToPixels(game.GetSettings().FarmTileSize);
		this._heightInTiles = game.ScreenHeight / game.MetersToPixels(game.GetSettings().FarmTileSize);

		// we want a border of tiles too
		this._widthInTiles += 2;
		this._heightInTiles += 2;

		this.SetupVertexBuffer(game);
		this.SetupTextureCoordBuffer(game);

		this.SetupGridModeVertexBuffer(game);
		
		return true;
	}

	public void ResetTiles(SuperFarmGame game) {

		this.SetupTextureCoordBuffer(game, false);
	}

	public void SetupTextureCoordBuffer(SuperFarmGame game) {

		this.SetupTextureCoordBuffer(game, true);
	}

	public void SetupTextureCoordBuffer(SuperFarmGame game, boolean commitChanges) {

		this._texCoords = new float[this._widthInTiles * this._heightInTiles * 12];

		for(int x = 0; x < this._widthInTiles; x++) {

			for(int y = 0; y < this._heightInTiles; y++) {

				int tileIndex = x + (y * this._widthInTiles);

				// default the ground to ground basic
				this.SetTextureCoordinates(game, this._groundBasic, tileIndex);
			}
		}

		if(commitChanges) {

			this.CommitTextureCoordinateChanges(game);
		}
	}

	public void SetupVertexBuffer(SuperFarmGame game) {

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);

		float verts[] = new float[this._widthInTiles * this._heightInTiles * 12]; // 12 verts = 2 tris per quad

		int idx = 0;
		for(int y = 0; y < this._heightInTiles; y++) {

			for(int x = 0; x < this._widthInTiles; x++) {

				// top left
				verts[idx + 0] = tileSize * x;
				verts[idx + 1] = tileSize * y;

				// bottom right
				verts[idx + 2] = tileSize * (x + 1);
				verts[idx + 3] = tileSize * (y + 1);

				// bottom left
				verts[idx + 4] = tileSize * x;
				verts[idx + 5] = tileSize * (y + 1);

				// top left
				verts[idx + 6] = tileSize * x;
				verts[idx + 7] = tileSize * y;

				// top right
				verts[idx + 8] = tileSize * (x + 1);
				verts[idx + 9] = tileSize * y;

				// bottom right
				verts[idx + 10] = tileSize * (x + 1);
				verts[idx + 11] = tileSize * (y + 1);

				idx += 12;
			}
		}

		ByteBuffer buff = ByteBuffer.allocateDirect(verts.length * 4);
		buff.order(ByteOrder.nativeOrder());
		this._vertBuffer = buff.asFloatBuffer();
		this._vertBuffer.put(verts);
		this._vertBuffer.position(0);
	}

	public void SetupGridModeVertexBuffer(SuperFarmGame game) {

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);

		int screenWidth = this._widthInTiles * tileSize;
		int screenHeight = this._heightInTiles * tileSize;

		this._numberOfGridLines = this._widthInTiles + this._heightInTiles;
		float verts[] = new float[this._numberOfGridLines * 4]; // rendering lines

		int idx = 0;

		for(int y = 0; y < this._heightInTiles; y++) {

			// left
			verts[idx + 0] = 0;
			verts[idx + 1] = y * tileSize;

			// right
			verts[idx + 2] = screenWidth;
			verts[idx + 3] = y * tileSize;

			idx += 4;
		}

		for(int x = 0; x < this._widthInTiles; x++) {

			// top
			verts[idx + 0] = x * tileSize;
			verts[idx + 1] = 0;

			// right
			verts[idx + 2] = x * tileSize;
			verts[idx + 3] = screenHeight;

			idx += 4;
		}

		ByteBuffer buff = ByteBuffer.allocateDirect(verts.length * 4);
		buff.order(ByteOrder.nativeOrder());
		this._gridModeVertexBuffer = buff.asFloatBuffer();
		this._gridModeVertexBuffer.put(verts);
		this._gridModeVertexBuffer.position(0);
	}

	public void Render(Camera camera, SuperFarmGame game) {

		game.GLContext.glPushMatrix();

		game.GLContext.glBindTexture(GL10.GL_TEXTURE_2D, this._groundBasic.GetTextureId(game));

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);

		// we have border tiles so we can fake an infinite farm
		// origin is in the top left corner of the screen
		Point renderPos = new Point();
		renderPos.x = -tileSize - (camera._position.x % tileSize);
		renderPos.y = -tileSize - (camera._position.y % tileSize);

		game.GLContext.glTranslatef(renderPos.x, renderPos.y, 0.0f);

		// reset any previous scales for this render
		game.GLContext.glScalef(1.0f, 1.0f, 1.0f);

		game.GLContext.glVertexPointer(2, GL10.GL_FLOAT, 0, this._vertBuffer);
		game.GLContext.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this._texCoordBuffer);

		game.GLContext.glDrawArrays(GL10.GL_TRIANGLES, 0, this._texCoords.length / 2);

		game.GLContext.glPopMatrix();

		if(this._isGridMode) {

			this.RenderGridMode(game, renderPos);
		}
	}

	public void RenderGridMode(SuperFarmGame game, Point renderPos) {

		game.GLContext.glPushMatrix();

		game.GLContext.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		game.GLContext.glDisable(GL10.GL_TEXTURE_2D);

		game.GLContext.glTranslatef(renderPos.x, renderPos.y, 0.0f);

		// reset any previous scales for this render
		game.GLContext.glScalef(1.0f, 1.0f, 1.0f);

		// white
		game.GLContext.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		game.GLContext.glVertexPointer(2, GL10.GL_FLOAT, 0, this._gridModeVertexBuffer);

		game.GLContext.glDrawArrays(GL10.GL_LINES, 0, this._numberOfGridLines * 2);

		game.GLContext.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		game.GLContext.glEnable(GL10.GL_TEXTURE_2D);
		
		game.GLContext.glPopMatrix();
	}
	
	public void SetTextureCoordinates(SuperFarmGame game, Sprite sprite, int tileIndex) {

		int bufferIndex = tileIndex * 12;

		TexCoordPoints coords = sprite.GetCurrentTextureCoords(game);

		this._texCoords[bufferIndex++] = coords.TopLeft.x;
		this._texCoords[bufferIndex++] = coords.TopLeft.y;

		this._texCoords[bufferIndex++] = coords.BottomRight.x;
		this._texCoords[bufferIndex++] = coords.BottomRight.y;

		this._texCoords[bufferIndex++] = coords.BottomLeft.x;
		this._texCoords[bufferIndex++] = coords.BottomLeft.y;

		this._texCoords[bufferIndex++] = coords.TopLeft.x;
		this._texCoords[bufferIndex++] = coords.TopLeft.y;

		this._texCoords[bufferIndex++] = coords.TopRight.x;
		this._texCoords[bufferIndex++] = coords.TopRight.y;

		this._texCoords[bufferIndex++] = coords.BottomRight.x;
		this._texCoords[bufferIndex++] = coords.BottomRight.y;
	}

	public void CommitTextureCoordinateChanges(SuperFarmGame game) {

		ByteBuffer buff = ByteBuffer.allocateDirect(this._texCoords.length * 4);
		buff.order(ByteOrder.nativeOrder());
		this._texCoordBuffer = buff.asFloatBuffer();
		this._texCoordBuffer.put(this._texCoords);
		this._texCoordBuffer.position(0);
	}

	public int GetWidthInTiles() {

		return this._widthInTiles;
	}

	public int GetHeightInTiles() {

		return this._heightInTiles;
	}

	public void SetGridMode(boolean isGridMode) {

		this._isGridMode = isGridMode;
	}

	public boolean GetGridMode() {

		return this._isGridMode;
	}

	public void SetSeason(Season season, SuperFarmGame game) {

		if(season == Season.Spring) {

			this._groundBasic = this._groundBasicSpring;
		}
		else if(season == Season.Summer) {

			this._groundBasic = this._groundBasicSummer;
		}
		else if(season == Season.Autumn) {

			this._groundBasic = this._groundBasicAutumn;
		}
		else if(season == Season.Winter) {

			this._groundBasic = this._groundBasicWinter;
		}

		// ensure the new ground basic texture coordinates are commited
		this.SetupTextureCoordBuffer(game, true);
	}
}
