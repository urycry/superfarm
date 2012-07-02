package com.richardhughes.superfarm;

public interface IHUDActionListener {

	public void actionDPAD_CENTER(HUDActionListenerEventArgs e);
	public void actionDPAD_CENTER_UP(HUDActionListenerEventArgs e);
	public void actionDPAD_LEFT(HUDActionListenerEventArgs e);
	public void actionDPAD_RIGHT(HUDActionListenerEventArgs e);
	public void actionDPAD_UP(HUDActionListenerEventArgs e);
	public void actionDPAD_DOWN(HUDActionListenerEventArgs e);
	public void actionBUTTON_MENU(HUDActionListenerEventArgs e);
	public void actionBUTTON_EXIT(HUDActionListenerEventArgs e);
	public void actionBUTTON_PLANT(HUDActionListenerEventArgs e);
	public void actionBUTTON_FINISH(HUDActionListenerEventArgs e);
	public void actionBUTTON_RETURNFROMMENU(HUDActionListenerEventArgs e);
	public void actionBUTTON_RETURNFROMPLANTMENU(HUDActionListenerEventArgs e);
	public void actionPLANT_SELECTED(HUDActionListenerEventArgs e);
	public void actionBUTTON_SAVE(HUDActionListenerEventArgs e);
}
