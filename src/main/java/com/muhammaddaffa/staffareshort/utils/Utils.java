package com.muhammaddaffa.staffareshort.utils;

import com.muhammaddaffa.mdlib.worldguards.SimpleWorldGuardAPI;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern durationPattern = Pattern.compile("(\\d+)([smhd])");

    public static int parseDurationInSeconds(String duration) {
        int totalSeconds = 0;

        Matcher matcher = durationPattern.matcher(duration);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);

            switch (unit) {
                case 's' -> totalSeconds += value;
                case 'm' -> totalSeconds += value * 60;
                case 'h' -> totalSeconds += value * 3600;
                case 'd' -> totalSeconds += value * 86400;
                default -> throw new IllegalArgumentException("Invalid duration unit: " + unit);
            }
        }

        return totalSeconds;
    }

    public static boolean isInsideBlacklistedArea(Player player) {
        return isInsideBlacklistedArea(player.getLocation());
    }

    public static boolean isInsideBlacklistedArea(Location location) {
        if (StaffAreShort.DEFAULT_CONFIG.getStringList("blacklisted-worlds").contains(location.getWorld().getName())) {
            return true;
        }
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            for (String regionId : StaffAreShort.DEFAULT_CONFIG.getStringList("blacklisted-regions")) {
                if (SimpleWorldGuardAPI.isInRegion(location, regionId)) return true;
            }
        }
        return false;
    }


}
