package me.korbsti.soaromafm.configmanager;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import me.korbsti.soaromafm.SoaromaFM;

public class PlayerDataManager {
	SoaromaFM plugin;

	public PlayerDataManager(SoaromaFM instance) {
		this.plugin = instance;
	}

	public void setDefaultPlayerData(String uuid) {
		plugin.yamlConfigData.set(uuid + ".user.hasPlayed", true);
		plugin.yamlConfigData.set(uuid + ".user.familyName", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.playerName", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.gender", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.marriedTo", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.marriedToUUID", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.role", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.soaromaDisplay", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.family.parentOneName", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.family.parentOneUUID", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.family.parentTwoName", "N/A");
		plugin.yamlConfigData.set(uuid + ".user.family.parentTwoUUID", "N/A");

		try {
			plugin.yamlConfigData.save(plugin.configFileData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSoaromaDisplay(String uuid, String display) {
		plugin.yamlConfigData.set(uuid + ".user.soaromaDisplay", display);
	}

	public String getSoaromaDisplay(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.soaromaDisplay");
	}

	public void setPlayerName(String uuid, String playerName) {
		plugin.yamlConfigData.set(uuid + ".user.playerName", playerName);
	}

	public void setParentOneName(String uuid, String parentOneName) {
		plugin.yamlConfigData.set(uuid + ".user.family.parentOneName", parentOneName);
	}

	public void setParentOneUUID(String uuid, String parentOneUUID) {
		plugin.yamlConfigData.set(uuid + ".user.family.parentOneUUID", parentOneUUID);
	}

	public void setParentTwoName(String uuid, String parentTwoName) {
		plugin.yamlConfigData.set(uuid + ".user.family.parentTwoName", parentTwoName);
	}

	public void setParentTwoUUID(String uuid, String parentTwoUUID) {
		plugin.yamlConfigData.set(uuid + ".user.family.parentTwoUUID", parentTwoUUID);
	}

	public String getParentOneName(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.family.parentOneName");
	}

	public String getParentOneUUID(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.family.parentOneUUID");
	}

	public String getParentTwoName(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.family.parentTwoName");
	}

	public String getParentTwoUUID(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.family.parentTwoUUID");
	}

	public void setMarriedTo(String uuid, String marriedTo) {
		plugin.yamlConfigData.set(uuid + ".user.marriedTo", marriedTo);
		plugin.marriedTo.put(uuid, marriedTo);
	}

	public void setMarriedToUUID(String uuid, String marriedTo) {
		plugin.yamlConfigData.set(uuid + ".user.marriedToUUID", marriedTo);
		plugin.marriedToUUID.put(uuid, marriedTo);
	}

	public void setFamilyNameTo(String uuid, String familyName) {
		plugin.yamlConfigData.set(uuid + ".user.familyName", familyName);
		plugin.marriedTo.put(uuid, familyName);
	}

	public void setGenderTo(String uuid, String gender) {
		plugin.yamlConfigData.set(uuid + ".user.gender", gender);
		plugin.gender.put(uuid, gender);
	}

	public void setFamilyRole(String uuid, String role) {
		plugin.yamlConfigData.set(uuid + ".user.role", role);
		plugin.role.put(uuid, role);
	}

	public String getPlayerName(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.playerName");
	}

	public Boolean getPlayedData(String uuid) {
		return plugin.yamlConfigData.getBoolean(uuid + ".user.hasPlayed");
	}

	public String getMarriedToUUID(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.marriedToUUID");
	}

	public String getMarriedTo(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.marriedTo");
	}

	public String getUserGender(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.gender");
	}

	public String getFamilyName(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.familyName");
	}

	public String getFamilyRole(String uuid) {
		return plugin.yamlConfigData.getString(uuid + ".user.role");
	}
}
