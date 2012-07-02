package com.richardhughes.superfarm;

import java.util.Hashtable;

import com.richardhughes.jx.util.FileHelper;
import com.richardhughes.jx.util.XMLHelper;

public class Plant {

	private String _id = "";
	public String GetId() { return this._id; }

	private String _name = "";
	public String GetName() { return this._name; }

	private String _animationFileName = "";
	public String GetAnimationFileName() { return this._animationFileName; }

	private String _menuIconFileName = "";
	public String GetMenuIconFileName() { return this._menuIconFileName; }

	private Hashtable<PlantStageTime, Float> _stageTimings = new Hashtable<PlantStageTime, Float>();
	public Hashtable<PlantStageTime, Float> GetStageTimings() { return this._stageTimings; }

	public boolean Load(String fileName, SuperFarmGame game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(SuperFarmGame.PATH_PLANTS + fileName, game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		this._id = xhelp.GetValue("/plant/id/text()");
		this._name = xhelp.GetValue("/plant/name/text()");
		this._animationFileName = xhelp.GetValue("/plant/animationfilename/text()");
		this._menuIconFileName = xhelp.GetValue("/plant/menuiconfilename/text()");

		this._stageTimings.put(PlantStageTime.Seed, this.ReadStageTime(xhelp, PlantStageTime.Seed, game));
		this._stageTimings.put(PlantStageTime.Seedling, this.ReadStageTime(xhelp, PlantStageTime.Seedling, game));
		this._stageTimings.put(PlantStageTime.Flower, this.ReadStageTime(xhelp, PlantStageTime.Flower, game));
		this._stageTimings.put(PlantStageTime.Fruit, this.ReadStageTime(xhelp, PlantStageTime.Fruit, game));
		this._stageTimings.put(PlantStageTime.Dead, this.ReadStageTime(xhelp, PlantStageTime.Dead, game));
		this._stageTimings.put(PlantStageTime.Gone, 0.0f);

		return this._id != "";
	}

	private float ReadStageTime(XMLHelper xhelp, PlantStageTime stage, SuperFarmGame game) {

		float f = Float.parseFloat(xhelp.GetValue("/plant/stagetimes/" + stage.toString().toLowerCase() + "/text()"));

		return f > 0.0f ? f : game.GetSettings().GetDefaultPlantStageTime();
	}
}
