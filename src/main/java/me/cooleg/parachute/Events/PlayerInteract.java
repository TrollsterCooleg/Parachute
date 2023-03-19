package me.cooleg.parachute.Events;

import me.cooleg.parachute.Parachute;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteract implements Listener {

    private final PlayerMove move;
    private final NamespacedKey key;

    public PlayerInteract(PlayerMove move, Parachute main) {
        this.move = move;
        key = new NamespacedKey(main, "item");
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {return;}
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {return;}
        if (!e.getItem()
                .getItemMeta()
                .getPersistentDataContainer()
                .has(key, PersistentDataType.STRING)) {return;}

        boolean blockBelow = false;

        for (int x = -1; x <= 1; x++) {
            for (int y = -2; y <= 0; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (e.getPlayer().getLocation().add(x, y ,z).getBlock().getType() == Material.AIR) {continue;}
                    blockBelow = true;
                    break;
                }
                if (blockBelow) {break;}
            }
            if (blockBelow) {break;}
        }
        if (blockBelow) {return;}

        if (e.getItem()
                .getItemMeta()
                .getPersistentDataContainer()
                .get(key, PersistentDataType.STRING)
                .equals("onetime")) {e.getItem().setAmount(e.getItem().getAmount()-1); move.dropFor.add(e.getPlayer().getUniqueId());}

        move.startParachute(e.getPlayer());
    }
}
