package me.houdert6.discordsrv;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class MaybeHasSRV implements DiscordSRVInterface {
    public String getLinkedDCID(Player player) {
        return null;
    }

    /**
     * Creates a discordsrv manager if discordsrv is enabled, otherwise makes a blank interface
     */
    public static DiscordSRVInterface makeSRVInterface(PluginManager pluginManager) {
        if (pluginManager.isPluginEnabled("DiscordSRV")) {
            try {
                return (DiscordSRVInterface) Class.forName("me.houdert6.discordsrv.DiscordSRVManager").getConstructors()[0].newInstance(pluginManager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new MaybeHasSRV();
        }
    }
}
