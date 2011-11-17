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

	private static final float SIDE_DISTANCE = Parameter.OBJECTS_INFOSIGN_SIDEDIST;
	private static final float LINE_DISTANCE = Parameter.OBJECTS_INFOSIGN_LINEDIST;
	private final String[] contents;
	private boolean extended = false;
	private NextShape extendedShape;

	public InfoSign(World world, Vector position, String[] contents) {
		super(world,
				ShapeFactory.createRectangle(position, new Vector(50, 50)),
				Masses.NO_MASS);
		this.contents = contents;
	}

	public InfoSign(World world, Vector position, String arguments) {
		this(world, position, arguments.split(","));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		this.extended = false;
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (other instanceof PlayerFigure) {
			this.extended = true;
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/infoSign.png"));
		if (this.extended) {
			g.pushTransform();
			g.resetTransform();
			g.translate(Engine.getInstance().getWidth() / 2.0f, Engine
					.getInstance().getHeight() / 2.0f);
			this.drawExtended(g);
			g.popTransform();
		}
	}

	private void drawExtended(Graphics g) {
		this.createExtendedShape(g);
		GraphicUtils.texture(g, this.extendedShape, ResourceManager
				.getInstance().getImage("textures/wood.png"), true);
		for (int i = 0; i < this.contents.length; i++) {
			GraphicUtils.drawString(g, new Vector(SIDE_DISTANCE, LINE_DISTANCE
					* (i + 1) + g.getFont().getLineHeight() * i),
					this.contents[i]);
		}
	}

	private void createExtendedShape(Graphics g) {
		if (this.extendedShape == null) {
			float width = 0, height = 0;
			height = g.getFont().getLineHeight();
			for (int i = 0; i < this.contents.length; i++) {
				width = Math.max(width, g.getFont().getWidth(this.contents[i]));
			}
			width = width + 2 * SIDE_DISTANCE;
			height = (height + LINE_DISTANCE) * this.contents.length
					+ LINE_DISTANCE;
			Vector dim = new Vector(width / 2.0f, height / 2.0f);
			this.extendedShape = ShapeFactory.createRectangle(dim, dim);
		}
	}
}
