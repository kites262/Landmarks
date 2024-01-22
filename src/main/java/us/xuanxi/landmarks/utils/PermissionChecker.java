package us.xuanxi.landmarks.utils;

import org.bukkit.command.CommandSender;
import us.xuanxi.landmarks.data.Finals;

public class PermissionChecker {
    public static boolean without(CommandSender sender, String permission){
        if(sender.hasPermission(Finals.permission_admin)){
            return false;
        }

        if(sender.hasPermission(permission)){
            return false;
        }else{
            sender.sendMessage(Finals.msg_no_permission);
            return true;
        }
    }
}
