package me.cooleg.parachute.Events;

import me.cooleg.parachute.Parachute;
import me.cooleg.parachute.Util.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerMove implements Listener {

    private final ItemStack oneTime;
    private final ArrayList<Item> groundItems = new ArrayList<>();
    public final ArrayList<UUID> dropFor = new ArrayList<>();
    private final HashMap<UUID, ArmorStand> armorStand = new HashMap<>();
    private final HashMap<UUID, Vector> pastPoint = new HashMap<>();
    private final HashMap<UUID, Vector> wind = new HashMap<>();
    private final ItemStack parachuteModel;
    private final ConfigWrapper config;
    private final Random rand = new Random();
    private final Parachute main;

    public PlayerMove(Parachute main, ConfigWrapper config) {
        this.main = main;
        this.config = config;
        oneTime = new ItemStack(config.getOnetimeMat());
        ItemMeta onetimeMeta = oneTime.getItemMeta();
        onetimeMeta.setCustomModelData(config.getOnetimeData());
        oneTime.setItemMeta(onetimeMeta);
        parachuteModel = new ItemStack(config.getModelMat());
        ItemMeta meta = parachuteModel.getItemMeta();
        meta.setCustomModelData(config.getModelData());
        parachuteModel.setItemMeta(meta);
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        if (!armorStand.containsKey(e.getPlayer().getUniqueId())) {return;}
        armorStand.get(e.getPlayer().getUniqueId()).setRotation(e.getPlayer().getLocation().getYaw(), 0);
        e.getPlayer().addPassenger(armorStand.get(e.getPlayer().getUniqueId()));
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
        if (blockBelow) {
            if (config.isDrop() && dropFor.contains(e.getPlayer().getUniqueId())) {
                Item item = e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), oneTime);
                item.setPickupDelay(100000);
                item.setUnlimitedLifetime(true);
                groundItems.add(item);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        item.remove();
                        groundItems.remove(item);
                    }
                }.runTaskLater(main, config.getDespawnTime()* 20L);
            }
            e.getPlayer().setFallDistance(0);
            stopParachute(e.getPlayer().getUniqueId());
        } else {
            final Player p = e.getPlayer();
            final Vector vector = p.getLocation().toVector().subtract(pastPoint.get(p.getUniqueId())).add(wind.get(p.getUniqueId())).multiply(.8 * config.getSpeed());
            p.setVelocity(vector.setY(-.2));
            pastPoint.put(p.getUniqueId(), p.getLocation().toVector());
        }
    }

    public void startParachute(Player p) {
        if (armorStand.containsKey(p.getUniqueId())) {return;}
        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setGravity(false);
        stand.getEquipment().setHelmet(parachuteModel);
        armorStand.put(p.getUniqueId(), stand);
        wind.put(p.getUniqueId(), config.getWind() > 0 ? new Vector(rand.nextDouble(-config.getWind(), config.getWind())/20, 0, rand.nextDouble(-config.getWind(), config.getWind())/20) : new Vector());
        pastPoint.put(p.getUniqueId(), p.getLocation().toVector());
        p.addPassenger(stand);
    }

    public void stopParachute(UUID id) {
        if (!armorStand.containsKey(id)) {return;}
        armorStand.get(id).remove();
        armorStand.remove(id);
        pastPoint.remove(id);
        wind.remove(id);
        dropFor.remove(id);
    }

    public void clearStands() {
        for (ArmorStand stand : armorStand.values()) {
            stand.remove();
        }
        armorStand.clear();
        pastPoint.clear();
        wind.clear();

        for (final Item item : groundItems) {
            item.remove();
        }
    }
}
