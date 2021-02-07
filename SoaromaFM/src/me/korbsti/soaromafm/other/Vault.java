package me.korbsti.soaromafm.other;

import org.bukkit.plugin.RegisteredServiceProvider;

import me.korbsti.soaromafm.SoaromaFM;
import net.milkbowl.vault.chat.Chat;

public class Vault {
	
	SoaromaFM plugin;
	
    public static Chat chat = null;
    
	public Vault(SoaromaFM instance) {
		this.plugin = instance;
	}
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
}
