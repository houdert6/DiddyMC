package me.houdert6.paper.util;

import me.houdert6.common.util.MessageSender;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

/**
 * Sends DiddyMC messages to a CommandSender using adventure components
 */
public class PaperMessageSender extends MessageSender {
    private TextComponent prefix;

    @Override
    public void loadPrefix(JavaPlugin plugin) {
        this.prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().getString("prefix", "&0[&4&kh&6Diddy-Bot&4&kh&0] &r"));
    }

    @Override
    public void sendMessage(CommandSender sender, String message, String hoverInfo, boolean hasPrefix, boolean isRed) {
        Component c;
        if (hasPrefix) {
            c = this.prefix.append(Component.text(message));
        } else {
            c = Component.text(message);
        }
        if (hoverInfo != null) {
            c = c.hoverEvent(HoverEvent.showText(Component.text(hoverInfo)));
        }
        if (isRed) {
            c = c.color(NamedTextColor.RED);
        }
        sender.sendMessage(c);
    }

    @Override
    public void broadcastMessage(Server server, String message, String hoverInfo, Entity... mentions) {
        if (mentions.length > 0) {
            ArrayList<TextComponent> msgParts = new ArrayList<>();
            int i = message.indexOf("%m");
            int start = 0;
            while (i >= 0) {
                msgParts.add(Component.text(message.substring(start, i)).hoverEvent(HoverEvent.showText(Component.text(hoverInfo))));
                i += 2;
                Entity mention = mentions[Integer.parseInt(message.substring(i, i + 1)) - 1];
                msgParts.add(Component.text("@" + mention.getName()).color(NamedTextColor.BLUE).hoverEvent(HoverEvent.showEntity(Key.key(mention.getType().getKey().getKey()), mention.getUniqueId(), Component.text(mention.getName()))));
                start = i + 1;
                i = message.indexOf("%m", i);
            }
            msgParts.add(Component.text(message.substring(start)).hoverEvent(HoverEvent.showText(Component.text(hoverInfo))));
            TextComponent c = this.prefix.hoverEvent(HoverEvent.showText(Component.text(hoverInfo)));
            for (TextComponent text : msgParts) {
                c = c.append(text);
            }
            server.broadcast(c);
            return;
        }
        server.broadcast(this.prefix.append(Component.text(message)).hoverEvent(HoverEvent.showText(Component.text(hoverInfo))));
    }
}
