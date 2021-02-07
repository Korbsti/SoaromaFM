package me.korbsti.soaromafm.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import me.korbsti.soaromafm.SoaromaFM;

public class ServerLoad implements Listener {
	SoaromaFM plugin;

	public ServerLoad(SoaromaFM instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			String uuid = p.getUniqueId().toString();
			plugin.familyName.put(uuid, plugin.playerDataManager.getFamilyName(uuid));
			plugin.gender.put(uuid, plugin.playerDataManager.getUserGender(uuid));
			plugin.marriedTo.put(uuid, plugin.playerDataManager.getMarriedTo(uuid));
			plugin.role.put(uuid, plugin.playerDataManager.getFamilyRole(uuid));
			plugin.userNumSetting.put(uuid, 0);

			if(plugin.currentChannel.get(uuid) == null) {
				plugin.currentChannel.put(uuid, "global");
			}
			plugin.display.setDisplayName(p);
		}
	}
}
