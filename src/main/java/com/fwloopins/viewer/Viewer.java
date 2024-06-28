package com.fwloopins.viewer;

import com.fwloopins.viewer.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public final class Viewer extends JavaPlugin {

    @Override
    public void onEnable() {
        registerListeners();
        registerTasks();
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
    }

    private void registerTasks() {
        Runnable particleTask = () -> {
            Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();
            for (Player player : onlinePlayers) {
                if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

                for (Player spectator : onlinePlayers.stream().filter(p -> p.getGameMode().equals(GameMode.SPECTATOR)).toList()) {
                    if (player.hasPermission("viewer.exempt")) continue; // Don't reveal actual player
                    player.spawnParticle(Particle.PORTAL, spectator.getLocation().add(0, 1.62, 0), 1);
                }
            }
        };

        Bukkit.getScheduler().runTaskTimer(this, particleTask, 0, 1);

        Runnable forceSpecatorTask = () -> {
            for (Player player : getServer().getOnlinePlayers()) {
                if (!player.getGameMode().equals(GameMode.SPECTATOR) && !player.hasPermission("viewer.exempt"))
                    player.setGameMode(GameMode.SPECTATOR);
            }
        };

        Bukkit.getScheduler().runTaskTimer(this, forceSpecatorTask, 0, 1);
    }
}
