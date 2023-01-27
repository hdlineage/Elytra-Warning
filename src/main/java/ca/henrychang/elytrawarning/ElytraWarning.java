package ca.henrychang.elytrawarning;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraWarning extends JavaPlugin {

    ElytraEventHandler eventHandler;
    int critical = 5;
    int warning = 10;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage("Elytra Warning Plugin Version 1.0");
        getServer().getConsoleSender().sendMessage("Elytra Warning Plugin Starting...");

        String warning_string = "Warning-Threshold";
        String critical_string = "Critical-Threshold";
        if (!getConfig().contains(critical_string) || !getConfig().contains(warning_string)){
            getConfig().set(warning_string, warning);
            getConfig().set(critical_string, critical);
            saveConfig();
        }else {
            int warning_temp = Integer.parseInt(getConfig().getString(warning_string));
            int critical_temp = Integer.parseInt(getConfig().getString(critical_string));
            if(warning_temp > 99 || critical_temp < 1 || critical_temp >= warning_temp) {
                getConfig().set(warning_string, warning);
                getConfig().set(critical_string, critical);
                saveConfig();
            }else {
                warning = warning_temp;
                critical = critical_temp;
            }
        }

        eventHandler = new ElytraEventHandler(this);
        getServer().getPluginManager().registerEvents(eventHandler, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage("Elytra Warning Plugin Stopping...");
    }
}
