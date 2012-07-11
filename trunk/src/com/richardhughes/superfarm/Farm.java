package com.richardhughes.superfarm;

import java.util.ArrayList;
import java.util.Hashtable;

import android.graphics.Point;
import android.util.Log;

import com.richardhughes.jx.game.framework.Quad;
import com.richardhughes.jx.game.framework.Sprite;
import com.richardhughes.jx.util.FileHelper;
import com.richardhughes.jx.util.XMLHelper;

public class Farm implements IPlantUpdateListener {

	private FarmTileMap _tilemap = new FarmTileMap();

	private Sprite _highlightSection = new Sprite();
	private boolean _renderHighlightSection = false;

	private Hashtable<String, Sprite> _data = new Hashtable<String, Sprite>();
	private ArrayList<String> _dataToRemove = new ArrayList<String>();

	private Sprite _groundSoil = new Sprite();

	private boolean _shouldPerformDataRenderCleanup = false;

	private ArrayList<Plant> _plants = new ArrayList<Plant>();
	public ArrayList<Plant> GetPlants() { return this._plants; }

	private Hashtable<Integer, PlantedPlant> _plantedPlants = new Hashtable<Integer, PlantedPlant>();
	public Hashtable<Integer, PlantedPlant> GetPlantedPlants() { return this._plantedPlants; }

	private int _plantIndex = 0;

	private Plant _currentPlant = null;
	public Plant GetCurrentPlant() { return this._currentPlant; }
	
	private Season _currentSeason = Season.Spring;
	public Season GetCurrentSeason() { return this._currentSeason; }
	public void SetCurrentSeason(Season value, SuperFarmGame game) {

		this._currentSeason = value;
		this._tilemap.SetSeason(this._currentSeason, game);
	}

	public boolean Load(SuperFarmGame game) {

		this._tilemap.Load(game);

		this.LoadConfig(game);
		
		return true;
	}

	private void LoadConfig(SuperFarmGame game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(SuperFarmGame.PATH_WORLD + "world.xml", game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);
		
		String s = "";

		s = xhelp.GetValue("/world/highlightfilename/text()");
		this._highlightSection.Load(SuperFarmGame.PATH_WORLD + s, game);
		this._highlightSection.Size.x = tileSize - 1; // don't cover the grid
		this._highlightSection.Size.y = tileSize - 1; // don't cover the grid

		s = xhelp.GetValue("/world/groundsoildfilename/text()");
		this._groundSoil.Load(SuperFarmGame.PATH_WORLD + s, game);

		this.LoadPlants(game);
	}

