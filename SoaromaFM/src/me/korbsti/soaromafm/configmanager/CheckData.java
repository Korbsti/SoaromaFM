package me.korbsti.soaromafm.configmanager;

import me.korbsti.soaromafm.SoaromaFM;

public class CheckData {
	SoaromaFM plugin;

	public CheckData(SoaromaFM instance) {
		plugin = instance;
	}

	public void dataCheckSetter(String playerName) {
		if (!plugin.playerDataManager.getPlayedData(playerName)) {
			plugin.playerDataManager.setDefaultPlayerData(playerName);
		}
		if (plugin.playerDataManager.getMarriedTo(playerName) != "null") {
			plugin.marriedTo.put(playerName, plugin.playerDataManager.getMarriedTo(playerName));
		} else {
			plugin.marriedTo.put(playerName, "null");
		}
		if (plugin.playerDataManager.getMarriedToUUID(playerName) != "null") {
			plugin.marriedTo.put(playerName, plugin.playerDataManager.getMarriedTo(playerName));
		} else {
			plugin.marriedTo.put(playerName, "null");
		}
		if (plugin.playerDataManager.getFamilyName(playerName) != "null") {
			plugin.familyName.put(playerName, plugin.playerDataManager.getFamilyName(playerName));
		} else {
			plugin.familyName.put(playerName, "null");
		}
		if (plugin.playerDataManager.getUserGender(playerName) != "null") {
			plugin.gender.put(playerName, plugin.playerDataManager.getUserGender(playerName));
		} else {
			plugin.gender.put(playerName, "null");
		}
		if (plugin.playerDataManager.getFamilyRole(playerName) != "null") {
			plugin.role.put(playerName, plugin.playerDataManager.getFamilyRole(playerName));
		} else {
			plugin.role.put(playerName, "null");
		}
	}
}
