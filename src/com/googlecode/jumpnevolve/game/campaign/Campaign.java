package com.googlecode.jumpnevolve.game.campaign;

import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipFile;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;

public class Campaign {

	public final CampaignLoader loader;

	private CampaignMap map;
	private HashMap<String, String> levels = new HashMap<String, String>();

	public Campaign(CampaignLoader loader) {
		this.loader = loader;
	}

	public ZipFile getZipFile() throws IOException {
		return new ZipFile(this.loader.source);
	}

	public CampaignMap getMap() {
		return map;
	}

	public void setMap(CampaignMap map) {
		this.map = map;
		this.map.setParentCampaign(this);
	}

	public void addLevel(String name, String path) {
		this.levels.put(name, path);
	}

	public void start(String name) {
		String path = this.levels.get(name);
		Level level = Levelloader.asyncLoadLevel(this.loader.source + "!"
				+ path);
		Engine.getInstance().switchState(level);
	}
}
