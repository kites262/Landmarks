package us.xuanxi.landmarks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import us.xuanxi.landmarks.config.Names;
import us.xuanxi.landmarks.service.LandmarkService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGo implements CommandExecutor, TabCompleter {
    LandmarkService lms;
    public CommandGo(LandmarkService lms){
        this.lms = lms;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            lms.goLandmark(sender, "home");
        } else if(args.length == 1) {
            lms.goLandmark(sender, args[0]);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase(Names.command_go)) {
            return new ArrayList<>(lms.getLandmarks());
        }
        return Collections.emptyList();
    }
}
