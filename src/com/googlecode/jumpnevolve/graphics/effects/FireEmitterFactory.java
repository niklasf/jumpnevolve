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

import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.effects.FireEmitter;

/**
 * Erzeugt einen Feuereffekt.
 * 
 * @author Niklas Fiekas
 */
public class FireEmitterFactory implements ParticleEmitterFactory {

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new FireEmitter(0, 0, 30.0f);
	}

}
