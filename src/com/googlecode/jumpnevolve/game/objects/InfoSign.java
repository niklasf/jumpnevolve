package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.ForegroundDrawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;
import com.googlecode.jumpnevolve.util.Parameter;

public class InfoSign extends ObjectTemplate implements ForegroundDrawable {

	private static final long serialVersionUID = -1622095244494997229L;

	private static InfoSign activeSign = null;

	private static final float SIDE_DISTANCE = Parameter.OBJECTS_INFOSIGN_SIDEDIST;
	private static final float LINE_DISTANCE = Parameter.OBJECTS_INFOSIGN_LINEDIST;
	private static final float END_INDENT = Parameter.OBJECTS_INFOSIGN_ENDINDENT;

	private static final String END_TEXT = Parameter.OBJECTS_INFOSIGN_ENDTEXT;

	private final String[] contents;
	private NextShape extendedShape;
	private boolean isPlayerCrash = false, wasPlayerCrash = false;

	public InfoSign(World world, Vector position, String[] contents) {
		super(world,
				ShapeFactory.createRectangle(position, new Vector(30, 50)),
				Masses.NO_MASS);
		this.contents = contents;
	}

	public InfoSign(World world, Vector position, String arguments) {
		this(world, position, arguments.split(","));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		wasPlayerCrash = isPlayerCrash;
		isPlayerCrash = false;
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (other instanceof PlayerFigure) {
			if (!wasPlayerCrash) {
				activeSign = this;
			}
			isPlayerCrash = true;
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/infoSign.png"));
	}

	private void drawExtended(Graphics g) {
		// ExtendedShape erzeugen
		this.createExtendedShape(g);

		// Hintergrund für den Text zeichnen
		g.translate(Engine.getInstance().getWidth() / 2.0f, Engine
				.getInstance().getHeight() / 2.0f);
		GraphicUtils.texture(g, this.extendedShape, ResourceManager
				.getInstance().getImage("textures/wood.png"), false);
		g.translate(-this.extendedShape.getBoundingRect().width / 2.0f,
				-this.extendedShape.getBoundingRect().height / 2.0f);

		// Schriftposition berechnen
		float lineHeight = g.getFont().getLineHeight();
		Vector curStringPos = new Vector(SIDE_DISTANCE, LINE_DISTANCE);

		// Text zeilenweise darstellen
		for (int i = 0; i < this.contents.length; i++) {
			GraphicUtils.drawString(g, curStringPos, this.contents[i]);
			curStringPos = curStringPos.add(new Vector(0, LINE_DISTANCE
					+ lineHeight));
		}

		// Allgemeine Endmitteilung darstellen
		curStringPos = curStringPos.add(new Vector(END_INDENT, LINE_DISTANCE
				+ lineHeight));
		GraphicUtils.drawString(g, curStringPos, END_TEXT);
	}

	private void createExtendedShape(Graphics g) {
		if (this.extendedShape == null) {
			// Mindestgröße von Breite und Höhe
			float width = END_INDENT + g.getFont().getWidth(END_TEXT);
			float height = g.getFont().getLineHeight();

			// Benötigte Größen ermitteln
			for (int i = 0; i < this.contents.length; i++) {
				width = Math.max(width, g.getFont().getWidth(this.contents[i]));
			}
			width = width + 2 * SIDE_DISTANCE;
			height = (height + LINE_DISTANCE) * (this.contents.length + 2)
					+ LINE_DISTANCE;

			// Shape erstellen
			Vector dim = new Vector(width / 2.0f, height / 2.0f);
			this.extendedShape = ShapeFactory.createRectangle(Vector.ZERO, dim);
		}
	}

	public static void drawActiveSign(Graphics g) {
		if (activeSign != null) {
			g.pushTransform();
			g.resetTransform();
			activeSign.drawExtended(g);
			g.popTransform();
		}
	}

	public static void disableActiveSign() {
		activeSign = null;
	}

	public static boolean isSignActive() {
		return activeSign != null;
	}
}
