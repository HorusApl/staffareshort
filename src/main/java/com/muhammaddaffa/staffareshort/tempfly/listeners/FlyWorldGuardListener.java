package com.muhammaddaffa.staffareshort.tempfly.listeners;

import com.muhammaddaffa.mdlib.worldguards.events.RegionEnterEvent;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FlyWorldGuardListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onRegionEnter(RegionEnterEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (StaffAreShort.DEFAULT_CONFIG.getStringList("blacklisted-regions").contains(event.getRegion().getId()) && !player.hasPermission("tempfly.bypass")) {
            if (player.isFlying() || player.getAllowFlight()) {
                StaffAreShort.DEFAULT_CONFIG.sendMessage(player, "messages.enter-blacklisted-area");
            }
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

}
