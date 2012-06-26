package com.richardhughes.superfarm;

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

	public boolean Load(String fileName, SuperFarmGame game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(SuperFarmGame.PATH_PLANTS + fileName, game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		this._id = xhelp.GetValue("/plant/id/text()");
		this._name = xhelp.GetValue("/plant/name/text()");
		this._animationFileName = xhelp.GetValue("/plant/animationfilename/text()");
		this._menuIconFileName = xhelp.GetValue("/plant/menuiconfilename/text()");

		return this._id != "";
	}
}
