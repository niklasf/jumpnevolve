package com.googlecode.jumpnevolve.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.game.menu.Menu;
import com.googlecode.jumpnevolve.game.objects.InfoSign;
import com.googlecode.jumpnevolve.game.player.Player;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.effects.FogEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.effects.SprayEmitterFactory;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Ein Level, dass einer Welt entspricht, enthält zusätzlich Punktezähler und
 * ähnliches.
 * 
 * @author Erik Wagner
 * 
 */
public class Level extends World {

	private static final float RELOAD_DELAY = Parameter.GAME_LEVEL_RELOADDELAY;

	private int points;
	private Timer timer = new Timer();
	private Player player;
	private Levelloader loader;
	private boolean finished = false;
	private ParticleEffect finishedEffect;
	private boolean failed = false;
	private ParticleEffect failedEffect;

	public Level(Levelloader loader, int width, int height, int subareaWidth) {
		super(width, height, subareaWidth);
		this.loader = loader;
		InfoSign.disableActiveSign();
	}

	/**
	 * Setzt die für das Level verfügbare Zeit. Nach Ablauf der Zeit wird das
	 * Level neugestartet.
	 * <p>
	 * Eine verfügbare Zeit von <strong>0 Sekunden</strong> bedeutet, dass
	 * unendlich viel Zeit zur Verfügung steht.
	 * 
	 * @param time
	 *            Die verfügbare Zeit in Sekunden
	 */
	public void setTime(float time) {
		this.timer.setTime(time);
		if (time != 0) {
			this.timer.start();
		} else {
			// Sicherstellen das der Timer nicht läuft
			this.timer.stop();
		}
	}

	public void save(String path) {
		try {
			ObjectOutputStream save = new ObjectOutputStream(
					new FileOutputStream(path));
			save.writeObject(this);
			save.close();
		} catch (FileNotFoundException e) {
			Log.error("Fehler beim Speichern des Levels\n");
			e.printStackTrace();
		} catch (IOException e) {
			Log.error("Fehler beim Speichern des Levels\n");
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
		Log.info("Reload Level");
		Level newLevel = Levelloader.asyncLoadLevel(this.loader.source);
		newLevel.player.setParentMenu(this.player.getParentMenu());
		Engine.getInstance().switchState(newLevel);
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
		// Level ignoriert Lags und rechnet dann einfach nicht weiter
		if (secounds < 1 / (float) Parameter.GAME_FPS_MINIMUM) {
			// Spiel pausieren, solange ein Info-Schild angezeigt wird
			if (InfoSign.isSignActive()) {
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
						|| input.isKeyDown(Input.KEY_ENTER)) {
					// Info-Schild ausschalten, wenn die linke Maustaste
					// gedrückt wurde
					InfoSign.disableActiveSign();
				}
			} else {
				// Nicht weitersimulieren, wenn das Ziel erreicht wurde
				if (!this.finished) {
					super.poll(input, secounds);
					this.pollPlayer(input, secounds);
					this.timer.poll(input, secounds);
					if (this.timer.didFinish()) {
						if (!this.failed) {
							this.failed();
						} else {
							this.reload();
						}
					}
				} else {
					// Nur der Spieler bewegt sich noch und springt ständig
					this.pollPlayer(input, secounds);
					InfoSign.disableActiveSign();
				}
				if (this.finishedEffect != null) {
					this.finishedEffect.poll(input, secounds);
				}
				if (this.failedEffect != null) {
					this.failedEffect.poll(input, secounds);
				}
			}
		} else {
			Log.warn("Lag detektiert! : thisFrameSec = " + secounds
					+ " maxFrameSec = " + 1
					/ (float) Parameter.GAME_FPS_MINIMUM);
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (this.failed) {
			this.drawFailedScreen(g);
		} else {
			this.drawRemainingTime(g);
		}
		if (this.finished) {
			this.drawFinishedScreen(g);
		} else {
			InfoSign.drawActiveSign(g);
		}
		this.drawPlayer(g);
	}

	private void drawRemainingTime(Graphics g) {
		GraphicUtils.drawString(
				g,
				this.getCamera()
						.getPosition()
						.sub(new Vector(Engine.getInstance().getWidth(), Engine
								.getInstance().getHeight() * 1.5f).div(3.0f)),
				"Verbleibende Zeit: " + (int) this.timer.getRemainingTime());
	}

	private void drawFailedScreen(Graphics g) {
		this.createFailedScreen();
		this.failedEffect.draw(g);
		GraphicUtils.drawString(
				g,
				this.getCamera()
						.getPosition()
						.sub(new Vector(Engine.getInstance().getWidth(), Engine
								.getInstance().getHeight()).div(4.0f)),
				"Die Zeit ist abgelaufen: Das Level wird in "
						+ (int) this.timer.getRemainingTime()
						+ " Sekunden neugestartet");
	}

	private void createFailedScreen() {
		if (this.failedEffect == null) {
			this.failedEffect = new ParticleEffect(this.getCamera()
					.getPosition(), new FogEmitterFactory());
		}
	}

	// TODO: Statistik erstellen und anzeigen
	/**
	 * Setzt den Status des Levels als beendet. Es erscheint ein Beenden-Dialog,
	 * durch den man zum Hauptmenü zurückkehren kann.
	 */
	public void finish() {
		this.finished = true;
		this.player.onFinish();
	}

	/**
	 * Setzt den Status des Levels als verloren. Das Level wird daraufhin nach
	 * einem zeitlichen Delay neugestartet.
	 */
	public void failed() {
		this.failed = true;
		this.timer.start(RELOAD_DELAY);
	}

	public boolean isFinished() {
		return this.finished;
	}

	private void drawFinishedScreen(Graphics g) {
		this.createFinishedScreen();
		this.finishedEffect.draw(g);
		GraphicUtils.drawString(
				g,
				this.getCamera()
						.getPosition()
						.sub(new Vector(Engine.getInstance().getWidth(), Engine
								.getInstance().getHeight()).div(4.0f)),
				"Level erfolgreich beendet");
	}

	private void createFinishedScreen() {
		if (this.finishedEffect == null) {
			this.finishedEffect = new ParticleEffect(this.getCamera()
					.getPosition(), new SprayEmitterFactory());
		}
	}
}
