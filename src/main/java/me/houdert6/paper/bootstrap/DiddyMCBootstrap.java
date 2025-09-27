package me.houdert6.paper.bootstrap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.houdert6.paper.DiddyMC;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DiddyMCBootstrap implements PluginBootstrap {
    DiddyMC plugin;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        try {
            Class.forName("io.papermc.paper.command.brigadier.Commands");
        } catch (Exception e) {
            context.getLogger().info(Component.text("Running without brigadier commands."));
            return;
        }
        this.newCommandLifecycleHandler(context);
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        this.plugin = new DiddyMC();
        return this.plugin;
    }

    public Object newCommandLifecycleHandler(BootstrapContext context) {
        try {
            Class<?> handlerClass = Class.forName("me.houdert6.paper.bootstrap.CommandLifecycleHandler");
            return handlerClass.getConstructors()[0].newInstance(this, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
