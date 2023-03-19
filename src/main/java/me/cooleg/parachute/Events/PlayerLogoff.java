package me.cooleg.parachute.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogoff implements Listener {

    private final PlayerMove move;

    public PlayerLogoff(PlayerMove move) {
        this.move = move;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        move.stopParachute(e.getPlayer().getUniqueId());
    }

}
