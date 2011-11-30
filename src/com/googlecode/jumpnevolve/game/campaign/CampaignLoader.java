package com.googlecode.jumpnevolve.game.campaign;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.util.Parameter;

public class CampaignLoader {

	public final String source;

	private Campaign campaign = null;

	public CampaignLoader(String source) {
		this.source = source;
	}

	public Campaign getCampaign() {
		if (this.campaign == null) {
			this.loadCampaign();
		}
		return this.campaign;
	}

	private void loadCampaign() {
		ZipFile zip = null;
		try {
			zip = new ZipFile(new File(transformSource(source)));
			Enumeration<? extends ZipEntry> entries = zip.entries();
			HashMap<String, ZipEntry> entryList = new HashMap<String, ZipEntry>();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) entries.nextElement();
				entryList.put(zipEntry.getName(), zipEntry);
			}
		} catch (IOException e) {
			Log.error("Kampagne konnte nicht geladen werden: " + source
					+ " Fehlermeldung: " + e);
			e.printStackTrace();
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					Log.error("Fehler beim Schließen des ZipFiles: " + e);
					e.printStackTrace();
				}
			}
		}
	}

	private static String transformSource(String source) {
		if (!source.startsWith(Parameter.PROGRAMM_DIRECTORY_CAMPAIGNS)) {
			source = Parameter.PROGRAMM_DIRECTORY_CAMPAIGNS + source;
		}
		if (!source.endsWith(".zip")) {
			source = source.substring(0, source.lastIndexOf('.')) + ".zip";
		}
		return source;
	}
}
