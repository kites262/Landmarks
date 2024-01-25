package us.xuanxi.landmarks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import us.xuanxi.landmarks.Landmarks;
import us.xuanxi.landmarks.data.Finals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigReader {
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
        if(!getBoolean(Finals.config_path_prefix + name + Finals.config_path_with_service)){
            return null;
        }
        World world = Bukkit.getWorld(getString(Finals.config_path_prefix + name + Finals.config_path_with_location_world));
        return new Location(
                world,
                getDouble(Finals.config_path_prefix + name + Finals.config_path_with_location_x),
                getDouble(Finals.config_path_prefix + name + Finals.config_path_with_location_y),
                getDouble(Finals.config_path_prefix + name + Finals.config_path_with_location_z),
                Float.parseFloat(getString(Finals.config_path_prefix + name + Finals.config_path_with_location_yaw)),
                Float.parseFloat(getString(Finals.config_path_prefix + name + Finals.config_path_with_location_pitch))
        );
    }

    public List<String> getLandmarks(){
        ConfigurationSection landmarksSection = config.getConfigurationSection("landmarks");
        if(landmarksSection == null) {
            return Collections.emptyList();
        } else {
            List<String> list = new ArrayList<>(landmarksSection.getKeys(false));
            Collections.sort(list);
            return list;
        }
    }

    public void setLocation(String name, Location location){
        if(name == null || location == null){
            return;
        }
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_service, true);
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_world, location.getWorld().getName());
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_x, location.getX());
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_y, location.getY());
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_z, location.getZ());
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_yaw, location.getYaw());
        setConfig(Finals.config_path_prefix + name + Finals.config_path_with_location_pitch, location.getPitch());
        save();
    }

    public void delLocation(String name){
        if(name == null){
            return;
        }
        delConfig(Finals.config_path_prefix + name);
        save();
    }

    public void save(){
        plugin.saveConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void reloadFromConfigFile(){
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
