package com.muhammaddaffa.staffareshort.users;

import com.muhammaddaffa.mdlib.utils.Config;
import com.muhammaddaffa.mdlib.utils.Logger;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private String name;

    // Data
    private int flightDuration;

    // Settings
    private boolean flying;
    private boolean allowFlight;
    private boolean slowEffect;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlightDuration() {
        if (this.flightDuration < 0) this.flightDuration = 0;
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
        if (this.flightDuration < 0) this.flightDuration = 0;
    }

    public void addFlightDuration(int amount) {
        this.flightDuration += amount;
        if (this.flightDuration < 0) this.flightDuration = 0;
    }

    public void removeFlightDuration(int amount) {
        this.flightDuration -= amount;
        if (this.flightDuration < 0) this.flightDuration = 0;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public boolean isAllowFlight() {
        return allowFlight;
    }

    public void setAllowFlight(boolean allowFlight) {
        this.allowFlight = allowFlight;
    }

    public boolean isSlowEffect() {
        return slowEffect;
    }

    public void setSlowEffect(boolean slowEffect) {
        this.slowEffect = slowEffect;
    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    private static final Map<UUID, User> userMap = new HashMap<>();

    @NotNull
    public static User find(OfflinePlayer player) {
        return find(player.getUniqueId());
    }

    @NotNull
    public static User find(UUID uuid) {
        return userMap.computeIfAbsent(uuid, k -> new User(uuid));
    }

    @Nullable
    public static User find(String name) {
        return all().stream()
                .filter(user -> user.getName() != null && user.getName().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }

    public static Collection<User> all() {
        return userMap.values();
    }

    public static void save() {
        Config data = StaffAreShort.DATA_CONFIG;
        FileConfiguration config = data.getConfig();
        // Save all users data
        Logger.info("Starting to save all users data...");
        for (User user : all()) {
            config.set("players." + user.getUniqueId().toString() + ".name", user.getName());
            config.set("players." + user.getUniqueId().toString() + ".flight-duration", user.getFlightDuration());
            config.set("players." + user.getUniqueId().toString() + ".flying", user.isFlying());
            config.set("players." + user.getUniqueId().toString() + ".allow-flight", user.isAllowFlight());
            config.set("players." + user.getUniqueId().toString() + ".slow-effect", user.isSlowEffect());
        }
        data.saveConfig();
        Logger.info("Successfully saved " + all().size() + " users data!");
    }

    public static void load() {
        FileConfiguration config = StaffAreShort.DATA_CONFIG.getConfig();
        // Load all users data
        Logger.info("Starting to load all users data...");
        if (!config.isConfigurationSection("players")) {
            Logger.info("There is no users data, skipping...");
            return;
        }
        for (String uuidString : config.getConfigurationSection("players").getKeys(false)) {
            // Create user data
            UUID uuid = UUID.fromString(uuidString);
            User user = new User(uuid);
            // Assign the data
            user.setName(config.getString("players." + uuidString + ".name"));
            user.setFlightDuration(config.getInt("players." + uuidString + ".flight-duration"));
            user.setFlying(config.getBoolean("players." + uuidString + ".flying"));
            user.setAllowFlight(config.getBoolean("players." + uuidString + ".allow-flight"));
            user.setSlowEffect(config.getBoolean("players." + uuidString + ".slow-effect"));
            // Store the data on the map
            userMap.put(uuid, user);
        }
        Logger.info("Successfully loaded " + userMap.size() + " users data!");
    }

}
