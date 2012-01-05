package com.googlecode.jumpnevolve.game.physic;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectFocusingCamera;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class ElectronEmitter extends AbstractObject {

	private final Timer timer;

	public ElectronEmitter(World world, Vector position, float delay) {
		super(world, ShapeFactory.createCircle(position, 1));
		this.timer = new Timer(delay);
		this.timer.start();
		this.timer.setTime(0);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.timer.didFinish()) {
			Electron e = new Electron(this.getWorld(), this.getPosition(),
					Vector.RIGHT.mul(0.1f));
			this.getWorld().add(e);
			//this.getWorld().add(new Follower(this.getWorld(), e, 2000));
			//this.getWorld().setCamera(new ObjectFocusingCamera(e));
			this.timer.start(this.timer.getStartingTime());
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.timer.poll(input, secounds);
	}

	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.getShape());
	}
}
