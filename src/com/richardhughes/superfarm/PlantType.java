package com.richardhughes.superfarm;

import org.w3c.dom.Node;

public class PlantType {

	private String _name = "";
	public String GetName() { return this._name; }
	public void SetName(String value) { this._name = value; }

	private String _id = "";
	public String GetId() { return this._id; }
	public void SetId(String value) { this._id = value; }

	private String _description = "";
	public String GetDescription() { return this._description; }
	public void SetDescription(String value) { this._description = value; }

	private String _menuIconFileName = "";
	public String GetMenuIconFileName() { return this._menuIconFileName; }
	public void SetMenuIconFileName(String value) { this._menuIconFileName = value; }

	public boolean Load(Node node) {

		return true;
	}
}
