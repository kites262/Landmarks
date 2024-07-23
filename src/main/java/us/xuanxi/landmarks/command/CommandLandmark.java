package us.xuanxi.landmarks.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.xuanxi.landmarks.config.Names;
import us.xuanxi.landmarks.service.LandmarkService;
import us.xuanxi.landmarks.service.PermissionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandLandmark implements CommandExecutor, TabCompleter {

    LandmarkService lms;
    public CommandLandmark(LandmarkService lms){
        this.lms = lms;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2 && args[0].equals(Names.command_new)){
            newLandmark(sender, args[1]);
        }else if(args.length == 2 && args[0].equals(Names.command_rm)) {
            rmLandmark(sender, args[1]);
        }else if(args.length == 1 && args[0].equals(Names.command_ls)) {
            lsLandmark(sender);
        }else if(args.length == 2 && args[0].equals(Names.command_go)){
            goLandmark(sender, args[1]);
        }else if(args.length == 1 && args[0].equals(Names.command_reload)){
            reloadPlugin(sender);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(Names.command_prefix)) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                if (!PermissionChecker.without(sender, Names.permission_command_new)){
                    list.add(Names.command_new);
                }
                if (!PermissionChecker.without(sender, Names.permission_command_rm)){
                    list.add(Names.command_rm);
                }
                if (!PermissionChecker.without(sender, Names.permission_command_go)) {
                    list.add(Names.command_go);
                }
                if (!PermissionChecker.without(sender, Names.permission_command_ls)) {
                    list.add(Names.command_ls);
                }
                if(!PermissionChecker.without(sender, Names.permission_command_reload)){
                    list.add(Names.command_reload);
                }
                return list;
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Names.command_go)) {
                return new ArrayList<>(lms.getLandmarks());
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Names.command_rm)) {
                return new ArrayList<>(lms.getLandmarks());
            }
        }
        return Collections.emptyList();
    }

    public void newLandmark(CommandSender sender, String name){
        if(PermissionChecker.infoWithout(sender, Names.permission_command_new)) return;
        if(sender instanceof Player player){
            if(lms.getLocation(name) == null){
                sender.sendMessage(Names.msg_landmark_created + name);
            }else{
                sender.sendMessage(Names.msg_landmark_updated + name);
            }
            lms.setLandmark(name, player.getLocation(), sender.getName());
        }else{
            sender.sendMessage(Names.msg_not_player);
        }
    }

    public void rmLandmark(CommandSender sender, String name){
        if(PermissionChecker.infoWithout(sender, Names.permission_command_rm)) return;
        if(lms.getLocation(name) != null){
            lms.delLocation(name);
            sender.sendMessage(Names.msg_landmark_removed + name);
        }else{
            sender.sendMessage(Names.msg_landmark_not_exist + name);
        }
    }

    public void lsLandmark(CommandSender sender){
        Map<String, String> landmarksMap = lms.getLandmarksMap();
        ComponentBuilder<TextComponent, TextComponent.Builder> cb = Component.text();
        for(Map.Entry<String, String> entry : landmarksMap.entrySet()){
            Component nameComponent = Component.text(entry.getKey());
            Component creatorComponent = Component.text(entry.getValue());
            nameComponent = nameComponent.clickEvent(
                    ClickEvent.runCommand("/" + Names.command_prefix + " " + Names.command_go + " " + entry.getKey())
            );

            cb.append(nameComponent.hoverEvent(HoverEvent.showText(creatorComponent)))
                    .append(Component.text(", "));
        }

        sender.sendPlainMessage("Landmarks( " + landmarksMap.size() + " ): ");
        sender.sendMessage(cb.build());
        sender.sendPlainMessage("--------------------");
    }

    public void goLandmark(CommandSender sender, String name){
        lms.goLandmark(sender, name);
    }

    public void reloadPlugin(CommandSender sender){
        if(PermissionChecker.infoWithout(sender, Names.permission_command_reload)) return;
        lms.reloadFromConfigFile();
        sender.sendMessage(Names.msg_plugin_reload);
    }
}
