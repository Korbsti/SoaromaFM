package me.korbsti.soaromafm.configmanager;

import org.bukkit.Bukkit;

import me.korbsti.soaromafm.SoaromaFM;
import net.md_5.bungee.api.ChatColor;

public class ConfigMessages {
	SoaromaFM plugin;

	public ConfigMessages(SoaromaFM instance) {
		this.plugin = instance;
	}

	public String getConfigMessage(String config) {
		return plugin.hex.translateHexColorCodes("#", "/", plugin.yamlConfig.getString(config));
	}
}
