package com.richardhughes.superfarm;

import com.richardhughes.jx.game.framework.*;

import android.os.Bundle;

public class activityGameMain extends GameActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public GameBase CreateGame() {

		GameInformation gameInfo = new GameInformation();
		gameInfo.IsDebugMode = this.IsDebugMode();
		gameInfo.EnableTracing = false;
		gameInfo.GameTag = SuperFarmGame.TAG;
		
		return new SuperFarmGame(this, gameInfo);
	}
}