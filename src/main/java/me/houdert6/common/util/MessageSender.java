package me.houdert6.common.util;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Sends DiddyMC messages to a CommandSender
 */
@SuppressWarnings("deprecation") // Paper uses a different implementation with adventure text
public class MessageSender {
    private String prefix;

    public void loadPrefix(JavaPlugin plugin) {
         this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix", "&0[&4&kh&6Diddy-Bot&4&kh&0] &r"));
    }

    /**
     * Sends a message to a sender
     * @param sender The sender to receive the message
     * @param message The message to send
     */
    public void sendMessage(CommandSender sender, String message, String hoverInfo, boolean hasPrefix, boolean isRed) {
        BaseComponent[] components;
        if (hasPrefix) {
            components = new ComponentBuilder().append(this.prefix).append(message).create();
        } else {
            components = new ComponentBuilder().append(message).create();
        }
        for (BaseComponent c : components) {
            if (hoverInfo != null) {
                c.getHoverEvent().addContent(new Text(hoverInfo));
            }
            if (isRed) {
                c.setColor(ChatColor.RED);
            }
        }
        sender.spigot().sendMessage(components);
    }

    /**
     * Sends a message with the DiddyMC prefix
     * @param server The server
     * @param message The message to send
     */
    public void broadcastMessage(Server server, String message, String hoverInfo, Entity... mentions) {
        BaseComponent[] components;
        if (mentions.length > 0) {
            ArrayList<BaseComponent> msgParts = new ArrayList<>();
            components = TextComponent.fromLegacyText(this.prefix);
            int i = message.indexOf("%m");
            int start = 0;
            while (i >= 0) {
                Collections.addAll(msgParts, TextComponent.fromLegacyText(message.substring(start, i)));
                i += 2;
                Entity mention = mentions[Integer.parseInt(message.substring(i, i + 1)) - 1];
                TextComponent mentionComponent = new TextComponent();
                mentionComponent.setText("@" + mention.getName());
                mentionComponent.setColor(ChatColor.BLUE);
                TextComponent nameComponent = new TextComponent();
                nameComponent.setText(mention.getName());
                mentionComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new net.md_5.bungee.api.chat.hover.content.Entity(mention.getType().getKey().getKey(), mention.getUniqueId().toString(), nameComponent)));
                msgParts.add(mentionComponent);
                start = i + 1;
                i = message.indexOf("%m", i);
            }
            Collections.addAll(msgParts, TextComponent.fromLegacyText(message.substring(start)));
            components[components.length - 1].setExtra(msgParts);
        } else {
            components = new ComponentBuilder().append(this.prefix).append(message).create();
        }
        for (BaseComponent c : components) {
            if (c.getHoverEvent() == null || c.getHoverEvent().getContents().isEmpty()) {
                c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverInfo)));
            }
        }
        server.spigot().broadcast(components);
    }
}
