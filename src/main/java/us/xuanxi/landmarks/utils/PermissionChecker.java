package us.xuanxi.landmarks.utils;

import org.bukkit.command.CommandSender;
import us.xuanxi.landmarks.data.Finals;

public class PermissionChecker {
    public static boolean infoWithout(CommandSender sender, String permission){
        if(without(sender, permission)){
            sender.sendMessage(Finals.msg_no_permission);
            return true;
        }else{
            return false;
        }
    }

    public static boolean without(CommandSender sender, String permission){
        if(sender.hasPermission(Finals.permission_admin)){
            return false;
        }
        return !sender.hasPermission(permission);
    }
}
