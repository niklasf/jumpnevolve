package com.googlecode.jumpnevolve.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

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
	private Player player;
	private Interface gui;
	private Levelloader loader;
	private String background = "landscape-photo.png";

	public Level(Levelloader loader, int width, int height, int subareaWidth) {
		super(width, height, subareaWidth);
		this.loader = loader;
		this.gui = new Interface(this);
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

	public void setBackground(String imageFile) {
		if (imageFile.equals("default")) {
			imageFile = "landscape.png";
		}
		this.background = imageFile;
	}

	public void addPlayer(String avaiableFigures, String startFigure) {
		this.player = new Player(this, avaiableFigures, startFigure);
	}

	public Player getPlayer() {
		return this.player;
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
		GraphicUtils.drawImage(g, new Rectangle(new Vector(0, 0), this.width,
				this.height), ResourceManager.getInstance()
				.getImage(background));
		GraphicUtils.draw(g, new Rectangle(new Vector(0, 0), this.width,
				this.height));
		System.out.println(width + " " + height + " " + zoomX);
		super.draw(g);
	}
}
