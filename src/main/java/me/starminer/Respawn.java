package me.starminer;

import org.bukkit.plugin.java.JavaPlugin;

public final class Respawn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("YO!~");
        new Ressurection(this);
        new Delayer(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
