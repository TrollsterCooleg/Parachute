package me.cooleg.parachute;

import com.google.common.base.Optional;
import me.cooleg.parachute.Commands.GetOnetimeParachute;
import me.cooleg.parachute.Commands.GetParachute;
import me.cooleg.parachute.Events.PlayerDeath;
import me.cooleg.parachute.Events.PlayerInteract;
import me.cooleg.parachute.Events.PlayerLogoff;
import me.cooleg.parachute.Events.PlayerMove;
import me.cooleg.parachute.Util.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Parachute extends JavaPlugin {

    private PlayerMove move;
    private ConfigWrapper config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = new ConfigWrapper(getConfig());
        Bukkit.getPluginManager().registerEvents(move = new PlayerMove(this, config), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(move, this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLogoff(move), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(move), this);
        getCommand("getparachute").setExecutor(new GetParachute(this, config));
        getCommand("getonetimeparachute").setExecutor(new GetOnetimeParachute(this, config));
    }

    @Override
    public void onDisable() {
        move.clearStands();
    }
}
