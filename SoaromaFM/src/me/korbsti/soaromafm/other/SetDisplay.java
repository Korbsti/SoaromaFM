package me.korbsti.soaromafm.other;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import me.korbsti.soaromafm.SoaromaFM;
import net.md_5.bungee.api.ChatColor;

public class SetDisplay {
	SoaromaFM plugin;

	public SetDisplay(SoaromaFM instance) {
		this.plugin = instance;
	}

	@SuppressWarnings("static-access")
	public void setDisplayName(Player p) {
		String displayName = plugin.configMessage.getConfigMessage("defaultChatFormat");
		String uuid = p.getUniqueId().toString();
		plugin.checkData.dataCheckSetter(uuid);
		
		if (!"N/A".equalsIgnoreCase(plugin.familyName.get(uuid))) {
			displayName = displayName.replace("{familyName}", plugin.familyName.get(uuid));
		} else {
			displayName = displayName.replace("{familyName} ", "");
			displayName = displayName.replace("{familyName}", "");
		}
		if (!"N/A".equalsIgnoreCase(plugin.gender.get(uuid))) {
			int x = 0;
			int y = 0;
			displayName = displayName.replace("{gender-name}", plugin.gender.get(uuid));
			for (String str : plugin.genderName) {
				if (str.equalsIgnoreCase(plugin.gender.get(uuid))) {
					y = x;
				}
				x++;
			}
			displayName = displayName.replace("{gender-prefix}",
					ChatColor.translateAlternateColorCodes('&', plugin.genderPrefix.get(y)));
		} else {
			displayName = displayName.replace("{gender-name} ", "");
			displayName = displayName.replace("{gender-prefix} ", "");
			displayName = displayName.replace("{gender-name}", "");
			displayName = displayName.replace("{gender-prefix}", "");
		}
		if (!"N/A".equalsIgnoreCase(plugin.marriedTo.get(uuid))) {
			displayName = displayName.replace("{marriage-heart}",
					plugin.configMessage.getConfigMessage("heart-prefix"));
			displayName = displayName.replace("{marriage-name}", plugin.marriedTo.get(uuid));
		} else {
			displayName = displayName.replace("{marriage-heart} ", "");
			displayName = displayName.replace("{marriage-name} ", "");
			displayName = displayName.replace("{marriage-heart}", "");
			displayName = displayName.replace("{marriage-name}", "");
		}
		String UUID = p.getUniqueId().toString();
		displayName = displayName.replace("{vault-prefix}", plugin.vault.chat.getPlayerPrefix(p));
		if (plugin.vault.chat.getPlayerPrefix(p).equals("") || plugin.vault.chat.getPlayerPrefix(p).equals(" ")
				|| plugin.vault.chat.getPlayerPrefix(p) == null) {
			displayName = displayName.replace("{vault-prefix} ", "");
			displayName = displayName.replace("{vault-prefix}", "");
		}
		String previous = plugin.playerDataManager.getSoaromaDisplay(UUID);
		plugin.playerDataManager.setSoaromaDisplay(UUID, displayName);
		try {
			plugin.yamlConfigData.save(plugin.configFileData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (plugin.yamlConfig.getBoolean("changeDisplayName")) {
			plugin.vault.chat.setPlayerPrefix(p,
					plugin.yamlConfig.getString("globalChatFormat")
							.replace("{vault-prefix}",
									plugin.vault.chat.getPlayerPrefix(p)
											.replace(previous, ""))
							.replace("{soaroma-prefix}", plugin.playerDataManager.getSoaromaDisplay(UUID)));
		}
	}
}
