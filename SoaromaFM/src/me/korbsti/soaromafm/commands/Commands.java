package me.korbsti.soaromafm.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.korbsti.soaromafm.SoaromaFM;

public class Commands implements CommandExecutor {
	SoaromaFM plugin;

	public Commands(SoaromaFM instance) {
		this.plugin = instance;
	}

	static final String mainVariable = "Korbsti";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ("fm".equalsIgnoreCase(label) && sender instanceof Player) {
			String playerName = sender.getName();
			Player player = (Player) sender;
			String uuid = player.getUniqueId().toString();
			if (args.length == 0) {
				sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
				return true;
			}
			plugin.checkData.dataCheckSetter(uuid);
			if ("divorce".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.marry.divorce")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if ("N/A".equals(plugin.marriedTo.get(uuid))) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsNotMarried"));
					return true;
				}
				sender.sendMessage(plugin.configMessage.getConfigMessage("marriageDivorce").replace("{user}",
						plugin.marriedTo.get(uuid)));
				plugin.broadcast.marriageDivorce(playerName, plugin.playerDataManager.getMarriedTo(uuid));
				String targetUUID = plugin.playerDataManager.getMarriedToUUID(uuid);
				plugin.playerDataManager.setMarriedTo(targetUUID, "N/A");
				plugin.playerDataManager.setMarriedTo(uuid, "N/A");
				plugin.playerDataManager.setMarriedToUUID(uuid, "N/A");
				plugin.playerDataManager.setMarriedToUUID(targetUUID, "N/A");
				try {
					plugin.yamlConfigData.save(plugin.configFileData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				plugin.display.setDisplayName(player);
				return true;
			}
			if ("marry".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.marry.propose")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if (args.length == 0) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
					return true;
				}
				if (args.length != 3) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsMarriage"));
					return true;
				}
				///// DECLINE MARRIAGE /////
				if ("decline".equalsIgnoreCase(args[1])) {
					if (!player.hasPermission("fm.marry.decline")) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
						return true;
					}
					Player targetSender = Bukkit.getServer().getPlayer(args[2]);
					if (targetSender == null) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
						return true;
					}
					String targetUUID = targetSender.getUniqueId().toString();
					if (plugin.pendingMarriageAccept.get(targetUUID).equals(playerName)) {
						targetSender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalDecline")
								.replace("{name}", player.getName()));
						sender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalDeclineDecliner")
								.replace("{name}", targetSender.getName()));
						plugin.pendingMarriageAccept.put(targetUUID, "N/A");
					} else {
						sender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalDeclineNotProposed"));
					}
					return true;
				}
				///// ACCEPT MARRIAGE /////
				if ("accept".equalsIgnoreCase(args[1])) {
					if ("marry".equalsIgnoreCase(args[0])) {
						if (!player.hasPermission("fm.marry.accept")) {
							sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
							return true;
						}
						Player targetSender = Bukkit.getServer().getPlayer(args[2]);
						if (targetSender == null) {
							sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
							return true;
						}
						if (targetSender == player) {
							sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPropsalSameUser"));
							return true;
						}

						if (!"N/A".equals(plugin.familyName.get(uuid))) {
							player.sendMessage(plugin.configMessage.getConfigMessage("leaveFamilyFirst"));
						}

						plugin.pendingMarriageAccept.put(uuid, args[2]);
						String targetSenderName = targetSender.getName();
						String targetUUID = targetSender.getUniqueId().toString();
						if (plugin.pendingMarriageAccept.get(targetUUID) == null) {
							plugin.pendingMarriageAccept.put(targetUUID, "N/A");
						}
						if (plugin.pendingMarriageAccept.get(targetUUID).equals(playerName)) {
							sender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalAccepter")
									.replace("{name}", targetSenderName));
							targetSender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalAccept")
									.replace("{name}", targetSenderName));
							plugin.yamlConfigData.addDefault("family-names", args[2]);
							plugin.playerDataManager.setMarriedTo(uuid, targetSenderName);
							plugin.playerDataManager.setMarriedTo(targetUUID, playerName);
							plugin.playerDataManager.setMarriedToUUID(uuid, targetUUID);
							plugin.playerDataManager.setFamilyRole(uuid, "parent");
							plugin.playerDataManager.setFamilyRole(targetUUID, "parent");
							plugin.playerDataManager.setMarriedToUUID(targetUUID, uuid);
							plugin.playerDataManager.setFamilyNameTo(uuid, plugin.familyName.get(targetUUID));
							plugin.playerDataManager.setFamilyNameTo(targetUUID, plugin.familyName.get(targetUUID));
							plugin.takenFamilyNames.add(plugin.familyName.get(targetUUID));
							plugin.yamlConfigData.set("family-names", plugin.takenFamilyNames);
							try {
								plugin.yamlConfigData.save(plugin.configFileData);
							} catch (IOException e) {
								e.printStackTrace();
							}
							plugin.display.setDisplayName(player);
							plugin.display.setDisplayName(targetSender);
							plugin.broadcast.marriageBroadcast(uuid, playerName);
							return true;
						}
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidMarriageAccept")
								.replace("{name}", targetSenderName));
					}
					return true;
				}
				///// MARRIAGE PROPOSAL /////
				Player targetSender = Bukkit.getServer().getPlayer(args[1]);

				if (targetSender == null) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
					return true;
				}
				String tUUID = targetSender.getUniqueId().toString();
				if (plugin.playerDataManager.getParentOneUUID(tUUID).equals(uuid)
						|| plugin.playerDataManager.getParentTwoUUID(tUUID).equals(uuid)
						|| plugin.playerDataManager.getParentOneUUID(uuid).equals(tUUID)
						|| plugin.playerDataManager.getParentTwoUUID(uuid).equals(tUUID)) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("sameFamily"));
					return true;
				}
				for (String str : plugin.takenFamilyNames) {
					if (str.equalsIgnoreCase(args[2])) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsFamilyNameTaken"));
						return true;
					}
				}
				if (playerName == targetSender.getName()) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPropsalSameUser"));
					return true;
				}
				if (plugin.marriedTo.get(targetSender.getName()) == null) {
					plugin.marriedTo.put(targetSender.getName(), "N/A");
				}
				if (!"N/A".equals(plugin.marriedTo.get(String.valueOf(targetSender.getUniqueId())))) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsAlreadyMarried"));
					return true;
				}
				sender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposal")
						.replace("{name}", targetSender.getName()).replace("{family-name}", args[2]));
				targetSender.sendMessage(plugin.configMessage.getConfigMessage("marriageProposalSend")
						.replace("{name}", sender.getName()).replace("{family-name}", args[2]));
				plugin.pendingMarriageAccept.put(uuid, args[1]);
				plugin.familyName.put(uuid, args[2]);
				return true;
			}
			///// GENDER LIST /////
			if ("genderlist".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.gender.list")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				ArrayList<String> genders = new ArrayList<String>();
				for (String str : plugin.genderName) {
					genders.add(str);
				}
				sender.sendMessage(
						plugin.configMessage.getConfigMessage("genderList").replace("{genders}", genders.toString()));
				return true;
			}

			/// REMOVE GENDER ///
			if ("removegender".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.gender.remove")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				plugin.playerDataManager.setGenderTo(player.getUniqueId().toString(), "N/A");
				try {
					plugin.yamlConfigData.save(plugin.configFileData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				plugin.display.setDisplayName(player);
				sender.sendMessage(plugin.configMessage.getConfigMessage("genderRemove"));
				return true;
			}

			///// CHANGE GENDER /////
			if ("gender".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.gender.choose")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if (args.length == 1) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
					return true;
				}
				for (String str : plugin.genderName) {
					if (str.equalsIgnoreCase(args[1])) {
						plugin.playerDataManager.setGenderTo(uuid, str);
						plugin.gender.put(uuid, str);
						try {
							plugin.yamlConfigData.save(plugin.configFileData);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage(
								plugin.configMessage.getConfigMessage("genderChangeMessage").replace("{gender}", str));
						plugin.display.setDisplayName(player);
						return true;
					}
				}
				sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGender"));
				return true;
			}
			///// HELP MENU /////
			if ("help".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.general.help")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				for (String str : plugin.helpMenu) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
				}
				return true;
			}
			///// FAMILY LEAVE /////
			if ("familyLeave".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.family.leave")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if ("N/A".equals(plugin.familyName.get(uuid))) {
					player.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsFamily"));
					return true;
				}
				int x = 0;
				int y = 0;
				if (plugin.takenFamilyNames.isEmpty()) {
					player.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsFamily"));
					return true;
				}
				for (String str : plugin.takenFamilyNames) {
					if (plugin.playerDataManager.getFamilyName(uuid).equals(str)) {
						y = x;
					} else {
						x++;
					}
				}
				String familyName = plugin.playerDataManager.getFamilyName(uuid);
				plugin.playerDataManager.setFamilyNameTo(uuid, "N/A");
				plugin.playerDataManager.setFamilyRole(uuid, "N/A");
				try {
					plugin.yamlConfigData.save(plugin.configFileData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage(plugin.configMessage.getConfigMessage("leaveFamily"));
				Boolean remove = true;
				for (String key : plugin.yamlConfigData.getKeys(false)) {
					if (!"family-names".equals(key) && plugin.playerDataManager.getFamilyName(key).equals(familyName)) {
						remove = false;
					}
				}
				if (remove) {
					plugin.takenFamilyNames.remove(y);
					plugin.yamlConfigData.set("family-names", plugin.takenFamilyNames);
					try {
						plugin.yamlConfigData.save(plugin.configFileData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				plugin.display.setDisplayName(player);
				return true;
			}
			///// FAMILY CHAT /////
			if ("chat".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.family.chat")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if ("global".equals(plugin.currentChannel.get(uuid))) {
					plugin.currentChannel.put(uuid, "family");
					player.sendMessage(plugin.configMessage.getConfigMessage("switchToFamilyChat"));
				} else {
					plugin.currentChannel.put(uuid, "global");
					player.sendMessage(plugin.configMessage.getConfigMessage("switchToGlobalChat"));
				}
				return true;
			}
			///// ADOPTION THING /////
			if ("adopt".equalsIgnoreCase(args[0])) {

				if (!player.hasPermission("fm.adopt.request")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if (args.length == 2) {

					Player targetSender = Bukkit.getServer().getPlayer(args[1]);
					if (targetSender == null) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
						return true;
					}
					String pName = player.getName();
					String tName = targetSender.getName();
					String playerUUID = player.getUniqueId().toString();
					String targetUUID = targetSender.getUniqueId().toString();
					if (pName.equals(tName)) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsAdoptSameUser"));
						return true;
					}
					if (playerUUID.equals(plugin.playerDataManager.getParentOneUUID(targetUUID))
							|| playerUUID.equals(plugin.playerDataManager.getParentTwoUUID(targetUUID))) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsAdoptSameFamily"));
						return true;
					}
					if (!"N/A".equals(plugin.playerDataManager.getParentOneUUID(targetUUID))
							&& !"N/A".equals(plugin.playerDataManager.getParentTwoUUID(targetUUID))) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsAlreadyHaveParents"));
						return true;
					}
					targetSender.sendMessage(
							plugin.configMessage.getConfigMessage("childProposalMessage").replace("{name}", pName));
					sender.sendMessage(plugin.configMessage.getConfigMessage("sentAdoptMessage"));
					plugin.pendingAdoptionAccept.put(pName, targetUUID);
					return true;
				} else {
					if (!player.hasPermission("fm.adopt.accept")) {
						sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
						return true;
					}
					if (args.length == 3) {
						if (args[1].equalsIgnoreCase("accept")) {
							Player targetSender = Bukkit.getServer().getPlayer(args[2]);
							if (targetSender == null) {
								sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
								return true;
							}
							plugin.pendingAdoptionAccept.put(player.getName(), targetSender.getUniqueId().toString());
							if (!player.getUniqueId().toString()
									.equals(plugin.pendingAdoptionAccept.get(targetSender.getName()))) {
								sender.sendMessage(plugin.configMessage.getConfigMessage("childDeclineMessage")
										.replace("{name}", targetSender.getName()));
								return true;
							} else {
								String targetUUID = targetSender.getUniqueId().toString();
								String playerUUID = player.getUniqueId().toString();
								if (!plugin.playerDataManager.getMarriedToUUID(targetUUID).equals("N/A")) {
									plugin.playerDataManager.setParentOneUUID(playerUUID, targetUUID);
									plugin.playerDataManager.setParentTwoUUID(playerUUID,
											plugin.playerDataManager.getMarriedToUUID(targetUUID));
									plugin.playerDataManager.setParentOneName(playerUUID, targetSender.getName());
									plugin.playerDataManager.setParentTwoName(playerUUID,
											plugin.playerDataManager.getMarriedTo(targetUUID));
								} else {
									plugin.playerDataManager.setParentOneUUID(playerUUID, targetUUID);
									plugin.playerDataManager.setParentOneName(playerUUID, targetSender.getName());
								}
								if (plugin.playerDataManager.getFamilyName(playerUUID).equals("N/A")) {
									if (!plugin.playerDataManager.getFamilyName(targetUUID).equals("N/A")) {
										plugin.playerDataManager.setFamilyNameTo(playerUUID,
												plugin.playerDataManager.getFamilyName(targetUUID));
									}
								}
								try {
									plugin.yamlConfigData.save(plugin.configFileData);
								} catch (IOException e) {
									e.printStackTrace();
								}
								targetSender.sendMessage(plugin.configMessage.getConfigMessage("childAcceptanceMessage")
										.replace("{name}", player.getName()));
								player.sendMessage(
										plugin.configMessage.getConfigMessage("childAcceptanceMessageToChild")
												.replace("{name}", targetSender.getName()));
								plugin.broadcast.kidAcceptance(playerName, playerUUID);
								plugin.display.setDisplayName(player);
								plugin.display.setDisplayName(targetSender);
								return true;

							}

						}
					}
				}
			}
			if ("runaway".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.adopt.runaway")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				String playerUUID = player.getUniqueId().toString();

				if (plugin.playerDataManager.getParentOneUUID(uuid).equals("N/A")
						&& plugin.playerDataManager.getParentTwoUUID(uuid).equals("N/A")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNoParents"));
					return true;
				}
				plugin.playerDataManager.setParentOneUUID(playerUUID, "N/A");
				plugin.playerDataManager.setParentTwoUUID(playerUUID, "N/A");
				plugin.playerDataManager.setParentOneName(playerUUID, "N/A");
				plugin.playerDataManager.setParentTwoName(playerUUID, "N/A");
				try {
					plugin.yamlConfigData.save(plugin.configFileData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.sendMessage(plugin.configMessage.getConfigMessage("leaveFamily"));
				plugin.broadcast.kidLeave(playerName);
				plugin.display.setDisplayName(player);
				return true;
			}
			if ("who".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.who.info")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if (args.length == 1) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
					return true;
				}
				Player targetSender = Bukkit.getServer().getPlayer(args[1]);
				if (targetSender == null) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsPlayerNotOnline"));
					return true;
				}
				String targetUUID = targetSender.getUniqueId().toString();
				ArrayList<String> siblings = new ArrayList<>();
				ArrayList<String> parents = new ArrayList<>();
				ArrayList<String> children = new ArrayList<>();
				if (!plugin.playerDataManager.getParentOneUUID(targetUUID).equals("N/A")) {
					parents.add(plugin.playerDataManager.getParentOneName(targetUUID));
				}
				if (!plugin.playerDataManager.getParentTwoUUID(targetUUID).equals("N/A")) {
					parents.add(plugin.playerDataManager.getParentTwoName(targetUUID));
				}
				for (String key : plugin.yamlConfigData.getKeys(false)) {
					if (!targetUUID.equals(key) && !key.equals("family-names")) {
						if (plugin.playerDataManager.getParentOneUUID(targetUUID)
								.equals(plugin.playerDataManager.getParentOneUUID(key))
								&& !plugin.playerDataManager.getParentOneUUID(targetUUID).equals("N/A")
								&& !plugin.playerDataManager.getParentOneUUID(key).equals("N/A")) {
							siblings.add(plugin.playerDataManager.getPlayerName(key));
						}
						if (plugin.playerDataManager.getParentTwoUUID(targetUUID)
								.equals(plugin.playerDataManager.getParentTwoUUID(key))
								&& !plugin.playerDataManager.getParentTwoUUID(targetUUID).equals("N/A")
								&& !plugin.playerDataManager.getParentTwoUUID(key).equals("N/A")) {
							siblings.add(plugin.playerDataManager.getPlayerName(key));
						}
						if (plugin.playerDataManager.getParentOneUUID(key).equals(targetUUID)
								&& !plugin.playerDataManager.getParentOneUUID(key).equals("N/A")) {
							children.add(plugin.playerDataManager.getPlayerName(key));
						}
						if (plugin.playerDataManager.getParentTwoUUID(key).equals(targetUUID)
								&& !plugin.playerDataManager.getParentTwoUUID(key).equals("N/A")) {
							children.add(plugin.playerDataManager.getPlayerName(key));
						}
					}
				}
				for (Object str : plugin.getConfig().getList("who-is-menu")) {
					String holder = String.valueOf(str)
							.replace("{married}", plugin.playerDataManager.getMarriedTo(targetUUID))
							.replace("{gender}", plugin.playerDataManager.getUserGender(targetUUID))
							.replace("{parents}", parents.toString()).replace("{siblings}", siblings.toString())
							.replace("{user}", targetSender.getName()).replace("{children}", children.toString());
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', holder));

				}
				return true;
			}
			if ("disown".equalsIgnoreCase(args[0])) {
				if (!player.hasPermission("fm.adopt.disown")) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("noPermission"));
					return true;
				}
				if (args.length == 1) {
					sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
					return true;
				}
				String playerUUID = player.getUniqueId().toString();
				String userName = args[1];
				for (String key : plugin.yamlConfigData.getKeys(false)) {
					if (!key.equals("family-names")) {
						if (plugin.playerDataManager.getPlayerName(key).equals(userName)) {
							if (plugin.playerDataManager.getParentOneUUID(key).equals(playerUUID)
									|| plugin.playerDataManager.getParentTwoUUID(playerUUID).equals(key)) {
								plugin.broadcast.kidDisown(player.getName(),
										plugin.playerDataManager.getPlayerName(key));
								plugin.playerDataManager.setParentOneUUID(key, "N/A");
								plugin.playerDataManager.setParentTwoUUID(key, "N/A");
								plugin.playerDataManager.setParentOneName(key, "N/A");
								plugin.playerDataManager.setParentTwoName(key, "N/A");
								try {
									plugin.yamlConfigData.save(plugin.configFileData);
								} catch (IOException e) {
									e.printStackTrace();
								}
								plugin.display.setDisplayName(player);
								player.sendMessage(plugin.configMessage.getConfigMessage("disownedChild"));
								return true;
							} else {
								player.sendMessage(plugin.configMessage.getConfigMessage("arentParent"));
								return true;
							}
						}
					}
				}
				return true;
			}
			sender.sendMessage(plugin.configMessage.getConfigMessage("invalidArgsGeneral"));
			return true;
		}
		return false;
	}
}