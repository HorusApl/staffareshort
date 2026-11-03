package com.muhammaddaffa.staffareshort.commands;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Placeholder;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlySpeedCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!(sender.hasPermission("tempfly.flyspeed"))) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.no-permission");
            return true;
        }
        if (args.length < 1) {
            Common.sendMessage(sender, "&cUsage: /flyspeed <speed>");
            return true;
        }
        if (!Common.isInt(args[0])) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.invalid-amount");
            return true;
        }
        int speed = Integer.parseInt(args[0]);
        if (speed > 10 || speed < 1) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.invalid-amount");
            return true;
        }
        // Set the fly speed of the player
        player.setFlySpeed((float) this.transform(speed));
        // Sends message
        StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.fly-speed", new Placeholder()
                .add("{speed}", speed));

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("tempfly.flyspeed")))
            return Collections.emptyList();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), new ArrayList<>());
        }
        return Collections.emptyList();
    }

    private double transform(int x) {
        // Constants for the transformation
        final double xMin = 1.0;
        final double xMax = 10.0;
        final double yMin = 0.1;
        final double yMax = 0.5;

        // Linear transformation formula
        return yMin + ((x - xMin) * (yMax - yMin) / (xMax - xMin));
    }

}
