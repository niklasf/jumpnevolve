package com.googlecode.jumpnevolve.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.menu.Menu;
import com.googlecode.jumpnevolve.game.player.Player;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.effects.FireEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.world.World;
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
	private Levelloader loader;
	private boolean finished = false;
	private ParticleEffect finishedEffect;

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
			System.out.println("Fehler beim Speichern des Levels\n");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Fehler beim Speichern des Levels\n");
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

	public void addPlayer(Vector position, String avaiableFigures,
			String startFigure) {
		this.addPlayer(null, position, avaiableFigures, startFigure);
	}

	public void addPlayer(Menu parent, Vector position, String avaiableFigures,
			String startFigure) {
		this.player = new Player(parent, this, position, avaiableFigures,
				startFigure, true);
	}

	public Player getPlayer() {
		return this.player;
	}

	private void reload() {
		// TODO: Level neu laden
	}

	private void pollPlayer(Input input, float secounds) {
		if (this.player != null) {
			this.player.poll(input, secounds);
			if (this.finished) {
				this.player.getFigure().startRound(input);
				this.player.getFigure().jump();
				this.player.getFigure().poll(input, secounds);
				this.player.getFigure().endRound();
			}
		}
	}

	private void drawPlayer(Graphics g) {
		if (this.player != null) {
			this.player.draw(g);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		// Nicht weitersimulieren, wenn das Ziel erreicht wurde
		if (!this.finished) {
			super.poll(input, secounds);
			this.pollPlayer(input, secounds);
			this.timer.poll(input, secounds);
			if (this.timer.didFinish()) {
				this.reload();
			}
		} else {
			// Nur der Spieler bewegt sich noch und springt ständig
			this.pollPlayer(input, secounds);
			if (this.finishedEffect != null) {
				this.finishedEffect.poll(input, secounds);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (this.finished) {
			this.drawFinishedScreen(g);
		}
		this.drawPlayer(g);
	}

	public void finish() {
		this.finished = true;
		this.player.onFinish();
		this.finishedEffect = new ParticleEffect(
				this.getCamera().getPosition(), new FireEmitterFactory());
	}

	public boolean isFinished() {
		return this.finished;
	}

	private void drawFinishedScreen(Graphics g) {
		if (this.finishedEffect != null) {
			this.finishedEffect.draw(g);
		}
		GraphicUtils.drawString(
				g,
				this.getCamera()
						.getPosition()
						.sub(new Vector(Engine.getInstance().getWidth(), Engine
								.getInstance().getHeight()).div(4.0f)),
				"Level erfolgreich beendet");
	}
}
