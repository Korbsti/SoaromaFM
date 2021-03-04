package me.korbsti.soaromafm.configmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.korbsti.soaromafm.SoaromaFM;

public class ConfigManagerr {
	static final String mainVariable = "Korbsti";
	SoaromaFM plugin;
	String dir = System.getProperty("user.dir");
	String directoryPathFile = dir + File.separator + "plugins" + File.separator + "SoaromaFM";
	public ConfigManagerr(SoaromaFM instance) {
		this.plugin = instance;
	}
	public void configCreator(String fileName, String fileData) {
		File file = new File(directoryPathFile);
		if (file.mkdirs()) {
			Bukkit.getLogger().info("Generated SoaromaFM configuration folder...");
		}
		plugin.configFile = new File(plugin.getDataFolder(), fileName);
		plugin.configFileData = new File(plugin.getDataFolder(), fileData);
		if (!plugin.configFile.exists()) {
			saveDefaultConfiguration(plugin.configFile);
		}
		if (!plugin.configFileData.exists()) {
			saveDefaultConfiguration(plugin.configFileData);
		}
		plugin.yamlConfig = YamlConfiguration.loadConfiguration(plugin.configFile);
		plugin.yamlConfigData = YamlConfiguration.loadConfiguration(plugin.configFileData);
	}
		public void saveDefaultConfiguration(File file) {
		try {
			InputStream input = plugin.getResource(file.getName());

			java.nio.file.Files.copy(input, file.toPath());
		} catch (IOException e) {
		}
	}
}