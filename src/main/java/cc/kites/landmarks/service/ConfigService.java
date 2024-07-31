package cc.kites.landmarks.service;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class ConfigService {
    private Configuration config;

    public ConfigService(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public void reloadConfig(Configuration config){
        this.config = config;
    }

    public boolean isNull(String path){
        return config.get(path) == null;
    }

    public String getString(String path){
        return String.valueOf(config.get(path));
    }

    public Double getAsDouble(String path){
        return Double.parseDouble(getString(path));
    }

    public Float getAsFloat(String path){
        return Float.parseFloat(getString(path));
    }

    public void setConfig(String path, Object object){
        config.set(path, object);
    }

    public void delConfig(String path){
        config.set(path, null);
    }

    public ConfigurationSection getConfigurationSection(String path){
        return config.getConfigurationSection(path);
    }

}
