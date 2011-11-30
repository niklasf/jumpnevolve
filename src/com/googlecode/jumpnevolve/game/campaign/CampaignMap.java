package com.googlecode.jumpnevolve.game.campaign;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class CampaignMap extends AbstractState {

	private ArrayList<LevelMarker> levels = new ArrayList<LevelMarker>();
	private final NextShape size;
	private final String mapImage;
	private Camera camera;

	public CampaignMap(int width, int height, String background) {
		this.size = ShapeFactory.createRectangle(new Vector(width / 2,
				height / 2), width, height);
		this.mapImage = background;
		// FIXME: camera muss initialisiert werden
	}

	public void addLevel(String source, Vector position, int status) {
		this.levels.add(new LevelMarker(source, position, status));
	}

	public void addLevel(LevelMarker marker) {
		this.levels.add(marker);
	}

	@Override
	public void poll(Input input, float secounds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		g.scale(1.0f, 1.0f);
		Vector cameraPosition = this.camera.getPosition();
		g.translate(Engine.getInstance().getWidth() / zoomX / 2.0f
				- cameraPosition.x, Engine.getInstance().getHeight() / zoomY
				/ 2.0f - cameraPosition.y);
		this.drawBackground(g);
		for (LevelMarker marker : (LevelMarker[]) this.levels.toArray()) {
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
		// TODO Auto-generated method stub

	}

}
