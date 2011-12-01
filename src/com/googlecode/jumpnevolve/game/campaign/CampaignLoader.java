package com.googlecode.jumpnevolve.game.campaign;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
				entryList.put(toKeyEntry(zipEntry.getName()), zipEntry);
			}

			// Main-Datei verarbeiten
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					zip.getInputStream(entryList.get("main"))));
			String current = reader.readLine();
			while (current != null) {
				// TODO: Informationen verarbeiten
				current = reader.readLine();
			}

			reader.close();
			// Map erstellen
			reader = new BufferedReader(new InputStreamReader(
					zip.getInputStream(entryList.get("main"))));
			current = reader.readLine();
			String[] split = current.split("_");
			if (!(split.length == 4)) {
				throw new IOException(
						"Erste Map-Zeile hat nicht das korrekte Format: "
								+ current);
			}
			// Map mit der ersten Zeile erstellen
			CampaignMap map = new CampaignMap(Integer.parseInt(split[1]),
					Integer.parseInt(split[2]), transformSource(this.source)
							+ "!images/" + split[3]);
			current = reader.readLine();
			while (current != null) {
				split = current.split("_");
				if (split[0].equals("Level")) {

				}
				// TODO: Informationen verarbeiten
				current = reader.readLine();
			}
			this.campaign.setMap(map);

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

	private static String toKeyEntry(String name) {
		if (name.endsWith(".info")) {
			if (name.substring(name.lastIndexOf("/")).toLowerCase()
					.startsWith("map")) {
				return "map";
			} else if (name.substring(name.lastIndexOf("/")).toLowerCase()
					.startsWith("campaign")) {
				return "main";
			}
		}
		if (name.endsWith(".lvl")) {
			return "level_" + name.substring(name.lastIndexOf("/"));
		}
		return name;
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
