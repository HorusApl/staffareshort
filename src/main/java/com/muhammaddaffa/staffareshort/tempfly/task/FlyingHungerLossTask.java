package com.muhammaddaffa.staffareshort.tempfly.task;

import com.muhammaddaffa.staffareshort.StaffAreShort;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyingHungerLossTask extends BukkitRunnable {

    private final Map<UUID, Location> lastLocation = new HashMap<>();
    private int counter = 0;

    @Override
    public void run() {
        // Check if counter match with the interval
        if (this.counter >= StaffAreShort.DEFAULT_CONFIG.getInt("hunger-loss.check-interval", 10)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("tempfly.bypass")) continue;
                if (!player.isFlying()) {
                    this.lastLocation.remove(player.getUniqueId());
                    continue;
                }
                // If the player is flying, record the last location
                Location location = player.getLocation();
                Location storedLocation = this.lastLocation.get(player.getUniqueId());
                if (storedLocation == null) {
                    this.lastLocation.put(player.getUniqueId(), location);
                    continue;
                }
                // Check if the location has the same world
                if (location.getWorld().equals(storedLocation.getWorld())) {
                    // Compare the distance
                    double distance = player.getLocation().distance(storedLocation);
                    if (distance >= StaffAreShort.DEFAULT_CONFIG.getInt("hunger-loss.block-distance")) {
                        // If code reaches this, that means the player is using flying really aggressively (e.g. travel long distance or to other worlds)
                        if (player.getSaturation() >= 0) {
                            // Reduce the saturation if player has any
                            player.setSaturation((float) (player.getSaturation() - StaffAreShort.DEFAULT_CONFIG.getDouble("hunger-loss.regular-loss.saturation")));
                        } else {
                            // Reduce the hunger bar instead
                            player.setFoodLevel(player.getFoodLevel() - StaffAreShort.DEFAULT_CONFIG.getInt("hunger-loss.regular-loss.hunger"));
                        }
                    } else {
                        // If code reaches this, that means the player just hovering
                        if (player.getSaturation() >= 0) {
                            // Reduce the saturation if player has any
                            player.setSaturation((float) (player.getSaturation() - StaffAreShort.DEFAULT_CONFIG.getDouble("hunger-loss.hover-loss.saturation")));
                        } else {
                            // Reduce the hunger bar instead
                            player.setFoodLevel(player.getFoodLevel() - StaffAreShort.DEFAULT_CONFIG.getInt("hunger-loss.hover-loss.hunger"));
                        }
                    }
                } else {
                    // If code reaches this, that means the player is using flying really aggressively (e.g. travel long distance or to other worlds)
                    if (player.getSaturation() >= 0) {
                        // Reduce the saturation if player has any
                        player.setSaturation((float) (player.getSaturation() - StaffAreShort.DEFAULT_CONFIG.getDouble("hunger-loss.regular-loss.saturation")));
                    } else {
                        // Reduce the hunger bar instead
                        player.setFoodLevel(player.getFoodLevel() - StaffAreShort.DEFAULT_CONFIG.getInt("hunger-loss.regular-loss.hunger"));
                    }
                }
                // Update the player's location
                this.lastLocation.put(player.getUniqueId(), location);
            }
            // Reset the counter
            this.counter = 0;
            return;
        }
        this.counter++;
    }

}
