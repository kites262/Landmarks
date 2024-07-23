package us.xuanxi.landmarks.service;

import org.bukkit.command.CommandSender;
import us.xuanxi.landmarks.config.Names;

public class PermissionChecker {
    public static boolean infoWithout(CommandSender sender, String permission){
        if(without(sender, permission)){
            sender.sendMessage(Names.msg_no_permission);
            return true;
        }else{
            return false;
        }
    }

    public static boolean without(CommandSender sender, String permission){
        if(sender.hasPermission(Names.permission_admin)){
            return false;
        }
        return !sender.hasPermission(permission);
    }
}
