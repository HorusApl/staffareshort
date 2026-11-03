package com.muhammaddaffa.staffareshort.users.listeners;

import com.muhammaddaffa.staffareshort.users.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class UserListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(AsyncPlayerPreLoginEvent event) {
        User.find(event.getUniqueId()).setName(event.getName());
    }

}
