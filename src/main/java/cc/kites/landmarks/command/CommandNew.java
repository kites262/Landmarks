package cc.kites.landmarks.command;

import cc.kites.landmarks.config.Names;
import cc.kites.landmarks.service.LandmarkService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandNew extends CommandLandmark{
    public CommandNew(LandmarkService lms) {
        super(lms);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 1){
            lms.requestCreateLandmark(sender, args[0]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }
}
