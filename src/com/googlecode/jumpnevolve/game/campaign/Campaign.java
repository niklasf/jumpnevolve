package com.googlecode.jumpnevolve.game.campaign;

import java.io.IOException;
import java.util.zip.ZipFile;

public class Campaign {

	public final CampaignLoader loader;

	private CampaignMap map;

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
	}
}
