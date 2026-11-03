package com.muhammaddaffa.staffareshort.tempfly.task;

import com.muhammaddaffa.staffareshort.StaffAreShort;
import com.muhammaddaffa.staffareshort.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyTask extends BukkitRunnable {

    @Override
    public void run() {
        for (User user : User.all()) {
            if (user.getFlightDuration() > 0) {
                // If the flight duration more than 0, reduce it right away
                user.removeFlightDuration(1);
                // Check if the flight duration is zero
                if (user.getFlightDuration() <= 0) {
                    // Set flying state to false
                    user.setAllowFlight(false);
                    user.setFlying(false);
                    // Set slow effect to true
                    user.setSlowEffect(true);
                    // Try to get the Player object
                    Player player = Bukkit.getPlayer(user.getUniqueId());
                    if (player != null) {
                        if (player.hasPermission("tempfly.permanent")) {
                            if (player.isFlying()) {
                                user.setAllowFlight(true);
                                user.setFlying(true);
                            }
                        } else {
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            // Give player a slow effect potion
                            player.addPotionEffect(new PotionEffect(
                                    PotionEffectType.SLOW_FALLING,
                                    20 * 30,
                                    0,
                                    false, false, true
                            ));
                            // Sends message
                            StaffAreShort.DEFAULT_CONFIG.sendMessage(player, "messages.duration-ran-out");
                        }
                        // Set slow effect to false
                        user.setSlowEffect(false);
                    }
                }
            }
        }
    }

}
