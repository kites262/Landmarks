package us.xuanxi.landmarks.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.xuanxi.landmarks.Landmarks;
import us.xuanxi.landmarks.config.Names;

import java.util.*;

public class LandmarkService {
    Configuration config;
    Landmarks plugin;

    public LandmarkService(Landmarks plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    private boolean isNull(String path){
        return config.get(path) == null;
    }

    private String getString(String path){
        return String.valueOf(config.get(path));
    }

    private Double getDouble(String path){
        return Double.parseDouble(getString(path));
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
        try{
            if(isNull(Names.config_path_prefix + name)){
                return null;
            }
            World world = Bukkit.getWorld(getString(Names.config_path_prefix + name + Names.config_path_with_location_world));
            return new Location(
                    world,
                    getDouble(Names.config_path_prefix + name + Names.config_path_with_location_x),
                    getDouble(Names.config_path_prefix + name + Names.config_path_with_location_y),
                    getDouble(Names.config_path_prefix + name + Names.config_path_with_location_z),
                    Float.parseFloat(getString(Names.config_path_prefix + name + Names.config_path_with_location_yaw)),
                    Float.parseFloat(getString(Names.config_path_prefix + name + Names.config_path_with_location_pitch))
            );
        }catch (NullPointerException e) {
            getPlugin().logError("NullPointerException when getting location: " + name);
            return null;
        }catch (Exception e){
            getPlugin().logError("UnknownException when getting location: " + name);
            return null;
        }
    }

    public String getLandmarkCreator(String name){
        return getString(Names.config_path_prefix + name + Names.config_path_with_creator);
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

    public Map<String, String> getLandmarksMap(){
        Map<String, String> map = new TreeMap<>();
        for(String name : getLandmarks()){
            map.put(name, getLandmarkCreator(name));
        }
        return map;
    }

    public void goLandmark(CommandSender sender, String name){
        if(PermissionChecker.infoWithout(sender, Names.permission_command_go)) return;
        if(sender instanceof Player player){
            if(getLocation(name) != null){
                Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                    player.teleportAsync(getLocation(name), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }, 1L);
                sender.sendMessage(Names.msg_teleported_to_landmark + name);
            }else{
                sender.sendMessage(Names.msg_landmark_not_exist + name);
            }
        }else{
            sender.sendMessage(Names.msg_not_player);
        }
    }

    public void setLandmarkOnly(String landmarkName, Location location){
        if(landmarkName == null || location == null){
            return;
        }
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_world, location.getWorld().getName());
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_x, location.getX());
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_y, location.getY());
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_z, location.getZ());
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_yaw, location.getYaw());
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_pitch, location.getPitch());
        save();
        plugin.getLogger().info("Created a new landmark (ONLY)" + landmarkName + " at " + location.toString());
    }

    public void setLandmark(String landmarkName, Location location, String playername){
        if(landmarkName == null || location == null){
            return;
        }
        setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_creator, playername);
        setLandmarkOnly(landmarkName, location);
        plugin.getLogger().info("Player " + playername + " created a new landmark " + landmarkName + " at " + location.toString());
    }

    public void delLocation(String name){
        if(name == null){
            return;
        }
        delConfig(Names.config_path_prefix + name);
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
