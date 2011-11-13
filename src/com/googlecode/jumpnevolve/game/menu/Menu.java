package com.googlecode.jumpnevolve.game.menu;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * @author Erik Wagner
 * 
 */
public abstract class Menu extends AbstractState implements Interfaceable {

	protected MainGUI gui;
	protected GridContainer mainCon;
	protected HashMap<String, SubMenu> states = new HashMap<String, SubMenu>();
	protected SubMenu curState;
	private String background;

	/**
	 * Switcht zurück zum Haupt-SubMenu
	 */
	public abstract void switchBackToMainState();

	public Menu() {
		this("default");
	}

	public Menu(String background) {
		this.gui = new MainGUI(this);
		this.mainCon = new GridContainer(1, 1, GridContainer.MODUS_X_LEFT,
				GridContainer.MODUS_Y_UP);
		this.gui.setMainContainer(mainCon);
		this.setBackground(background);
	}

	public void addSubMenu(SubMenu menu) {
		if (this.curState == null) {
			this.curState = menu;
		}
		this.states.put(menu.name, menu);
		this.mainCon.add(menu, 0, 0);
	}

	public void switchTo(String name) {
		if (this.states.containsKey(name)) {
			this.curState = this.states.get(name);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		this.curState.poll(input, secounds);
	}

	@Override
	public void draw(Graphics g) {
		this.drawBackground(g);
		this.curState.draw(g);
	}

	public void drawBackground(Graphics g) {
		GraphicUtils.drawImage(g, ShapeFactory.createRectangle(
				new Vector(this.getWidth() / 2.0f, this.getHeight() / 2.0f),
				this.getWidth(), this.getHeight()), ResourceManager
				.getInstance().getImage(this.getBackgroundFile()));
	}

	/**
	 * @return Der gesamte Dateipfad aus dem das Hintergrundbild geladen wird
	 */
	public String getBackgroundFile() {
		return "backgrounds/" + this.background;
	}

	public void setBackground(String imageFile) {
		if (imageFile.equals("default")) {
			imageFile = "landscape.png";
		}
		this.background = imageFile;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Nichts tun
	}

	@Override
	public int getHeight() {
		return Engine.getInstance().getHeight();
	}

	@Override
	public int getWidth() {
		return Engine.getInstance().getWidth();
	}

	@Override
	public Camera getCamera() {
		// Eine Kamera die immer auf das Zentrum der Engine fixiert ist
		return new Camera() {
			private static final long serialVersionUID = -8443861917665677557L;

			@Override
			public Vector getPosition() {
				return new Vector(Engine.getInstance().getWidth() / 2.0f,
						Engine.getInstance().getHeight() / 2.0f);
			}
		};
	}

}
