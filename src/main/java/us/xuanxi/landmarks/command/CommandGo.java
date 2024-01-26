package us.xuanxi.landmarks.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import us.xuanxi.landmarks.data.Finals;
import us.xuanxi.landmarks.utils.ConfigReader;
import us.xuanxi.landmarks.utils.PermissionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGo implements CommandExecutor, TabCompleter {
    ConfigReader cr;
    public CommandGo(ConfigReader cr){
        this.cr = cr;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            goLandmark(sender, args[0]);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase(Finals.command_go)) {
            return new ArrayList<>(cr.getLandmarks());
        }
        return Collections.emptyList();
    }

    public void goLandmark(CommandSender sender, String name){
        if(PermissionChecker.infoWithout(sender, Finals.permission_command_go)) return;
        if(sender instanceof Player player){
            if(cr.getLocation(name) != null){
                Bukkit.getScheduler().runTaskLater(cr.getPlugin(), () -> {
                    player.teleportAsync(cr.getLocation(name), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }, 1L);
                sender.sendMessage(Finals.msg_teleported_to_landmark + name);
            }else{
                sender.sendMessage(Finals.msg_landmark_not_exist + name);
            }
        }else{
            sender.sendMessage(Finals.msg_not_player);
        }
    }
}
