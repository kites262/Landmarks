package us.xuanxi.landmarks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import us.xuanxi.landmarks.Landmarks;

import java.util.Collections;
import java.util.Set;

public class ConfigReader {
    public static final String prefix = "landmarks.";
    public static final String with_service = ".enabled";
    public static final String with_location_world = ".location.world";
    public static final String with_location_x = ".location.x";
    public static final String with_location_z = ".location.y";
    public static final String with_location_y = ".location.z";
    public static final String with_location_yaw = ".location.yaw";
    public static final String with_location_pitch = ".location.pitch";

    Configuration config;
    Landmarks plugin;

    public ConfigReader(Landmarks plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    private String getString(String path){
        return String.valueOf(config.get(path));
    }

    private Double getDouble(String path){
        return Double.parseDouble(getString(path));
    }

    private Boolean getBoolean(String path){
        return Boolean.parseBoolean(getString(path));
    }

    private void setConfig(String path, Object object){
        config.set(path, object);
    }

    private void delConfig(String path){
        config.set(path, null);
    }

    public Landmarks getPlugin(){
        return plugin;
    }

    public Location getLocation(String name){
        if(!getBoolean(prefix + name + with_service)){
            return null;
        }
        World world = Bukkit.getWorld(getString(prefix + name + with_location_world));
        return new Location(
                world,
                getDouble(prefix + name + with_location_x),
                getDouble(prefix + name + with_location_y),
                getDouble(prefix + name + with_location_z),
                Float.parseFloat(getString(prefix + name + with_location_yaw)),
                Float.parseFloat(getString(prefix + name + with_location_pitch))
        );
    }

    public Set<String> getLandmarks() {
        ConfigurationSection landmarksSection = config.getConfigurationSection("landmarks");
        if (landmarksSection == null) {
            return Collections.emptySet();
        } else {
            return landmarksSection.getKeys(false);
        }
    }

    public void setLocation(String name, Location location){
        if(name == null || location == null){
            return;
        }
        setConfig(prefix + name + with_service, true);
        setConfig(prefix + name + with_location_world, location.getWorld().getName());
        setConfig(prefix + name + with_location_x, location.getX());
        setConfig(prefix + name + with_location_y, location.getY());
        setConfig(prefix + name + with_location_z, location.getZ());
        setConfig(prefix + name + with_location_yaw, location.getYaw());
        setConfig(prefix + name + with_location_pitch, location.getPitch());
        save();
    }

    public void delLocation(String name){
        if(name == null){
            return;
        }
        delConfig(prefix + name);
        save();
    }

    public void save(){
        plugin.saveConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
