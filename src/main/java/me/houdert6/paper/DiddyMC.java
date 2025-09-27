package me.houdert6.paper;

import com.google.common.collect.ImmutableList;
import me.houdert6.common.DiddyMCCommon;
import me.houdert6.paper.util.PaperMessageSender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class DiddyMC extends JavaPlugin {
    private final DiddyMCCommon common = new DiddyMCCommon(this, new PaperMessageSender());

    @Override
    public void onEnable() {
        this.common.onEnable();
        // Commands
        try {
            Class.forName("io.papermc.paper.command.brigadier.Commands");
        } catch (Exception e) {
            Command rizzme = new Command("rizzme", "Receive a random pickup line!", "Usage: /rizzme", new ArrayList<>()) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    if (args.length == 0) {
                        DiddyMC.this.common.rizzMeCommand(sender);
                    } else {
                        sender.sendMessage(Component.text(this.usageMessage));
                    }
                    return true;
                }
                @Override
                public @Nullable String getPermission() {
                    return "diddymc.rizzme";
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    return ImmutableList.of();
                }
            };
            this.getServer().getCommandMap().register(this.getName(), rizzme);
            Command oil = new Command("oil", "oil up your friends", "Usage: /oil <entity>", new ArrayList<>()) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    if (args.length == 1) {
                        List<Entity> entities = DiddyMC.this.getServer().selectEntities(sender, args[0]);
                        if (entities.size() > 0) {
                            DiddyMC.this.common.oilCommand(sender, entities.get(0));
                            return true;
                        }
                    }
                    sender.sendMessage(Component.text(this.usageMessage));
                    return true;
                }
                @Override
                public @Nullable String getPermission() {
                    return "diddymc.oil";
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    if (args.length == 1) {
                        return super.tabComplete(sender, alias, args);
                    }
                    return ImmutableList.of();
                }
            };
            this.getServer().getCommandMap().register(this.getName(), oil);
            Command diddle = new Command("diddle", "Diddle your friends", "Usage: /diddle <entity>", new ArrayList<>()) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    if (args.length == 1) {
                        List<Entity> entities = DiddyMC.this.getServer().selectEntities(sender, args[0]);
                        if (entities.size() > 0) {
                            DiddyMC.this.common.diddleCommand(sender, entities.get(0));
                            return true;
                        }
                    }
                    sender.sendMessage(Component.text(this.usageMessage));
                    return true;
                }
                @Override
                public @Nullable String getPermission() {
                    return "diddymc.diddle";
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    if (args.length == 1) {
                        return super.tabComplete(sender, alias, args);
                    }
                    return ImmutableList.of();
                }
            };
            this.getServer().getCommandMap().register(this.getName(), diddle);
            Command getaura = new Command("getaura", "display a users aura", "Usage: /getaura <member>", new ArrayList<>()) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    if (args.length == 1) {
                        Player player = DiddyMC.this.getServer().getPlayer(args[0]);
                        if (player != null) {
                            DiddyMC.this.common.getAuraCommand(sender, player);
                            return true;
                        }
                    }
                    sender.sendMessage(Component.text(this.usageMessage));
                    return true;
                }
                @Override
                public @Nullable String getPermission() {
                    return "diddymc.diddle";
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    if (args.length == 1) {
                        return super.tabComplete(sender, alias, args);
                    }
                    return ImmutableList.of();
                }
            };
            this.getServer().getCommandMap().register(this.getName(), getaura);
            this.getLogger().log(Level.INFO, "Registered paper commands!");
        }
        this.getLogger().log(Level.INFO, "DiddyMC Enabled");
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "DiddyMC Disabled");
    }

    public DiddyMCCommon getCommon() {
        return common;
    }
}