	private void LoadPlants(SuperFarmGame game) {

		try {

			String[] fileNames = game.CurrentApplicationContext.getAssets().list(SuperFarmGame.PATH_PLANTS_NO_SLASH);

			for(String fileName : fileNames) {

				try {

					this.LoadPlant(fileName, game);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void LoadPlant(String fileName, SuperFarmGame game) {

		Plant p = new Plant();

		if(p.Load(fileName, game)) {

			this._plants.add(p);
		}
	}

	public void Render(Camera camera, SuperFarmGame game) {

		this._tilemap.Render(camera, game);

		if(this._renderHighlightSection) {

			// TODO a scene manager to manage this kind of thing
			Quad.SetVertexBuffer(game.GLContext);
			this._highlightSection.Render(game);
			this._renderHighlightSection = false;
		}
	}

	public int PixelToTile(int pixelPosition, SuperFarmGame game) {

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);

		return pixelPosition / tileSize;
	}

	public void Update(Camera camera, SuperFarmGame game) {

		PlantedPlant [] plants = (PlantedPlant[])this._plantedPlants.values().toArray(new PlantedPlant[0]);

		for(int i = 0; i < plants.length; i++) {

			plants[i].Update(game);
		}

		int tileIndexTopLeftX = this.PixelToTile(camera._position.x, game);
		int tileIndexTopLeftY = this.PixelToTile(camera._position.y, game);

		int tileIndexBottomRightX = this.PixelToTile(camera._position.x + game.ScreenWidth, game);
		int tileIndexBottomRightY = this.PixelToTile(camera._position.y + game.ScreenHeight, game);

		boolean textureCoordsSet = false;

		this._tilemap.ResetTiles(game);

		int dataElementsRendered = 0;

		for(int x = tileIndexTopLeftX; x <= tileIndexBottomRightX + 1; x++) { // plus 1 to ensure that the tile doesn't just disappear when it goes off to the right or bottom of the screen

			for(int y = tileIndexTopLeftY; y <= tileIndexBottomRightY + 1; y++) {

				int idx = (x - tileIndexTopLeftX) + ((y - tileIndexTopLeftY) * this._tilemap.GetWidthInTiles());

				String key = this.BuildTileDataKey(x, y);

				if(this._dataToRemove.contains(key)) {

					this._tilemap.RemoveTextureCoordinates(game, idx);
					textureCoordsSet = true;
					this._data.remove(key);
				}

				if(!this._data.containsKey(key))
					continue;

				this._tilemap.SetTextureCoordinates(game, this._data.get(key), idx);

				textureCoordsSet = true;
				dataElementsRendered++;
				this._shouldPerformDataRenderCleanup = true;
			}
		}

		if(dataElementsRendered == 0 && this._shouldPerformDataRenderCleanup) {

			this._shouldPerformDataRenderCleanup = false;

			// reset the farm as otherwise any of the last data elements will hang around as
			// their texture coords will still be in the buffer, although no actually being
			// rendered. This is because when a data element is rendered,
			// FarmTileMap.CommitTextureCoordinateChanges is called

			this._tilemap.SetupTextureCoordBuffer(game);
		}

		if(textureCoordsSet) {

			this._tilemap.CommitTextureCoordinateChanges(game);
		}

		// data should all be removed now
		this._dataToRemove.clear();
	}

	public void AddPlant(Point pos, SuperFarmGame game) {

		int index = this._plantIndex++;

		int tileX = this.PixelToTile(pos.x, game);
		int tileY = this.PixelToTile(pos.y, game);

		PlantedPlant pp = new PlantedPlant();
		pp.Load(game);
		pp.SetIndex(index);
		pp.SetPlant(this._currentPlant, game);
		pp.SetPos(pos);
		pp.SetKey(this.BuildTileDataKey(tileX, tileY));
		pp.SetUpdateListener(this);

		this._plantedPlants.put(index, pp);

		this.AddItem(this._groundSoil, pos, game);
	}

	public void AddItem(Sprite sprite, Point pos, SuperFarmGame game) {

		int tileX = this.PixelToTile(pos.x, game);
		int tileY = this.PixelToTile(pos.y, game);

		String key = this.BuildTileDataKey(tileX, tileY);

		this.AddItem(sprite, key, game);
	}

	public void AddItem(Sprite sprite, String key, SuperFarmGame game) {

		if(this._data.containsKey(key)) {

			this._data.remove(key);
		}

		this._data.put(key, sprite);
	}

	public void RemoveItem(String key, SuperFarmGame game) {

		if(this._data.containsKey(key)) {

			this._dataToRemove.add(key);
		}
	}

	public String BuildTileDataKey(int x, int y) {

		return x + ":" + y;
	}

	public void AddHighlight(SuperFarmGame game, Point pos, Camera camera) {

		int tileSize = game.MetersToPixels(game.GetSettings().FarmTileSize);

		int tileIndexTopLeftX = this.PixelToTile(camera._position.x, game);
		int tileIndexTopLeftY = this.PixelToTile(camera._position.y, game);

		int xOffset = -tileSize - (camera._position.x % tileSize);
		int yOffset = -tileSize - (camera._position.y % tileSize);

		this._highlightSection.Position.x = (this.PixelToTile(pos.x, game) - tileIndexTopLeftX) * tileSize;
		this._highlightSection.Position.y = (this.PixelToTile(pos.y, game) - tileIndexTopLeftY) * tileSize;

		this._highlightSection.Position.x += xOffset;
		this._highlightSection.Position.y += yOffset;
		
		this._renderHighlightSection = true;
	}

	public void SetGridMode(boolean gridMode) {

		this._tilemap.SetGridMode(gridMode);
	}

	public void SetPlantToPlantById(String id, SuperFarmGame game) {

		id = id.trim().toLowerCase();

		this._currentPlant = null;

		for(Plant p : this._plants) {

			if(p.GetId().trim().toLowerCase().equals(id)) {

				this._currentPlant = p;
			}
		}
	}

	private void RemovePlant(int plantIndex, SuperFarmGame game) {

		PlantedPlant pp = this._plantedPlants.get(plantIndex);

		this.RemoveItem(pp.GetKey(), game);
		this._plantedPlants.remove(plantIndex);
	}

	private void UpdatePlant(int plantIndex, SuperFarmGame game) {

		PlantedPlant pp = this._plantedPlants.get(plantIndex);
		this.AddItem(pp.GetSprite(), pp.GetKey(), game);
	}

	// IPlantUpdateListener methods

	@Override
	public void OnStageTimeUpdate(PlantUpdateListenerEventArgs e, SuperFarmGame game) {

		this.UpdatePlant(e.GetIndex(), game);
game.DebugLog("Stage Changed: " + e.GetIndex());
game.DebugLog("Stage Changed: " + e.GetStageTime());
		if(e.GetStageTime() == PlantStageTime.Gone) {

			this.RemovePlant(e.GetIndex(), game);
		}
	}
}
