package com.richardhughes.superfarm;

public class PlantUpdateListenerEventArgs {

	private int _index = 0;
	public int GetIndex() { return this._index; }
	public void SetIndex(int value) { this._index = value; }

	private PlantStageTime _stageTime = PlantStageTime.Seed;
	public PlantStageTime GetStageTime() { return this._stageTime; }
	public void SetStageTime(PlantStageTime value) { this._stageTime = value; }
}
