package cc.kites.landmarks.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import cc.kites.landmarks.Landmarks;
import cc.kites.landmarks.config.Names;

import java.util.*;

public class LandmarkService {
    private final Landmarks plugin;
    private final ConfigService cs;

    public LandmarkService(Landmarks plugin, ConfigService cs) {
        this.plugin = plugin;
        this.cs = cs;
    }

    public List<String> filterLandmarks(String input) {
        input = input.toLowerCase();
        List<String> allLandmarks = new ArrayList<>(getLandmarkList());
        List<String> suggestions = new ArrayList<>();

        for (String landmark : allLandmarks) {
            if (landmark.toLowerCase().startsWith(input)) {
                suggestions.add(landmark);
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    public List<String> getLandmarkList() {
        ConfigurationSection landmarksSection = cs.getConfigurationSection("landmarks");
        if (landmarksSection == null) {
            return Collections.emptyList();
        } else {
            List<String> list = new ArrayList<>(landmarksSection.getKeys(false));
            Collections.sort(list);
            return list;
        }
    }

    public Map<String, String> getLandmarksMap() {
        Map<String, String> map = new TreeMap<>();
        for (String name : getLandmarkList()) {
            map.put(name, getLandmarkCreator(name));
        }
        return map;
    }

    public Landmarks getPlugin() {
        return plugin;
    }

    public void requestCreateLandmark(CommandSender sender, String name) {
        if (PermissionChecker.infoWithout(sender, Names.permission_command_new)) return;
        if (sender instanceof Player player) {
            if (getLandmark(name) == null) {
                sender.sendMessage(Names.msg_landmark_created + name);
            } else {
                sender.sendMessage(Names.msg_landmark_updated + name);
            }
            setLandmark(name, player.getLocation(), sender.getName());
        } else {
            sender.sendMessage(Names.msg_not_player);
        }
    }

    public void requestDeleteLandmark(CommandSender sender, String name) {
        if (PermissionChecker.infoWithout(sender, Names.permission_command_rm)) return;
        if (getLandmark(name) != null) {
            plugin.getLogger().info("Player " + sender.getName() + " deleted a landmark " + getLandmark(name));
            delLandmark(name);
            sender.sendMessage(Names.msg_landmark_removed + name);
        } else {
            sender.sendMessage(Names.msg_landmark_not_exist + name);
        }
    }

    public void requestListLandmark(CommandSender sender) {
        Map<String, String> landmarksMap = getLandmarksMap();
        ComponentBuilder<TextComponent, TextComponent.Builder> cb = Component.text();
        for (Map.Entry<String, String> entry : landmarksMap.entrySet()) {
            Component nameComponent = Component.text(entry.getKey());
            Component creatorComponent = Component.text(entry.getValue());
            nameComponent = nameComponent.clickEvent(
                    ClickEvent.runCommand("/" + Names.command_prefix + " " + Names.command_go + " " + entry.getKey())
            );

            cb.append(nameComponent.hoverEvent(HoverEvent.showText(creatorComponent)))
                    .append(Component.text(", "));
        }

        sender.sendPlainMessage("Landmarks( " + landmarksMap.size() + " ): ");
        sender.sendMessage(cb.build());
        sender.sendPlainMessage("--------------------");
    }

    public void requestRenameLandmark(CommandSender sender, String oldName, String newName) {
        if (PermissionChecker.infoWithout(sender, Names.permission_command_mv)) return;
        if (getLandmark(oldName) != null) {
            if (getLandmark(newName) == null) {
                setLandmarkOnly(newName, getLandmark(oldName));
                setLandmarkCreator(newName, getLandmarkCreator(oldName));
                delLandmark(oldName);
                sender.sendMessage(Names.msg_landmark_updated + oldName + " -> " + newName);
            } else {
                sender.sendMessage(Names.msg_landmark_already_existed + newName);
            }
        } else {
            sender.sendMessage(Names.msg_landmark_not_exist + oldName);
        }

    }

    public void requestTeleportLandmark(CommandSender sender, String name) {
        if (PermissionChecker.infoWithout(sender, Names.permission_command_go)) return;
        if (sender instanceof Player player) {
            Location location = getLandmark(name);
            if (location != null) {
                teleportPlayer(player, location);
                sender.sendMessage(Names.msg_teleported_to_landmark + name);
            } else {
                sender.sendMessage(Names.msg_landmark_not_exist + name);
            }
        } else {
            sender.sendMessage(Names.msg_not_player);
        }
    }

    public void reloadFromFile() {
        plugin.reloadConfig();
        cs.reloadConfig(plugin.getConfig());
    }

    public void saveToFile() {
        plugin.saveConfig();
        plugin.reloadConfig();
        cs.reloadConfig(plugin.getConfig());
    }

    private void delLandmark(String name) {
        if (name == null) {
            return;
        }
        cs.delConfig(Names.config_path_prefix + name);
        saveToFile();
    }

    private Location getLandmark(String name) {
        try {
            if (name.contains(".") || cs.isNull(Names.config_path_prefix + name)) {
                return null;
            }
            World world = Bukkit.getWorld(cs.getString(Names.config_path_prefix + name + Names.config_path_with_location_world));
            return new Location(
                    world,
                    cs.getAsDouble(Names.config_path_prefix + name + Names.config_path_with_location_x),
                    cs.getAsDouble(Names.config_path_prefix + name + Names.config_path_with_location_y),
                    cs.getAsDouble(Names.config_path_prefix + name + Names.config_path_with_location_z),
                    cs.getAsFloat(Names.config_path_prefix + name + Names.config_path_with_location_yaw),
                    cs.getAsFloat(Names.config_path_prefix + name + Names.config_path_with_location_pitch)
            );
        } catch (NullPointerException e) {
            getPlugin().logError("NullPointerException when getting location: " + name);
            return null;
        } catch (Exception e) {
            getPlugin().logError("UnknownException when getting location: " + name);
            return null;
        }
    }

    private String getLandmarkCreator(String name) {
        return cs.getString(Names.config_path_prefix + name + Names.config_path_with_creator);
    }

    private void teleportPlayer(Player player, Location location) {
        Bukkit.getScheduler().runTaskLater(
                getPlugin(), () -> {
                    player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, 1L);
    }

    private void setLandmark(String landmarkName, Location location, String player) {
        if (landmarkName == null || location == null) {
            return;
        }
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_creator, player);
        setLandmarkOnly(landmarkName, location);
        plugin.getLogger().info("Player " + player + " created a new landmark " + landmarkName + " at " + location.toString());
    }

    private void setLandmarkCreator(String landmarkName, String player) {
        if (landmarkName == null || getLandmark(landmarkName) == null || player == null) {
            return;
        }
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_creator, player);
        saveToFile();
    }

    private void setLandmarkOnly(String landmarkName, Location location) {
        if (landmarkName == null || location == null) {
            return;
        }
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_world, location.getWorld().getName());
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_x, location.getX());
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_y, location.getY());
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_z, location.getZ());
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_yaw, location.getYaw());
        cs.setConfig(Names.config_path_prefix + landmarkName + Names.config_path_with_location_pitch, location.getPitch());
        saveToFile();
    }











}
