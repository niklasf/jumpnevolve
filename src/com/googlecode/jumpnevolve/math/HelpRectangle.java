package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
class HelpRectangle {

	/**
	 * Die X-Koordinate der oberen linken Ecke
	 */
	public final float x;

	/**
	 * Die Y-Koordinate der oberen linken Ecke
	 */
	public final float y;

	/**
	 * Die Breite
	 */
	public final float width;

	/**
	 * Die Höhe
	 */
	public final float height;

	/**
	 * 
	 */
	public HelpRectangle(float left, float right, float up, float down) {
		this.x = left;
		this.y = up;
		this.width = right - left;
		this.height = down - up;
	}

	public boolean doesCollide(HelpRectangle other) {
		HelpRectangle bounding = this.getBoundingRectangle(other);
		return bounding.width <= this.width + other.width
				&& bounding.height <= this.height + other.height;
	}

	/**
	 * @param other
	 *            Ein zweites Rechteck
	 * @return Das kleinste Rechteck das beide Rechtecke vollständig enthält.
	 */
	public HelpRectangle getBoundingRectangle(HelpRectangle other) {
		// Obere linke Ecke
		float left = Math.min(this.x, other.x);
		float top = Math.min(this.y, other.y);

		// Untere Rechte Ecke
		float right = Math.max(this.x + this.width, other.x + other.width);
		float bottom = Math.max(this.y + this.height, other.y + other.height);

		// Rechteck erzeugen
		return new HelpRectangle(left, top, right - left, bottom - top);
	}
}
