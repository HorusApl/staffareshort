package com.muhammaddaffa.staffareshort.papi;

import com.muhammaddaffa.mdlib.utils.TimeUtils;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import com.muhammaddaffa.staffareshort.users.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "aglerr";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "staffareshort";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if (params.equalsIgnoreCase("tempfly_duration")) {
            if (player.hasPermission("tempfly.permanent")) {
                return StaffAreShort.DEFAULT_CONFIG.getString("tempfly-permanent-placeholder", "∞");
            }
            return TimeUtils.format(User.find(player).getFlightDuration());
        }

        return null; // Placeholder is unknown by the Expansion
    }

}
