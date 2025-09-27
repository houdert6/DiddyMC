package me.houdert6.paper.bootstrap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandLifecycleHandler implements LifecycleEventHandler<ReloadableRegistrarEvent<Commands>> {
    private final DiddyMCBootstrap bootstrap;
    private final BootstrapContext context;

    public CommandLifecycleHandler(DiddyMCBootstrap bootstrap, BootstrapContext context) {
        this.bootstrap = bootstrap;
        this.context = context;
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this);
    }

    @Override
    public void run(@NotNull ReloadableRegistrarEvent<Commands> commands) {
        // rizzme
        LiteralArgumentBuilder<CommandSourceStack> rizzme = Commands.literal("rizzme").requires(sender -> sender.getSender().hasPermission("diddymc.rizzme")).executes((ctx) -> {
            bootstrap.plugin.getCommon().rizzMeCommand(ctx.getSource().getExecutor() != null ? ctx.getSource().getExecutor() : ctx.getSource().getSender());
            return Command.SINGLE_SUCCESS;
        });
        commands.registrar().register(rizzme.build(), "Receive a random pickup line!");
        // oil
        LiteralArgumentBuilder<CommandSourceStack> oil = Commands.literal("oil").requires(sender -> sender.getSender().hasPermission("diddymc.oil")).then(Commands.argument("entity", ArgumentTypes.entity()).executes((ctx) -> {
            List<Entity> entities = ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
            if (entities.size() > 0) {
                bootstrap.plugin.getCommon().oilCommand(ctx.getSource().getExecutor() != null ? ctx.getSource().getExecutor() : ctx.getSource().getSender(),entities.getFirst());
            } else {
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }));
        commands.registrar().register(oil.build(), "oil up your friends");
        // diddle
        LiteralArgumentBuilder<CommandSourceStack> diddle = Commands.literal("diddle").requires(sender -> sender.getSender().hasPermission("diddymc.diddle")).then(Commands.argument("entity", ArgumentTypes.entity()).executes((ctx) -> {
            List<Entity> entities = ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
            if (entities.size() > 0) {
                bootstrap.plugin.getCommon().diddleCommand(ctx.getSource().getExecutor() != null ? ctx.getSource().getExecutor() : ctx.getSource().getSender(),entities.getFirst());
            } else {
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }));
        commands.registrar().register(diddle.build(), "Diddle your friends");
        // getaura
        LiteralArgumentBuilder<CommandSourceStack> getaura = Commands.literal("getaura").requires(sender -> sender.getSender().hasPermission("diddymc.getaura")).then(Commands.argument("member", ArgumentTypes.player()).executes((ctx) -> {
            List<Player> players = ctx.getArgument("member", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
            if (players.size() > 0) {
                bootstrap.plugin.getCommon().getAuraCommand(ctx.getSource().getExecutor() != null ? ctx.getSource().getExecutor() : ctx.getSource().getSender(), players.getFirst());
            } else {
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }));
        commands.registrar().register(getaura.build(), "display a users aura");
        context.getLogger().info(Component.text("Registered brigadier commands!"));
    }
}
