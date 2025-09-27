package me.houdert6.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class DiscordSRVManager implements DiscordSRVInterface {
    private final DiscordSRV srv;

    public DiscordSRVManager(PluginManager pluginManager) {
        this.srv = (DiscordSRV) pluginManager.getPlugin("DiscordSRV");
    }

    public String getLinkedDCID(Player player) {
        return this.srv.getAccountLinkManager().getDiscordId(player.getUniqueId());
    }
}
