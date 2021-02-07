package me.korbsti.soaromafm.events;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import me.korbsti.soaromafm.SoaromaFM;

public class Login implements Listener {
	static final String mainVariable = "Korbsti";
	SoaromaFM plugin;

	public Login(SoaromaFM instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		plugin.checkData.dataCheckSetter(uuid);
		plugin.currentChannel.put(uuid, "global");
		plugin.display.setDisplayName(e.getPlayer());
		plugin.playerDataManager.setPlayerName(uuid, e.getPlayer().getName());
		plugin.userNumSetting.put(uuid, 0);
		try {
			plugin.yamlConfigData.save(plugin.configFileData);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
