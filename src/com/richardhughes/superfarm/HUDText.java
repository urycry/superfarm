package com.richardhughes.superfarm;

import org.w3c.dom.Node;

import android.graphics.Color;
import android.graphics.Point;

public class HUDText {

	private String _font = "";
	public String GetFont() { return this._font; }

	private String _text = "";
	public String GetText() { return this._text; }

	private String _renderText = "";
	public String GetRenderText() { return this._renderText; }

	private Point _pos = new Point();
	public Point GetPos() { return this._pos; }

	private float _size = 0.0f;
	public float GetSize() { return this._size; }

	private int _color = Color.BLACK;
	public int GetColor() { return this._color; }

	private SpriteFont _spriteFont = new SpriteFont();

	public boolean Load(Node node, SuperFarmGame game) {
		
		for(int i = 0; i < node.getChildNodes().getLength(); i++) {

			Node n = node.getChildNodes().item(i);

			if(n.getNodeType() == Node.ELEMENT_NODE) {

				if(n.getNodeName().equals("font")) {

					this._font = n.getTextContent();
				}
				else if(n.getNodeName().equals("text")) {

					this._text = n.getTextContent();
				}
				else if(n.getNodeName().equals("x")) {

					this._pos.x = Integer.parseInt(n.getTextContent());

					this._pos.x *= game.ScreenWidth / 100.0f;
				}
				else if(n.getNodeName().equals("y")) {

					this._pos.y = Integer.parseInt(n.getTextContent());

					this._pos.y *= game.ScreenHeight / 100.0f;
				}
				else if(n.getNodeName().equals("size")) {

					this._size = Float.parseFloat(n.getTextContent());
				}
				else if(n.getNodeName().equals("color")) {

					this._color = Color.parseColor(n.getTextContent());
				}
			}
		}

		this.SetupFont(game);

		return true;
	}

	public void Render(SuperFarmGame game) {

		this._spriteFont.DrawText(game, this._renderText, this._pos, this._size, this._color);
	}

	public void Update(SuperFarmGame game) {

		this._renderText = game.GetTokenizer().Tokenize(this._text, game);
	}

	public void Unload(SuperFarmGame game) {

		// to unload a font, a reference count will be needed to ensure that all uses of the font are unloaded before the actual font is unloaded
		//game.GetFontManager().GetFont(this._font, game)
	}

	private void SetupFont(SuperFarmGame game) {

		this._spriteFont = game.GetFontManager().GetFont(this._font, game);
	}
}
