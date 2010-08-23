/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.googlecode.jumpnevolve.tests.graphics;

import com.jme.app.SimplePassGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Torus;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.effects.water.ProjectedGrid;
import com.jmex.effects.water.WaterHeightGenerator;
import com.jmex.effects.water.WaterRenderPass;

/**
 * <code>TestProjectedWater</code> Test for the water effect pass.
 * 
 * @author Rikard Herlitz (MrCoder)
 */
public class TestProjectedWater extends SimplePassGame {
	public static void main(String[] args) {
		TestProjectedWater app = new TestProjectedWater();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	// debug stuff
	private Node debugQuadsNode;
	private float farPlane = 10000.0f;
	private ProjectedGrid projectedGrid;

	private Skybox skybox;

	private WaterRenderPass waterEffectRenderPass;

	private void buildSkyBox() {
		this.skybox = new Skybox("skybox", 10, 10, 10);

		String dir = "skybox/";
		Texture north = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "1.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture south = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "3.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture east = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "2.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture west = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "4.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture up = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "6.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		Texture down = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource(dir + "5.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);

		this.skybox.setTexture(Skybox.Face.North, north);
		this.skybox.setTexture(Skybox.Face.West, west);
		this.skybox.setTexture(Skybox.Face.South, south);
		this.skybox.setTexture(Skybox.Face.East, east);
		this.skybox.setTexture(Skybox.Face.Up, up);
		this.skybox.setTexture(Skybox.Face.Down, down);
		this.skybox.preloadTextures();

		CullState cullState = this.display.getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.None);
		cullState.setEnabled(true);
		this.skybox.setRenderState(cullState);

		ZBufferState zState = this.display.getRenderer().createZBufferState();
		zState.setEnabled(false);
		this.skybox.setRenderState(zState);

		FogState fs = this.display.getRenderer().createFogState();
		fs.setEnabled(false);
		this.skybox.setRenderState(fs);

		this.skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
		this.skybox.setCullHint(Spatial.CullHint.Never);
		this.skybox.setTextureCombineMode(TextureCombineMode.Replace);
		this.skybox.updateRenderState();

