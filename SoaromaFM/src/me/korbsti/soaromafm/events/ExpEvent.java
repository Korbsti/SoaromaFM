package me.korbsti.soaromafm.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.korbsti.soaromafm.SoaromaFM;

public class ExpEvent implements Listener {
	SoaromaFM plugin;

	public ExpEvent(SoaromaFM instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void expEvent(PlayerExpChangeEvent e) {
		if (!plugin.yamlConfig.getBoolean("enableXPFamilyShare")) {
			return;
		}
		String userUUID = e.getPlayer().getUniqueId().toString();
		double x = e.getAmount();
		int amountGiven = (int) (x * plugin.expPercentage);
		if (amountGiven == 0) {
			amountGiven = 1;
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			String uuid = p.getUniqueId().toString();
			if (plugin.playerDataManager.getFamilyName(uuid).equals(userUUID) && !uuid.equals(userUUID)
					&& p.hasPermission("fm.gainfamily.xp")) {
				p.giveExp(amountGiven);
				if (plugin.yamlConfig.getBoolean("notifyFamilyXPGain")) {
					/*
					 * p.sendMessage(plugin.configMessage.getConfigMessage("XPGainMessage").replace(
					 * "{xp-amount}", String.valueOf(amountGiven)));
					 */
					BossBar bossBar = Bukkit
							.createBossBar(
									ChatColor.translateAlternateColorCodes('&',
											plugin.configMessage.getConfigMessage("XPGainMessage")
													.replace("{xp-amount}", String.valueOf(amountGiven))),
									BarColor.GREEN, BarStyle.SOLID);
					bossBar.addPlayer(p);
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {
							bossBar.removePlayer(p);

						}

					}, 15);
				}
			}
		}
	}
}
