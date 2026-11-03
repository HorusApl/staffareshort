package com.muhammaddaffa.staffareshort.commands;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Config;
import com.muhammaddaffa.mdlib.utils.Placeholder;
import com.muhammaddaffa.staffareshort.StaffAreShort;
import com.muhammaddaffa.staffareshort.users.User;
import com.muhammaddaffa.staffareshort.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempFlyCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("tempfly.admin"))) return true;
        // Check if player put no args
        if (args.length == 0) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.help");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give"     -> this.give(sender, args);
            case "take"     -> this.take(sender, args);
            case "set"      -> this.set(sender, args);
            case "reload"   -> this.reload(sender);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("tempfly.admin"))) return Collections.emptyList();
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("give");
            suggestions.add("take");
            suggestions.add("set");
            suggestions.add("reload");

            return StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }
        if (args.length == 2 && List.of("give", "take", "set").contains(args[0])) {
            return null;
        }
        if (args.length == 3 && List.of("give", "take", "set").contains(args[0])) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("30s");
            suggestions.add("5m");
            suggestions.add("10m");
            suggestions.add("30m");
            suggestions.add("1h");
            suggestions.add("1h30m");

            return StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }
        return Collections.emptyList();
    }

    private void give(CommandSender sender, String[] args) {
        if (args.length < 3) {
            Common.sendMessage(sender, "&cUsage: /tempfly give <player> <duration>");
            return;
        }
        User user = User.find(args[1]);
        if (user == null) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.user-not-found");
            return;
        }
        // Parse the duration
        String durationArg = args[2];
        int duration = Utils.parseDurationInSeconds(durationArg);
        // Add the flight duration
        user.addFlightDuration(duration);
        // Sends message
        StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.give-duration", new Placeholder()
                .add("{player}", user.getName())
                .add("{duration}", durationArg));
    }

    private void take(CommandSender sender, String[] args) {
        if (args.length < 3) {
            Common.sendMessage(sender, "&cUsage: /tempfly take <player> <duration>");
            return;
        }
        User user = User.find(args[1]);
        if (user == null) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.user-not-found");
            return;
        }
        // Parse the duration
        String durationArg = args[2];
        int duration = Utils.parseDurationInSeconds(durationArg);
        // Add the flight duration
        user.removeFlightDuration(duration);
        // Sends message
        StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.take-duration", new Placeholder()
                .add("{player}", user.getName())
                .add("{duration}", durationArg));
    }

    private void set(CommandSender sender, String[] args) {
        if (args.length < 3) {
            Common.sendMessage(sender, "&cUsage: /tempfly set <player> <duration>");
            return;
        }
        User user = User.find(args[1]);
        if (user == null) {
            StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.user-not-found");
            return;
        }
        // Parse the duration
        String durationArg = args[2];
        int duration = Utils.parseDurationInSeconds(durationArg);
        // Add the flight duration
        user.setFlightDuration(duration);
        // Sends message
        StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.set-duration", new Placeholder()
                .add("{player}", user.getName())
                .add("{duration}", durationArg));
    }

    private void reload(CommandSender sender) {
        // Reload the config
        Config.reload();
        // Sends message
        StaffAreShort.DEFAULT_CONFIG.sendMessage(sender, "messages.reload");
    }

}
