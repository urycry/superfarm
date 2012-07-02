package com.richardhughes.superfarm;

import org.w3c.dom.NodeList;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.util.FileHelper;
import com.richardhughes.jx.util.XMLHelper;

public class GameSettings {

	// in pixels
	public int MeterSize = 0;

	// in meters
	public int FarmTileSize = 0;

	private int _startingYear = 0;
	public int GetStartingYear() { return this._startingYear; }

	private int _dayInRealSeconds = 0;
	public int GetDayInRealSeconds() { return this._dayInRealSeconds; }

	// in game hours
	private int _defaultPlantStageTime = 0;
	public int GetDefaultPlantStageTime() { return this._defaultPlantStageTime; }

	public boolean Load(GameBase game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(SuperFarmGame.PATH_GAMESETTINGS, game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		this.MeterSize = Integer.parseInt(xhelp.GetValue("/settings/metersize/text()"));
		this.FarmTileSize = Integer.parseInt(xhelp.GetValue("/settings/farmtilesize/text()"));
		this._startingYear = Integer.parseInt(xhelp.GetValue("/settings/startingyear/text()"));
		this._dayInRealSeconds = Integer.parseInt(xhelp.GetValue("/settings/dayinrealseconds/text()"));
		this._defaultPlantStageTime = Integer.parseInt(xhelp.GetValue("/settings/defaultplantstagetime/text()"));

		return true;
	}
}
