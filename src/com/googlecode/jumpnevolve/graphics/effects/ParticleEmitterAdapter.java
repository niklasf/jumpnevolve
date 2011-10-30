/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Adapter für {@link #ParticleEmitter}.
 *
 * @author Niklas Fiekas
 */
abstract class ParticleEmitterAdapter implements ParticleEmitter {

	public boolean completed() {
		return false;
	}

	public Image getImage() {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isOriented() {
		return false;
	}

	public void resetState() {
	}

	public void setEnabled(boolean enabled) {
		throw new UnsupportedOperationException();
	}

	public boolean useAdditive() {
		return false;
	}

	public boolean usePoints(ParticleSystem system) {
		return false;
	}

	public void wrapUp() {
	}

}
