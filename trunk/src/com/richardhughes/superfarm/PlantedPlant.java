package com.richardhughes.superfarm;

import android.graphics.Point;

public class PlantedPlant {

	private int _index = 0;
	public int GetIndex() { return this._index; }
	public void SetIndex(int value) { this._index = value; }

	private Plant _plant = null;
	public Plant GetPlant() { return this._plant; }
	public void SetPlant(Plant value) { this._plant = value; }

	private Point _pos = new Point();
	public Point GetPos() { return this._pos; }
	public void SetPos(Point value) { this._pos = value; }

	private PlantStageTime _stageTime = PlantStageTime.Seed;
	public PlantStageTime GetStageTime() { return this._stageTime; }
	public void SetStageTime(PlantStageTime value) { this._stageTime = value; }

	private float _currentHourCount = 0;

	private IPlantUpdateListener _updateListener = null;
	public IPlantUpdateListener GetUpdateListener() { return this._updateListener; }
	public void SetUpdateListener(IPlantUpdateListener value) { this._updateListener = value; }

	public void Update(SuperFarmGame game) {

		// we want to get the precise number of hours passed in case a stage is only 30 in game minutes for instance
		this._currentHourCount += game.GetGameTime().GetMinutes() / 60.0f;

		if(this._currentHourCount > this._plant.GetStageTimings().get(this._stageTime) * 60.0f) { // convert the stage timing hours to minutes

			this._currentHourCount = 0.0f;

			if(this._stageTime == PlantStageTime.Seed)
				this._stageTime = PlantStageTime.Seedling;
			else if(this._stageTime == PlantStageTime.Seedling)
				this._stageTime = PlantStageTime.Flower;
			else if(this._stageTime == PlantStageTime.Flower)
				this._stageTime = PlantStageTime.Fruit;
			else if(this._stageTime == PlantStageTime.Fruit)
				this._stageTime = PlantStageTime.Dead;
			else if(this._stageTime == PlantStageTime.Dead)
				this._stageTime = PlantStageTime.Gone;

			if(this._updateListener != null) {

				PlantUpdateListenerEventArgs e = new PlantUpdateListenerEventArgs();
				e.SetIndex(this._index);
				e.SetStageTime(this._stageTime);

				this._updateListener.OnStageTimeUpdate(e);
			}
		}
	}
}
