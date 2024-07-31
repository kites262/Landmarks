package cc.kites.landmarks;

import cc.kites.landmarks.command.CommandGo;
import cc.kites.landmarks.command.CommandLandmark;
import cc.kites.landmarks.command.CommandLs;
import cc.kites.landmarks.command.CommandNew;
import cc.kites.landmarks.service.ConfigService;
import cc.kites.landmarks.service.LandmarkService;
import org.bukkit.plugin.java.JavaPlugin;
import cc.kites.landmarks.config.Names;

import java.util.Objects;
import java.util.logging.Logger;


public final class Landmarks extends JavaPlugin {
    private final Logger logger = this.getLogger();
    private final ConfigService cs = new ConfigService(this);
    private final LandmarkService lms = new LandmarkService(this, cs);

    @Override
    public void onEnable() {
        log("Landmarks plugin by flew_kites, ready to enable.");
        // Plugin startup logic
        this.saveDefaultConfig();

        //register commands
        Objects.requireNonNull(this.getCommand(Names.command_prefix))
                .setExecutor(new CommandLandmark(lms));

        Objects.requireNonNull(this.getCommand(Names.command_go))
                .setExecutor(new CommandGo(lms));

        Objects.requireNonNull(this.getCommand(Names.command_ls))
                .setExecutor(new CommandLs(lms));

        Objects.requireNonNull(this.getCommand(Names.command_new))
                .setExecutor(new CommandNew(lms));

        log("Landmarks plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        log("Saving config to file...");
        // Plugin shutdown logic
        lms.saveToFile();
        log("Landmarks plugin has been disabled!");
    }

    public void log(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.severe(message);
    }

    public void logWarning(String message) {
        logger.warning(message);
    }
}
