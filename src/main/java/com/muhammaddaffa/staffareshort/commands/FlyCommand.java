package com.muhammaddaffa.staffareshort.commands;

import com.muhammaddaffa.staffareshort.StaffAreShort;
import com.muhammaddaffa.staffareshort.users.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!(sender.hasPermission("tempfly.fly"))) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.no-permission");
            return true;
        }
        // Check if player is allowed to fly
        User user = User.find(player);
        // Check if player has flight duration
        if (user.getFlightDuration() > 0 || sender.hasPermission("tempfly.permanent")) {
            if (player.getAllowFlight()) {
                // Make player do not fly
                player.setAllowFlight(false);
                player.setFlying(false);
                // Sends message
                StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.flight-off");
            } else {
                // Make player fly
                player.setAllowFlight(true);
                player.setFlying(true);
                // Sends message
                StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.flight-on");
            }
        } else {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.unable-fly");
        }
        return true;
    }

}
