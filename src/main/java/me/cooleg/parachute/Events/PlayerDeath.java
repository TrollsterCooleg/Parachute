package me.cooleg.parachute.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private final PlayerMove move;

    public PlayerDeath(PlayerMove move) {
        this.move = move;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        move.stopParachute(e.getEntity().getUniqueId());
    }

}
