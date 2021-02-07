package me.korbsti.soaromafm.broadcasting;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.korbsti.soaromafm.SoaromaFM;

public class Broadcasting {
	SoaromaFM plugin;

	public Broadcasting(SoaromaFM instance) {
		this.plugin = instance;
	}

	public void marriageBroadcast(String uuid, String playerName) {
		if (plugin.yamlConfig.getBoolean("enableMarriageBroadcast")) {
			String message = plugin.configMessage.getConfigMessage("marriageBroadcastMessage")
					.replace("{first-user}", playerName)
					.replace("{second-user}", plugin.playerDataManager.getMarriedTo(uuid))
					.replace("{family-name}", plugin.playerDataManager.getFamilyName(uuid));
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(message);
			}
		}
	}

	public void marriageDivorce(String playerName, String divorced) {
		if (plugin.yamlConfig.getBoolean("enableDivorceBroadcast")) {
			String message = plugin.configMessage.getConfigMessage("marriageDivorceBroadcast").replace("{first-user}", playerName).replace("{second-user}", divorced);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(message);
			}
		}
	}

	public void kidAcceptance(String playerName, String uuid) {
		if (plugin.yamlConfig.getBoolean("enableChildAcceptanceBroadcast")) {
			String message = plugin.configMessage.getConfigMessage("childAcceptanceBroadcastMessage").replace("{parent}", plugin.playerDataManager.getParentOneName(uuid))
					.replace("{child}", plugin.playerDataManager.getPlayerName(uuid));
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(message);
			}
		}
	}

	public void kidLeave(String playerName) {
		if (plugin.yamlConfig.getBoolean("enableChildLeaveBroadcast")) {
			String message = plugin.configMessage.getConfigMessage("childRunawayBroadcastMessage").replace("{user}", playerName);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(message);
			}
		}
	}
	
   public void kidDisown(String playerName, String child) {
		if (plugin.yamlConfig.getBoolean("enableChildDisownBroadcast")) {
			String message = plugin.configMessage.getConfigMessage("childDisownBroadcast").replace("{user}", playerName).replace("{child}", child);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(message);
			}
		}
   }
}
