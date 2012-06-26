package com.richardhughes.superfarm;

import org.w3c.dom.Node;

import com.richardhughes.jx.game.framework.GameBase;
import com.richardhughes.jx.game.framework.Quad;

public class Image {

	private String _fileName = "";
	private int _textureId = -1;

	private String _highlightFileName = "";
	private int _highlightTextureId = -1;

	private String _actionClick = "";
	private String _actionMove = "";
	private String _actionUp = "";

	private boolean _isHighlighted = false; 
	
	private Quad _renderQuad = new Quad();

	public boolean Load(Node node, GameBase game) {
		
		for(int i = 0; i < node.getChildNodes().getLength(); i++) {

			Node n = node.getChildNodes().item(i);

			if(n.getNodeType() == Node.ELEMENT_NODE) {

				if(n.getNodeName().equals("filename")) {

					this._fileName = n.getTextContent();
				}
				else if(n.getNodeName().equals("highlightfilename")) {

					this._highlightFileName = n.getTextContent();
				}
				else if(n.getNodeName().equals("x")) {

					float x = Integer.parseInt(n.getTextContent());

					x *= game.ScreenWidth / 100.0f;

					this._renderQuad.Position.x = (int)x;
				}
				else if(n.getNodeName().equals("y")) {

					float y = Integer.parseInt(n.getTextContent());

					y *= game.ScreenHeight / 100.0f;

					this._renderQuad.Position.y = (int)y;
				}
				else if(n.getNodeName().equals("width")) {

					float width = Integer.parseInt(n.getTextContent());

					width *= game.ScreenWidth / 100.0f;

					this._renderQuad.Width = (int)width;
				}
				else if(n.getNodeName().equals("height")) {

					float height = Integer.parseInt(n.getTextContent());

					height *= game.ScreenHeight / 100.0f;

					this._renderQuad.Height = (int)height;
				}
				else if(n.getNodeName().equals("action_click")) {

					this._actionClick = n.getTextContent();
				}
				else if(n.getNodeName().equals("action_move")) {

					this._actionMove = n.getTextContent();
				}
				else if(n.getNodeName().equals("action_up")) {

					this._actionUp = n.getTextContent();
				}
			}
		}

		this._textureId = game.TextureManager.LoadTexture(this._fileName, game.CurrentApplicationContext);
		if(this._highlightFileName.length() > 0)
			this._highlightTextureId = game.TextureManager.LoadTexture(this._highlightFileName, game.CurrentApplicationContext);

		this._renderQuad.RequiresBlending = true;

		this.SetHighlight(false);
		
		return true;
	}

	public void Unload(GameBase game) {

		game.TextureManager.FreeTexture(this._fileName);

		if(this._highlightFileName.length() > 0)
			game.TextureManager.FreeTexture(this._highlightFileName);

		this._textureId = -1;
		this._highlightTextureId = -1;
		this._renderQuad.TextureId = -1;
	}

	public void Render(GameBase game) {

		this._renderQuad.Render(game.GLContext);
	}

	public int GetX() {

		return this._renderQuad.Position.x;
	}

	public int GetY() {

		return this._renderQuad.Position.y;
	}

	public int GetWidth() {

		return this._renderQuad.Width;
	}

	public int GetHeight() {

		return this._renderQuad.Height;
	}

	public void SetHighlight(boolean b) {

		this._isHighlighted = this._highlightTextureId != -1 && b; // if there is no highlight texture id, no highlighted texture can be set

		if(this._isHighlighted) {

			this._renderQuad.TextureId = this._highlightTextureId;
		}
		else {

			this._renderQuad.TextureId = this._textureId;
		}
	}

	public String GetAction(ImageActionType type) {

		String action = "";

		if(type == ImageActionType.Click) {

			action = this._actionClick;
		}
		else if(type == ImageActionType.Move) {

			action = this._actionMove;
		}
		else if(type == ImageActionType.Up) {

			action = this._actionUp;
		}

		return action;
	}
}
