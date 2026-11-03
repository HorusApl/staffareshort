package com.muhammaddaffa.staffareshort.tempfly.listeners;

import com.muhammaddaffa.mdlib.utils.Executor;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import com.muhammaddaffa.staffareshort.users.User;
import com.muhammaddaffa.staffareshort.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.find(player);
        // Set the status back
        player.setAllowFlight(user.isAllowFlight());
        player.setFlying(user.isFlying());
        // If we should give slow effect potion to player
        if (user.isSlowEffect()) {
            user.setSlowEffect(false);
            // Give player a slow effect potion
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW_FALLING,
                    20 * 30,
                    0,
                    false, false, true
            ));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;
        if (!from.getWorld().equals(to.getWorld())) {
            boolean allowFlight = player.getAllowFlight();
            boolean flying = player.isFlying();

            Executor.syncLater(2L, () -> {
                if (Utils.isInsideBlacklistedArea(player) && !player.hasPermission("tempfly.bypass")) {
                    if (flying || allowFlight) {
                        StaffAreShort.DEFAULT_CONFIG.sendMessage(player, "messages.enter-blacklisted-area");
                    }
                    player.setFlying(false);
                    player.setAllowFlight(false);
                } else {
                    if (allowFlight) player.setAllowFlight(true);
                    if (flying) player.setFlying(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = User.find(player);
        // Save the flight status
        user.setFlying(player.isFlying());
        user.setAllowFlight(player.getAllowFlight());
    }

}
