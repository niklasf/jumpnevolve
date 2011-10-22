/**
 * 
 */
package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Kamera die auf ein Objekt fokusiert ist, gleichzeitig aber keine
 * Bereiche außerhalb der Welt zeigt
 * 
 * @author Erik Wagner
 * 
 */
public class LimitedObjectFocusingCamera extends ObjectFocusingCamera implements
		LimitedCamera {

	private static final long serialVersionUID = -4863095708326682508L;

	public LimitedObjectFocusingCamera(AbstractObject object) {
		super(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.game.LimitedCamera#limitPosition(com.googlecode
	 * .jumpnevolve.math.Vector,
	 * com.googlecode.jumpnevolve.graphics.world.World)
	 */
	@Override
	public Vector limitPosition(Vector position, World world) {
		int halfScreenWidth = Engine.getInstance().getWidth() / 2, halfScreenHeight = Engine
				.getInstance().getHeight() / 2;
		Vector re = position;
		if (position.x < halfScreenWidth) {
			re = re.modifyX(halfScreenWidth);
		}
		if (position.y < halfScreenHeight) {
			re = re.modifyY(halfScreenHeight);
		}
		if (position.x > world.width - halfScreenWidth) {
			re = re.modifyX(world.width - halfScreenWidth);
		}
		if (position.y > world.height - halfScreenHeight) {
			re = re.modifyY(world.height - halfScreenHeight);
		}
		return re;
	}

	@Override
	public Vector getPosition() {
		return limitPosition(super.getPosition(), this.object.getWorld());
	}

}
