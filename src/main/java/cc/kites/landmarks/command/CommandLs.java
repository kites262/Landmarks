package cc.kites.landmarks.command;

import cc.kites.landmarks.service.LandmarkService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandLs extends CommandLandmark {

    public CommandLs(LandmarkService lms) {
        super(lms);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        lms.requestListLandmark(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }
}
