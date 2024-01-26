package us.xuanxi.landmarks.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;

public class TextParser {
    public static void sendHoverMessage(CommandSender sender, String text, String hoverText){
        Component hoverMsg = Component.text(hoverText);
        Component msg = Component.text(text).hoverEvent(HoverEvent.showText(hoverMsg));
        sender.sendMessage(msg);
    }
}
