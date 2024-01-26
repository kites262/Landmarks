package us.xuanxi.landmarks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.xuanxi.landmarks.data.Finals;
import us.xuanxi.landmarks.utils.ConfigReader;
import us.xuanxi.landmarks.utils.PermissionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLandmark implements CommandExecutor, TabCompleter {

    ConfigReader cr;
    public CommandLandmark(ConfigReader cr){
        this.cr = cr;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2 && args[0].equals(Finals.command_new)){
            newLandmark(sender, args[1]);
        }else if(args.length == 2 && args[0].equals(Finals.command_rm)) {
            rmLandmark(sender, args[1]);
        }else if(args.length == 1 && args[0].equals(Finals.command_ls)) {
            lsLandmark(sender);
        }else if(args.length == 2 && args[0].equals(Finals.command_go)){
            goLandmark(sender, args[1]);
        }else if(args.length == 1 && args[0].equals(Finals.command_reload)){
            reloadPlugin(sender);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(Finals.command_prefix)) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                if (!PermissionChecker.without(sender, Finals.permission_command_new)){
                    list.add(Finals.command_new);
                }
                if (!PermissionChecker.without(sender, Finals.permission_command_rm)){
                    list.add(Finals.command_rm);
                }
                if (!PermissionChecker.without(sender, Finals.permission_command_go)) {
                    list.add(Finals.command_go);
                }
                if (!PermissionChecker.without(sender, Finals.permission_command_ls)) {
                    list.add(Finals.command_ls);
                }
                if(!PermissionChecker.without(sender, Finals.permission_command_reload)){
                    list.add(Finals.command_reload);
                }
                return list;
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Finals.command_go)) {
                return new ArrayList<>(cr.getLandmarks());
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Finals.command_rm)) {
                return new ArrayList<>(cr.getLandmarks());
            }
        }
        return Collections.emptyList();
    }

    public void newLandmark(CommandSender sender, String name){
        if(PermissionChecker.without(sender, Finals.permission_command_new)) return;
        if(sender instanceof Player player){
            if(cr.getLocation(name) == null){
                sender.sendMessage(Finals.msg_landmark_created + name);
            }else{
                sender.sendMessage(Finals.msg_landmark_updated + name);
            }
            cr.setLandmark(name, player.getLocation(), sender.getName());
        }else{
            sender.sendMessage(Finals.msg_not_player);
        }
    }

    public void rmLandmark(CommandSender sender, String name){
        if(PermissionChecker.without(sender, Finals.permission_command_rm)) return;
        if(cr.getLocation(name) != null){
            cr.delLocation(name);
            sender.sendMessage(Finals.msg_landmark_removed + name);
        }else{
            sender.sendMessage(Finals.msg_landmark_not_exist + name);
        }
    }

    public void lsLandmark(CommandSender sender){
        if(PermissionChecker.without(sender, Finals.permission_command_ls)) return;
        StringBuilder msg = new StringBuilder();
        int count = 0;
        for(String name : cr.getLandmarks()){
            msg.append(name).append(", ");
            count++;
        }
        if(count != 0) {
            msg.deleteCharAt(msg.length() - 2);
            msg.deleteCharAt(msg.length() - 1);
        }
        sender.sendMessage("Landmarks(" + count + "): ");
        sender.sendMessage(msg.toString());
    }

    public void goLandmark(CommandSender sender, String name){
        CommandGo cmd = new CommandGo(cr);
        cmd.goLandmark(sender, name);
    }

    public void reloadPlugin(CommandSender sender){
        if(PermissionChecker.without(sender, Finals.permission_command_reload)) return;
        cr.reloadFromConfigFile();
        sender.sendMessage(Finals.msg_plugin_reload);
    }
}
