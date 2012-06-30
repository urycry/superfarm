package com.richardhughes.superfarm;

public class GameTimeUpdateEventArgs {

	private Season _season = Season.Spring;
	public Season GetSeason() { return this._season; }
	public void SetSeason(Season value) { this._season = value; }
}
