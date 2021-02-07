package me.korbsti.soaromafm.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.korbsti.soaromafm.SoaromaFM;
import org.bukkit.entity.Player;

public class CommandPre implements Listener {
	SoaromaFM plugin;
	
	public CommandPre(SoaromaFM instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void commandPre(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().equalsIgnoreCase("/nick N/A") || e.getMessage().equalsIgnoreCase("/nickname N/A")) {
			e.setCancelled(true);
			Player p1 = e.getPlayer();
			p1.sendMessage(
					"[SoaromaFM] I am sorry but that nickname will break SoaromaFM, please use a different nickname ;-;");
			
		}
	}
}