		this.skybox.lockBounds();
		this.skybox.lockMeshes();
	}

	private void createDebugQuads() {
		this.debugQuadsNode = new Node("quadNode");
		this.debugQuadsNode.setCullHint(Spatial.CullHint.Never);

		float quadWidth = this.display.getWidth() / 8;
		float quadHeight = this.display.getWidth() / 8;
		Quad debugQuad = new Quad("reflectionQuad", quadWidth, quadHeight);
		debugQuad.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		debugQuad.setCullHint(Spatial.CullHint.Never);
		debugQuad.setLightCombineMode(Spatial.LightCombineMode.Off);
		TextureState ts = this.display.getRenderer().createTextureState();
		ts.setTexture(this.waterEffectRenderPass.getTextureReflect());
		debugQuad.setRenderState(ts);
		debugQuad.updateRenderState();
		debugQuad.getLocalTranslation().set(quadWidth * 0.6f,
				quadHeight * 1.0f, 1.0f);
		this.debugQuadsNode.attachChild(debugQuad);

		if (this.waterEffectRenderPass.getTextureRefract() != null) {
			debugQuad = new Quad("refractionQuad", quadWidth, quadHeight);
			debugQuad.setRenderQueueMode(Renderer.QUEUE_ORTHO);
			debugQuad.setCullHint(Spatial.CullHint.Never);
			debugQuad.setLightCombineMode(Spatial.LightCombineMode.Off);
			ts = this.display.getRenderer().createTextureState();
			ts.setTexture(this.waterEffectRenderPass.getTextureRefract());
			debugQuad.setRenderState(ts);
			debugQuad.updateRenderState();
			debugQuad.getLocalTranslation().set(quadWidth * 0.6f,
					quadHeight * 2.1f, 1.0f);
			this.debugQuadsNode.attachChild(debugQuad);
		}

		if (this.waterEffectRenderPass.getTextureDepth() != null) {
			debugQuad = new Quad("refractionQuad", quadWidth, quadHeight);
			debugQuad.setRenderQueueMode(Renderer.QUEUE_ORTHO);
			debugQuad.setCullHint(Spatial.CullHint.Never);
			debugQuad.setLightCombineMode(Spatial.LightCombineMode.Off);
			ts = this.display.getRenderer().createTextureState();
			ts.setTexture(this.waterEffectRenderPass.getTextureDepth());
			debugQuad.setRenderState(ts);
			debugQuad.updateRenderState();
			debugQuad.getLocalTranslation().set(quadWidth * 0.6f,
					quadHeight * 3.2f, 1.0f);
			this.debugQuadsNode.attachChild(debugQuad);
		}
	}

	private Node createObjects() {
		Node objects = new Node("objects");

		Torus torus = new Torus("Torus", 50, 50, 10, 20);
		torus.setLocalTranslation(new Vector3f(50, -5, 20));
		TextureState ts = this.display.getRenderer().createTextureState();
		Texture t0 = TextureManager.loadTexture(
				TestProjectedWater.class.getClassLoader().getResource(
						"jmetest/data/images/Monkey.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		Texture t1 = TextureManager.loadTexture(
				TestProjectedWater.class.getClassLoader().getResource(
						"jmetest/data/texture/north.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t1.setEnvironmentalMapMode(Texture.EnvironmentalMapMode.SphereMap);
		ts.setTexture(t0, 0);
		ts.setTexture(t1, 1);
		ts.setEnabled(true);
		torus.setRenderState(ts);
		objects.attachChild(torus);

		ts = this.display.getRenderer().createTextureState();
		t0 = TextureManager.loadTexture(TestProjectedWater.class
				.getClassLoader().getResource("jmetest/data/texture/wall.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t0.setWrap(Texture.WrapMode.Repeat);
		ts.setTexture(t0);

		Box box = new Box("box1", new Vector3f(-10, -10, -10), new Vector3f(10,
				10, 10));
		box.setLocalTranslation(new Vector3f(0, -7, 0));
		box.setRenderState(ts);
		objects.attachChild(box);

		box = new Box("box2", new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5));
		box.setLocalTranslation(new Vector3f(15, 10, 0));
		box.setRenderState(ts);
		objects.attachChild(box);

		box = new Box("box3", new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5));
		box.setLocalTranslation(new Vector3f(0, -10, 15));
		box.setRenderState(ts);
		objects.attachChild(box);

		box = new Box("box4", new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5));
		box.setLocalTranslation(new Vector3f(20, 0, 0));
		box.setRenderState(ts);
		objects.attachChild(box);

		ts = this.display.getRenderer().createTextureState();
		t0 = TextureManager.loadTexture(
				TestProjectedWater.class.getClassLoader().getResource(
						"jmetest/data/images/Monkey.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		t0.setWrap(Texture.WrapMode.Repeat);
		ts.setTexture(t0);

		box = new Box("box5", new Vector3f(-50, -2, -50), new Vector3f(50, 2,
				50));
		box.setLocalTranslation(new Vector3f(0, -15, 0));
		box.setRenderState(ts);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		objects.attachChild(box);

		return objects;
	}

	private void setupFog() {
		FogState fogState = this.display.getRenderer().createFogState();
		fogState.setDensity(1.0f);
		fogState.setEnabled(true);
		fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		fogState.setEnd(this.farPlane);
		fogState.setStart(this.farPlane / 10.0f);
		fogState.setDensityFunction(FogState.DensityFunction.Linear);
		fogState.setQuality(FogState.Quality.PerVertex);
		this.rootNode.setRenderState(fogState);
	}

	private void setupKeyBindings() {
		KeyBindingManager.getKeyBindingManager().set("f", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("e", KeyInput.KEY_E);
		KeyBindingManager.getKeyBindingManager().set("g", KeyInput.KEY_G);

		Text t = Text.createDefaultTextLabel("Text",
				"F: switch freeze/unfreeze projected grid");
		t.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		t.setLightCombineMode(Spatial.LightCombineMode.Off);
		t.setLocalTranslation(new Vector3f(0, 20, 1));
		this.statNode.attachChild(t);

		t = Text.createDefaultTextLabel("Text",
				"E: debug show/hide reflection and refraction textures");
		t.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		t.setLightCombineMode(Spatial.LightCombineMode.Off);
		t.setLocalTranslation(new Vector3f(0, 40, 1));
		this.statNode.attachChild(t);
	}

	private void switchShowDebug() {
		if (this.debugQuadsNode.getCullHint() == Spatial.CullHint.Never) {
			this.debugQuadsNode.setCullHint(Spatial.CullHint.Always);
		} else {
			this.debugQuadsNode.setCullHint(Spatial.CullHint.Never);
		}
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		this.waterEffectRenderPass.cleanup();
	}

	@Override
	protected void simpleInitGame() {
		this.display.setTitle("Water Test");
		this.cam.setFrustumPerspective(50.0f, (float) this.display.getWidth()
				/ (float) this.display.getHeight(), 1f, this.farPlane);
		this.cam.setLocation(new Vector3f(100, 50, 100));
		this.cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
		this.cam.update();

		setupKeyBindings();

		setupFog();

		Node reflectedNode = new Node("reflectNode");

		buildSkyBox();
		reflectedNode.attachChild(this.skybox);
		reflectedNode.attachChild(createObjects());

		this.rootNode.attachChild(reflectedNode);

		this.waterEffectRenderPass = new WaterRenderPass(this.cam, 4, true,
				true);
		this.waterEffectRenderPass.setClipBias(0.5f);
		this.waterEffectRenderPass.setWaterMaxAmplitude(5.0f);
		// setting to default value just to show
		this.waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f,
				1.0f, 0.0f), 0.0f));

		this.projectedGrid = new ProjectedGrid("ProjectedGrid", this.cam, 100,
				70, 0.01f, new WaterHeightGenerator());
		// or implement your own waves like this(or in a separate class)...
		// projectedGrid = new ProjectedGrid( "ProjectedGrid", cam, 50, 50,
		// 0.01f, new HeightGenerator() {
		// public float getHeight( float x, float z, float time ) {
		// return
		// FastMath.sin(x*0.05f+time*2.0f)+FastMath.cos(z*0.1f+time*4.0f)*2;
		// }
		// } );

		this.waterEffectRenderPass.setWaterEffectOnSpatial(this.projectedGrid);
		this.rootNode.attachChild(this.projectedGrid);

		createDebugQuads();
		this.rootNode.attachChild(this.debugQuadsNode);

		this.waterEffectRenderPass.setReflectedScene(reflectedNode);
		this.waterEffectRenderPass.setSkybox(this.skybox);
		this.pManager.add(this.waterEffectRenderPass);

		RenderPass rootPass = new RenderPass();
		rootPass.add(this.rootNode);
		this.pManager.add(rootPass);

		RenderPass statPass = new RenderPass();
		statPass.add(this.statNode);
		this.pManager.add(statPass);

		this.rootNode.setCullHint(Spatial.CullHint.Never);
		this.rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}

	@Override
	protected void simpleUpdate() {
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("f", false)) {
			this.projectedGrid.switchFreeze();
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("e", false)) {
			switchShowDebug();
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("g", false)) {
			this.waterEffectRenderPass
					.setUseRefraction(!this.waterEffectRenderPass
							.isUseRefraction());
			this.waterEffectRenderPass.reloadShader();
		}

		this.skybox.getLocalTranslation().set(this.cam.getLocation());
		this.cam.update();
	}
}