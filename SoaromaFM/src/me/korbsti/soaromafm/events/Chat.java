package me.korbsti.soaromafm.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.korbsti.soaromafm.SoaromaFM;
import net.md_5.bungee.api.ChatColor;

public class Chat implements Listener {
	static final String mainVariable = "Korbsti";
	SoaromaFM plugin;

	public Chat(SoaromaFM instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		if (!"global".equals(plugin.currentChannel.get(uuid))) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				String n = p.getUniqueId().toString();
				if (p.hasPermission("fm.use.familychat")
						&& plugin.familyName.get(n).equals(plugin.familyName.get(uuid))) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.yamlConfig.getString("familyChatFormat")
									.replace("{family}", plugin.yamlConfig.getString("family-chat-prefix"))
									.replace("{displayName}", p.getDisplayName())
									.replace("{message}", e.getMessage())));
				}
			}
			Bukkit.getLogger()
			.info(ChatColor.translateAlternateColorCodes('&',
					plugin.yamlConfig.getString("familyChatFormat")
					.replace("{family}", plugin.yamlConfig.getString("family-chat-prefix"))
					.replace("{displayName}", player.getDisplayName())
					.replace("{message}", e.getMessage())));
			e.setCancelled(true);
		}
		if (plugin.updateDisplayNameChat) {
			plugin.display.setDisplayName(player);
		}
	}
}
