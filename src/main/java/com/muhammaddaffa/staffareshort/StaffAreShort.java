package com.muhammaddaffa.staffareshort;

import com.muhammaddaffa.mdlib.MDLib;
import com.muhammaddaffa.mdlib.configupdater.ConfigUpdater;
import com.muhammaddaffa.mdlib.utils.Config;
import com.muhammaddaffa.mdlib.utils.Logger;
import com.muhammaddaffa.mdlib.worldguards.listeners.RegionListener;
import com.muhammaddaffa.staffareshort.commands.FlyCommand;
import com.muhammaddaffa.staffareshort.commands.FlySpeedCommand;
import com.muhammaddaffa.staffareshort.commands.TempFlyCommand;
import com.muhammaddaffa.staffareshort.papi.PAPIExpansion;
import com.muhammaddaffa.staffareshort.tempfly.listeners.FlyListener;
import com.muhammaddaffa.staffareshort.tempfly.listeners.FlyWorldGuardListener;
import com.muhammaddaffa.staffareshort.tempfly.task.FlyTask;
import com.muhammaddaffa.staffareshort.tempfly.task.FlyingHungerLossTask;
import com.muhammaddaffa.staffareshort.users.User;
import com.muhammaddaffa.staffareshort.users.listeners.UserListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class StaffAreShort extends JavaPlugin {

    private static StaffAreShort instance;
    public static Config DEFAULT_CONFIG, DATA_CONFIG;

    @Override
    public void onLoad() {
        MDLib.inject(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            MDLib.registerWorldGuard();
        }
        MDLib.onEnable(this);
        // Create a config.yml
        DEFAULT_CONFIG = new Config("config.yml", null, true);
        DATA_CONFIG = new Config("data.yml", null, false);
        // Update the config
        this.updateConfig();

        // Register commands & listeners
        this.registerCommands();
        this.registerListeners();

        // Load user data
        User.load();
        // Start the fly task
        new FlyTask().runTaskTimer(this, 0L, 20L);
        new FlyingHungerLossTask().runTaskTimerAsynchronously(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        try {
            // Save the user flight status
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = User.find(player);
                user.setFlying(player.isFlying());
                user.setAllowFlight(player.getAllowFlight());
            }
            // Save user data
            User.save();
        } finally {
            MDLib.shutdown();
        }
    }

    private void registerCommands() {
        // Fly Command
        FlyCommand flyCommand = new FlyCommand();
        this.getCommand("fly").setExecutor(flyCommand);

        // FlySpeed Command
        FlySpeedCommand flySpeedCommand = new FlySpeedCommand();
        this.getCommand("flyspeed").setExecutor(flySpeedCommand);
        this.getCommand("flyspeed").setTabCompleter(flySpeedCommand);

        // Main Commands
        TempFlyCommand tempFlyCommand = new TempFlyCommand();
        this.getCommand("tempfly").setExecutor(tempFlyCommand);
        this.getCommand("tempfly").setTabCompleter(tempFlyCommand);
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        // Register events
        pm.registerEvents(new UserListener(), this);
        pm.registerEvents(new FlyListener(), this);
        // Register placeholder api
        if (pm.getPlugin("PlaceholderAPI") != null) {
            new PAPIExpansion().register();
        }
        // Register worldguard hook
        if (pm.getPlugin("WorldGuard") != null) {
            pm.registerEvents(new FlyWorldGuardListener(), this);
        }
    }

    private void updateConfig() {
        try {
            File configFile = new File(this.getDataFolder(), "config.yml");
            ConfigUpdater.update(this, "config.yml", configFile);
        } catch (IOException ex) {
            Logger.severe("Failed to update the config.yml");
            ex.printStackTrace();
        }
        Config.reload();
    }

    public static StaffAreShort instance() {
        return instance;
    }

}
