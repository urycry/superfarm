package com.richardhughes.superfarm;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.DebugUtils;
import android.util.Log;

import com.richardhughes.jx.game.framework.*;

public class SuperFarmGame extends GameBase implements IHUDActionListener {

	public final static String TAG = "superfarm";

	public final static String PATH_GAMESETTINGS = "gamesettings.xml";
	
	public final static String PATH_FONTS = "Fonts/";
	public final static String PATH_ACTORS = "Actors/";
	public final static String PATH_WORLD = "World/";
	public final static String PATH_PLANTS = "World/Plants/";
	public final static String PATH_PLANTS_NO_SLASH = "World/Plants";

	public final static int ACTIVITY_RESULT_MENU = 0;
	
	private GameSettings _settings = new GameSettings();

	private Actor _farmer = new Actor();
	private Farm _farm = new Farm();
	private Camera _camera = new Camera();
	private HUD _hud = new HUD();

	private SpriteFont _debugFont = new SpriteFont();

	private ArrayList<String> _debugMessages = new ArrayList<String>();

	private GameMode _gameMode = GameMode.Normal;

	private GameTime _gameTime = new GameTime();

	public SuperFarmGame(Context context, GameInformation gameInfo) {
		super(context, gameInfo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void OnLoad() {

		this._settings.Load(this);

		this._gameTime.Load(this._settings.GetStartingYear(), this._settings.GetDayInRealSeconds());
		
		this._camera.Load(this);
	}

	@Override
	public void OnLoadResources(GL10 gl) {

		this._debugFont.Load(this, SuperFarmGame.PATH_FONTS + "debug.fnt");
		
		this._farmer.Load(SuperFarmGame.PATH_ACTORS + "farmer.xml", this);

		this._farm.Load(this);

		this._hud.Load("HUD/HUD.xml", this);
		this._hud.SetActionListener(this);

		this.AddPlantImagesToPlantMenu("plant_menu");
	}

	@Override
	public void OnUpdate(GL10 gl) {

		// when viewing the menu, the game is effectively paused
		if(this._gameMode == GameMode.Menu)
			return;

		this._gameTime.Update(this);
		this.DebugPrint("Time : " + this._gameTime.GetHours() + ":" + this._gameTime.GetMinutes() + ":" + this._gameTime.GetSeconds(), false);

		this._farmer.Update(this);

		this._camera.Focus(this, this._farmer);

		this._farm.Update(this._camera, this);

		if(this._gameMode == GameMode.Plant) {

			this._farm.AddHighlight(this, this._farmer.GetPosition(), this._camera);
		}
	}

	@Override
	public void OnRender(GL10 gl) {

		this._farm.Render(this._camera, this);

		Quad.SetVertexBuffer(gl);
		this._farmer.Render(this, this._camera);

		this._hud.Render(this);

		//this.DebugPrint("FPS: " + this.FPS, false);
		this.DebugPrint(this._gameTime.GetDay() + " - " + this._gameTime.GetDayOfMonth() + "/" + this._gameTime.GetMonth() + "/" + this._gameTime.GetYear() + " - " + this._gameTime.GetHours() + ":" + this._gameTime.GetMinutes() + ":" + this._gameTime.GetSeconds());

		this.RenderDebugMessages();
	}

	private void RenderDebugMessages() {

		Point p = new Point();
		p.y = 40;

		for(String message : this._debugMessages) {

			this._debugFont.DrawText(this, message, p, 3.0f, Color.WHITE);

			p.y += 30;
		}

		this._debugMessages.clear();
	}
	
	@Override
	public void OnUnLoadResoureces(GL10 gl) {

		this._hud.SetActionListener(null);
		this._hud.Unload(this);

		this._debugFont.Unload(this);
	}

	@Override
	public void OnShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnTouchDown(int x, int y) {

		this._hud.OnTouchDown(x, y);
	}

	@Override
	public void OnTouchUp(int x, int y) {

		this._hud.OnTouchUp(x, y);
	}

	@Override
	public void actionDPAD_CENTER(HUDActionListenerEventArgs e) {

		this.Action();
	}

	@Override
	public void actionDPAD_LEFT(HUDActionListenerEventArgs e) {

		this._farmer.Move(ActorDirection.West, this);
	}

	@Override
	public void actionDPAD_RIGHT(HUDActionListenerEventArgs e) {

		this._farmer.Move(ActorDirection.East, this);
	}

	@Override
	public void actionDPAD_UP(HUDActionListenerEventArgs e) {

		this._farmer.Move(ActorDirection.North, this);
	}

	@Override
	public void actionDPAD_DOWN(HUDActionListenerEventArgs e) {

		this._farmer.Move(ActorDirection.South, this);
	}

	@Override
	public void actionBUTTON_MENU(HUDActionListenerEventArgs e) {

		this._hud.ShowControl("menu");
		this._hud.HideControl("dpad");
		this._hud.HideControl("buttonMenu");
		this._hud.HideControl("info_bar");

		this._gameMode = GameMode.Menu;
	}

	@Override
	public void actionBUTTON_EXIT(HUDActionListenerEventArgs e) {

		((Activity)this.CurrentApplicationContext).finish();
	}

	@Override
	public void actionBUTTON_PLANT(HUDActionListenerEventArgs e) {

		this._hud.HideControl("button_menu");
		this._hud.ShowControl("plant_menu");

		this.CloseMenu();
	}

	@Override
	public void actionBUTTON_FINISH(HUDActionListenerEventArgs e) {

		this._gameMode = GameMode.Normal;

		this._farm.SetGridMode(false);

		this._hud.ShowControl("button_menu");
		this._hud.HideControl("button_finish");

		this.CloseMenu();
	}

	@Override
	public void actionBUTTON_RETURNFROMMENU(HUDActionListenerEventArgs e) {

		this._gameMode = GameMode.Normal;

		this._hud.ShowControl("button_menu");

		this.CloseMenu();
	}

	@Override
	public void actionBUTTON_RETURNFROMPLANTMENU(HUDActionListenerEventArgs e) {

		this._hud.HideControl("plant_menu");
		this._hud.ShowControl("menu");
	}

	@Override
	public void actionPLANT_SELECTED(HUDActionListenerEventArgs e) {

		String plantId = e.GetImageId().trim().toLowerCase();

		if(plantId.equals(""))
			return;

		this._gameMode = GameMode.Plant;

		this._farm.SetGridMode(true);

		this.CloseMenu();

		this._hud.HideControl("plant_menu");
		this._hud.HideControl("buttonMenu");

		this._hud.ShowControl("button_finish");

		this._farm.SetPlantToPlantById(plantId, this);
	}

	private void CloseMenu() {

		this._hud.HideControl("menu");
		this._hud.ShowControl("dpad");
		this._hud.ShowControl("buttonMenu");
		this._hud.ShowControl("info_bar");
	}
	
	public void DebugPrint(String message) {

		this.DebugPrint(message, false);
	}

	public void DebugPrint(String message, boolean printToLog) {

		this._debugMessages.add(message);

		if(printToLog)
			this.DebugLog(message);		
	}
	
	public void DebugLog(String message) {

		StackTraceElement stack = Thread.currentThread().getStackTrace()[3]; // 3 will be the caller of the 'DebugLog' method

		String callerInfo = stack.getFileName() + " - " + stack.getClassName() + "." + stack.getMethodName() + ":" + stack.getLineNumber();

		Log.d(this.GameInfo.GameTag, "DebugLog: " + callerInfo);
		Log.d(this.GameInfo.GameTag, "DebugLog: " + message);
	}

	public GameSettings GetSettings() {

		return this._settings;
	}

	public int MetersToPixels(float meters) {

		return (int)(meters * this._settings.MeterSize);
	}

	private void Action() {

		if(this._gameMode == GameMode.Plant) {

			this.Action_Plant();
		}
	}

	private void Action_Plant() {

		this._farm.AddItem(this._farmer.GetPosition(), this);
	}

	private void AddPlantImagesToPlantMenu(String id) {

		int x = 8;
		int y = 7;

		int idx = 0;
		for(Plant p : this._farm.GetPlants()) {

			Image i = new Image();
			i.Load(p.GetId(), p.GetMenuIconFileName(), x, y, 25, 25, this);
			i.SetActionUp("plant_selected");

			this._hud.AddImageToControl(id, i);

			x += 25 + 4; // 4 is the spacing

			idx++;

			if(idx % 3 == 0) {

				x = 8;
				y += 25 + 4;
			}
		}
	}
}
