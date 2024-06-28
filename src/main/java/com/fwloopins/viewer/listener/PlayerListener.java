package com.fwloopins.viewer.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setToSpectatorIfApplicable(event.getPlayer());
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (!event.getNewGameMode().equals(GameMode.SPECTATOR) && !player.hasPermission("viewer.exempt"))
            event.setCancelled(true);
    }

    private void setToSpectatorIfApplicable(Player player) {
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

        if (!player.hasPermission("viewer.exempt"))
            player.setGameMode(GameMode.SPECTATOR);
    }
}
