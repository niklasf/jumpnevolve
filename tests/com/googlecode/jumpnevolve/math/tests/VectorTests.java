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

package com.googlecode.jumpnevolve.math.tests;

import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Niklas Fiekas
 */
public class VectorTests {

	/**
	 * Test method for {@link com.googlecode.jumpnevolve.math.Vector#abs()}.
	 */
	@Test
	public void testAbs() {
		assertThat(Vector.ZERO.abs(), is(0.0f));
		assertThat(Vector.RIGHT.abs(), is(1.0f));
		assertThat(new Vector(3, 4).abs(), is(5.0f));
		assertThat(new Vector(-1, 0).abs(), is(1.0f));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#add(com.googlecode.jumpnevolve.math.Vector)}
	 * .
	 */
	@Test
	public void testAdd() {
		assertThat(Vector.ZERO.add(Vector.ZERO), is(Vector.ZERO));
		assertThat(new Vector(18, 18).add(Vector.ZERO), is(new Vector(18, 18)));
		assertThat(new Vector(7, 1).add(new Vector(-2, 15)), is(new Vector(5,
				16)));
	}

	/**
	 * Test method for {@link com.googlecode.jumpnevolve.math.Vector#div(float)}
	 * .
	 */
	@Test
	public void testDiv() {
		assertThat(Vector.RIGHT.div(1), is(Vector.RIGHT));
		assertThat(Vector.ZERO.div(300), is(Vector.ZERO));
		assertThat(new Vector(18, -29).div(2), is(new Vector(9, -14.5f)));
		assertThat(Float.isInfinite(new Vector(20, -27).div(0).abs()), is(true));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#getDirection()}.
	 */
	@Test
	public void testGetDirection() {
		assertThat(Vector.RIGHT.getDirection(), is(Vector.RIGHT));
		assertThat(new Vector(10, 0).getDirection(), is(Vector.RIGHT));
		assertThat(new Vector(-99, 0).getDirection(), is(Vector.RIGHT.mul(-1)));
		assertThat(new Vector(10, 10).getDirection(), is(new Vector(1, 1)
				.getDirection()));
		assertThat(Float.isNaN(Vector.ZERO.getDirection().x), is(true));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#getDistance(com.googlecode.jumpnevolve.math.Vector)}
	 * .
	 */
	@Test
	public void testGetDistance() {
		assertThat(Vector.ZERO.getDistance(Vector.ZERO), is(0.0f));
		assertThat(Vector.ZERO.getDistance(new Vector(44, 20)), is(new Vector(
				44, 20).abs()));
		assertThat(new Vector(-27, 29).getDistance(new Vector(200, 107)),
				is(new Vector(-27, 29).sub(new Vector(200, 107)).abs()));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#getNormal()}
	 */
	@Test
	public void testGetNormal() {
		assertThat(Vector.DOWN.getNormal(), either(is(Vector.LEFT)).or(
				is(Vector.RIGHT)));
		assertThat(Vector.LEFT.getNormal(), either(is(Vector.UP)).or(
				is(Vector.DOWN)));
		assertThat(new Vector(7, 5).getNormal(), either(is(new Vector(-5, 7)))
				.or(is(new Vector(5, -7))));
	}

	/**
	 * Test method for {@link com.googlecode.jumpnevolve.math.Vector#isZero()}.
	 */
	@Test
	public void testIsZero() {
		assertThat(Vector.ZERO.isZero(), is(true));
		assertThat(Vector.RIGHT.isZero(), is(false));
		assertThat(new Vector(0, 0).isZero(), is(true));
		assertThat(new Vector(0, 16).isZero(), is(false));
		assertThat(new Vector(-2, 2).isZero(), is(false));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#mulAsLists(Vector)}
	 */
	@Test
	public void testMulAsLists() {
		assertThat(Vector.ZERO.mulAsLists(new Vector(939.0f, -193.0f)),
				is(Vector.ZERO));
		assertThat(Vector.LEFT.mulAsLists(Vector.UP), is(Vector.ZERO));
		assertThat(new Vector(3, 5).mulAsLists(new Vector(-2, 4)),
				is(new Vector(-6, 20)));
	}

	/**
	 * Test method for {@link com.googlecode.jumpnevolve.math.Vector#mul(float)}
	 * .
	 */
	@Test
	public void testMulFloat() {
		assertThat(Vector.ZERO.mul(18), is(Vector.ZERO));
		assertThat(Vector.RIGHT.mul(0), is(Vector.ZERO));
		assertThat(new Vector(14, 18).mul(1), is(new Vector(14, 18)));
		assertThat(new Vector(-3, 4).mul(2), is(new Vector(-6, 8)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#mul(com.googlecode.jumpnevolve.math.Vector)}
	 * .
	 */
	@Test
	public void testMulVector() {
		assertThat(new Vector(9, 1).mul(Vector.ZERO), is(0.0f));
		assertThat(new Vector(9, 1).mul(Vector.RIGHT), is(9.0f));
		assertThat(new Vector(2, 4).mul(new Vector(50, 12)),
				is(2.0f * 50.0f + 4.0f * 12.0f));
		assertThat(new Vector(17, 18).mul(new Vector(17, 18)), is(new Vector(
				17, 18).squareAbs()));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#squareAbs()}.
	 */
	@Test
	public void testSquareAbs() {
		assertThat(Vector.ZERO.squareAbs(), is(0.0f));
		assertThat(Vector.RIGHT.squareAbs(), is(1.0f));
		assertThat(new Vector(4, 3).squareAbs(), is(25.0f));
		assertThat(new Vector(29, 21).squareAbs(), is(new Vector(21, 29).abs()
				* new Vector(29, 21).abs()));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Vector#sub(com.googlecode.jumpnevolve.math.Vector)}
	 * .
	 */
	@Test
	public void testSub() {
		assertThat(new Vector(18, 19).sub(Vector.ZERO), is(new Vector(18, 19)));
		assertThat(new Vector(77, -11).sub(new Vector(1, 2)), is(new Vector(76,
				-13)));
	}
}
