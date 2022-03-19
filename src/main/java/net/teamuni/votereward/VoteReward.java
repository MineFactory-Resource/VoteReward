package net.teamuni.votereward;

import net.teamuni.votereward.configs.Config;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoteReward extends JavaPlugin implements Listener {
    private static VoteReward instance;

    private Config config;
    @Override
    public void onEnable() {
        instance = this;

        config = new Config();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static VoteReward getInstance() {
        return instance;
    }
}
