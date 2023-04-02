package me.cooleg.parachute.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigWrapper {
    private final boolean drop;
    private final boolean stack;
    private final boolean onetimeStack;
    private final int despawnTime;
    private final double speed;
    private final double wind;
    private final int modelData;
    private final int itemData;
    private final int onetimeData;
    private final Material onetimeMat;
    private final Material parachuteMat;
    private final Material modelMat;
    private final String regularName;
    private final String regularDescription;
    private final String onetimeName;
    private final String onetimeDescription;

    public ConfigWrapper(FileConfiguration config) {
        drop = config.getBoolean("drop-enabled");
        stack = config.getBoolean("parachutes-stack");
        onetimeStack = config.getBoolean("onetime-stack");

        despawnTime = config.getInt("drop-time");

        speed = config.getDouble("speed-multiplier");
        wind = config.getDouble("wind-speed");

        modelData = config.getInt("parachute-model");
        itemData = config.getInt("parachute-item");
        onetimeData = config.getInt("onetime-item");

        String regName = config.getString("regular-name");
        regularName = ChatColor.translateAlternateColorCodes('&', regName == null ? "Parachute" : regName);
        String regDesc = config.getString("regular-description");
        regularDescription = ChatColor.translateAlternateColorCodes('&', regDesc == null ? "" : regDesc);
        String oneName = config.getString("onetime-name");
        onetimeName = ChatColor.translateAlternateColorCodes('&', oneName == null ? "One Time Parachute" : oneName);
        String oneDesc = config.getString("onetime-description");
        onetimeDescription = ChatColor.translateAlternateColorCodes('&', oneDesc == null ? "" : oneDesc);

        Material onetime;
        try {
            onetime = Material.valueOf(config.getString("onetime-material").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {onetime = Material.PAPER;}
        onetimeMat = onetime;
        Material parachute;
        try {
            parachute = Material.valueOf(config.getString("regular-material").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {parachute = Material.PAPER;}
        parachuteMat = parachute;
        Material model;
        try {
            model = Material.valueOf(config.getString("parachute-material").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {model = Material.PAPER;}
        modelMat = model;
    }

    public boolean isDrop() {
        return drop;
    }
    public int getDespawnTime() {
        return despawnTime;
    }
    public double getSpeed() {return speed;}
    public double getWind() {return wind;}
    public int getModelData() {return modelData;}
    public int getItemData() {return itemData;}
    public int getOnetimeData() {return onetimeData;}
    public Material getOnetimeMat() {return onetimeMat;}
    public Material getParachuteMat() {return parachuteMat;}
    public Material getModelMat() {return modelMat;}
    public String getRegularName() {return regularName;}
    public String getRegularDescription() {return regularDescription;}
    public String getOnetimeName() {return onetimeName;}
    public String getOnetimeDescription() {return onetimeDescription;}
    public boolean isStack() {return stack;}
    public boolean isOnetimeStack() {return onetimeStack;}

}
