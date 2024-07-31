package cc.kites.landmarks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import cc.kites.landmarks.config.Names;
import cc.kites.landmarks.service.LandmarkService;

import java.util.Collections;
import java.util.List;

public class CommandGo extends CommandLandmark {
    public CommandGo(LandmarkService lms){
        super(lms);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            lms.requestTeleportLandmark(sender, "home");
        } else if(args.length == 1) {
            lms.requestTeleportLandmark(sender, args[0]);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase(Names.command_go)) {
            return lms.filterLandmarks(args[0]);
        }
        return Collections.emptyList();
    }
}
