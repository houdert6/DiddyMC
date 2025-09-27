package me.houdert6.spigot;

import com.google.common.collect.ImmutableList;
import me.houdert6.common.DiddyMCCommon;
import me.houdert6.common.util.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public final class DiddyMC extends JavaPlugin {
    private final DiddyMCCommon common = new DiddyMCCommon(this, new MessageSender());

    @Override
    public void onEnable() {
        this.common.onEnable();
        this.getCommand("rizzme").setExecutor(this);
        this.getCommand("oil").setExecutor(this);
        this.getCommand("diddle").setExecutor(this);
        this.getCommand("getaura").setExecutor(this);

        this.getLogger().log(Level.INFO, "DiddyMC Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equals("rizzme") && args.length == 0) {
            this.common.rizzMeCommand(sender);
            return true;
        }
        if (label.equals("oil") && args.length == 1) {
            List<Entity> entities = this.getServer().selectEntities(sender, args[0]);
            if (entities.size() > 0) {
                this.common.oilCommand(sender, entities.get(0));
                return true;
            }
        }
        if (label.equals("diddle") && args.length == 1) {
            List<Entity> entities = this.getServer().selectEntities(sender, args[0]);
            if (entities.size() > 0) {
                this.common.diddleCommand(sender, entities.get(0));
                return true;
            }
        }
        if (label.equals("getaura") && args.length == 1) {
            Player player = this.getServer().getPlayer(args[0]);
            if (player != null) {
                this.common.getAuraCommand(sender, player);
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (alias.equals("rizzme")) {
            return ImmutableList.of();
        }
        if (alias.equals("oil") || alias.equals("diddle") || alias.equals("getaura")) {
            if (args.length == 1) {
                return super.onTabComplete(sender, command, alias, args);
            }
            return ImmutableList.of();
        }
        return null;
    }
}
