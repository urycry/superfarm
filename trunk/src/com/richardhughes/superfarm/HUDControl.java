package com.richardhughes.superfarm;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.util.FileHelper;
import com.richardhughes.jx.util.XMLHelper;

public class HUDControl {

	private HUD _hud = null;

	private ArrayList<Image> _images = new ArrayList<Image>();
	public ArrayList<Image> GetImages() { return this._images; }

	private int _lastPressedImageIndex = -1;

	private String _id = "";
	public String GetId() { return this._id; }
	public void SetId(String value) { this._id = value; }

	private boolean _isVisible = true;
	public boolean GetIsVisible() { return this._isVisible; }
	public void SetIsVisible(boolean value) { this._isVisible = value; }
	
	public HUDControl(HUD hud)
		throws NullPointerException
	{
		if(hud == null)
			throw new NullPointerException();

		this._hud = hud;
	}

	public boolean Load(String fileName, SuperFarmGame game) {

		FileHelper rh = new FileHelper();
		String fileData = rh.GetAssetData(fileName, game.CurrentApplicationContext);

		XMLHelper xhelp = new XMLHelper();
		xhelp.SetXMLData(fileData);

		this._id = xhelp.GetValue("/hudcontrol/id/text()");

		String v = xhelp.GetValue("/hudcontrol/isvisible/text()");
		this.SetIsVisible(!v.trim().toLowerCase().equals("false")); // default to visible

		NodeList controls = xhelp.GetValues("/hudcontrol/image");

		for(int i = 0; i < controls.getLength(); i++) {

			Node n = controls.item(i);

			this.LoadImage(n, game);
		}

		return true;
	}

	public boolean LoadImage(Node nodeImage, SuperFarmGame game) {
		
		Image i = new Image();

		i.Load(nodeImage, game);
		this._images.add(i);

		return true;
	}
	
	public void Unload(GameBase game) {

		for(Image i : this._images) {

			i.Unload(game);
		}
	}
	
	public void Render(GameBase game) {

		if(!this.GetIsVisible())
			return;

		for(Image i : this._images) {

			i.Render(game);
		}
	}

	public void OnTouchDown(int x, int y) {

		if(!this.GetIsVisible())
			return;

		int index = 0;
		for(Image i : this._images) {

			if(this.IsImageWithBounds(x, y, i)) {

				this._lastPressedImageIndex = index;
				
				this.ImageAction(i, ImageActionType.Click);

				i.SetHighlight(true);
			}
			else {

				if(index == this._lastPressedImageIndex) {
					
					this.ImageAction(i, ImageActionType.Up);

					this._lastPressedImageIndex = -1;
				}

				i.SetHighlight(false);
			}

			index++;
		}
	}

	public void OnTouchUp(int x, int y) {

		if(!this.GetIsVisible())
			return;

		int index = 0;
		for(Image i : this._images) {

			if(index == this._lastPressedImageIndex) {
				
				this.ImageAction(i, ImageActionType.Up);

				this._lastPressedImageIndex = -1;
			}

			i.SetHighlight(false);

			index++;
		}
	}

	public boolean IsImageWithBounds(int x, int y, Image i) {

		int ix = i.GetX();
		int iy = i.GetY();
		int w = i.GetWidth();
		int h = i.GetHeight();

		return x >= ix && x <= ix + w &&
				y >= iy && y <= iy + h;
	}

	public void ImageAction(Image i, ImageActionType type) {

		this._hud.OnImageAction(i.GetAction(type), i.GetId());
	}

	public void AddImage(Image i) {

		this._images.add(i);
	}
}
