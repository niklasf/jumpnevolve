package com.googlecode.jumpnevolve.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.objects.RollingBall;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;

/**
 * Ein Level, dass einer Welt entspricht, enthält zusätzlich Punktezähler und
 * ähnliches.
 * 
 * @author Erik Wagner
 * 
 */
public class Level extends World {

	private int points;
	private Timer timer = new Timer();
	private AbstractObject currentFigure;
	private Levelloader loader;
	private String avaiableFigurs = new String("all");
	private String background = "landscape.png";

	public Level(Levelloader loader, int width, int height, int subareaWidth) {
		super(width, height, subareaWidth);
		this.loader = loader;
	}

	public void setTime(float time) {
		this.timer.setTime(time);
		this.timer.start();
	}

	public void save(String path) {
		try {
			ObjectOutputStream save = new ObjectOutputStream(
					new FileOutputStream(path));
			save.writeObject(this);
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPoints() {
		return this.points;
	}

	public void addPoints(int points) {
		if (points > 0) {
			this.points += points;
		}
	}

	public void subPoints(int points) {
		if (points > 0) {
			this.points -= points;
		}
	}

	public void changeFigure(String newFigure) {
		FigureTemplate figure = null;
		if (newFigure.equals("RollingBall")) {
			figure = new RollingBall(this, currentFigure.getPosition());
		}
		// TODO: Weitere Figuren hinzufügen
		if (figure != null) {
			this.removeFromAllLists(currentFigure);
			this.add(figure);
			this.currentFigure = figure;
		}
	}

	public void setBackground(String imageFile) {
		if (imageFile.equals("default")) {
			imageFile = "landscape.png";
		}
		this.background = imageFile;
	}

	private void reload() {
		// TODO: Level neu laden
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.timer.poll(input, secounds);
		if (this.timer.didFinish()) {
			this.reload();
		}
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public void draw(Graphics g) {
		super.configScreen(g);
		GraphicUtils.drawImage(g, new Rectangle(this.getCamera().getPosition(),
				Engine.getInstance().getWidth() / zoomX, Engine.getInstance()
						.getHeight()
						/ zoomY), ResourceManager.getInstance().getImage(
				background));
		super.draw(g);
	}
}
