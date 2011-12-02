package com.googlecode.jumpnevolve.game.campaign;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class CampaignMap extends AbstractState {

	private HashMap<Vector, LevelMarker> levels = new HashMap<Vector, LevelMarker>();
	private final NextShape size;
	private final String mapImage;
	private Camera camera;
	private Campaign parent;
	private LevelMarker currentLevel;

	public CampaignMap(int width, int height, String background) {
		this.size = ShapeFactory.createRectangle(new Vector(width / 2,
				height / 2), width, height);
		this.mapImage = background;
		// FIXME: camera muss initialisiert werden
	}

	public void addLevel(String source, Vector position, int status) {
		this.addLevel(new LevelMarker(source, position, status));
	}

	public void addLevel(LevelMarker marker) {
		this.levels.put(marker.position, marker);
		this.currentLevel = marker;
	}

	@Override
	public void poll(Input input, float secounds) {
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			this.startLevel();
		}
	}

	private void startLevel() {
		if (this.parent != null && this.currentLevel != null) {
			if (this.currentLevel.getStatus() != LevelMarker.STATUS_NOTAVAIBLE) {
				this.parent.start(this.currentLevel.name);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		g.scale(1.0f, 1.0f);
		Vector cameraPosition = this.camera.getPosition();
		g.translate(Engine.getInstance().getWidth() / zoomX / 2.0f
				- cameraPosition.x, Engine.getInstance().getHeight() / zoomY
				/ 2.0f - cameraPosition.y);
		this.drawBackground(g);
		for (LevelMarker marker : this.levels.values()) {
			marker.draw(g);
		}
	}

	private void drawBackground(Graphics g) {
		GraphicUtils.drawImage(g, this.size, ResourceManager.getInstance()
				.getImage(this.mapImage));
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Nichts tun
	}

	public void addConnection(Vector pos1, Vector pos2) {
		if (this.levels.containsKey(pos1) && this.levels.containsKey(pos2)) {
			LevelMarker one = this.levels.get(pos1), two = this.levels
					.get(pos2);
			new LevelConnection(one, two);
		} else {
			Log.warn("Fehlerhafte Verbindung zwischen zwei Leveln (kein Level an den entsprechenden Stellen gefunden: "
					+ pos1 + " und " + pos2);
		}
	}

	public void setParentCampaign(Campaign campaign) {
		this.parent = campaign;
	}

}
