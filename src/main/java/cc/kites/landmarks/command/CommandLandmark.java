package cc.kites.landmarks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import cc.kites.landmarks.config.Names;
import cc.kites.landmarks.service.LandmarkService;
import cc.kites.landmarks.service.PermissionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLandmark implements CommandExecutor, TabCompleter {

    LandmarkService lms;
    public CommandLandmark(LandmarkService lms){
        this.lms = lms;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2 && args[0].equals(Names.command_new)){
            lms.requestCreateLandmark(sender, args[1]);
        }else if(args.length == 2 && args[0].equals(Names.command_rm)) {
            lms.requestDeleteLandmark(sender, args[1]);
        }else if(args.length == 1 && args[0].equals(Names.command_ls)) {
            lms.requestListLandmark(sender);
        }else if(args.length == 2 && args[0].equals(Names.command_go)) {
            lms.requestTeleportLandmark(sender, args[1]);
        }else if(args.length == 3 && args[0].equals(Names.command_mv)) {
            lms.requestRenameLandmark(sender, args[1], args[2]);
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
                if(!PermissionChecker.without(sender, Names.permission_command_mv)){
                    list.add(Names.command_mv);
                }
                return list;
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Names.command_go)) {
                return lms.filterLandmarks(args[1]);
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Names.command_rm)) {
                return lms.filterLandmarks(args[1]);
            } else if (args.length == 2 && args[0].equalsIgnoreCase(Names.command_mv)) {
                return lms.filterLandmarks(args[1]);
            }
        }
        return Collections.emptyList();
    }

    public void reloadPlugin(CommandSender sender){
        if(PermissionChecker.infoWithout(sender, Names.permission_command_reload)) return;
        lms.reloadFromFile();
        sender.sendMessage(Names.msg_plugin_reload);
    }
}
