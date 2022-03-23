package net.teamuni.votereward;

import net.teamuni.votereward.configs.Config;
import net.teamuni.votereward.configs.Players;
import net.teamuni.votereward.mysql.Database;
import net.teamuni.votereward.mysql.MYSQL;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;

public final class VoteReward extends JavaPlugin implements Listener {
    private static VoteReward instance;
    public static final SimpleDateFormat format = new SimpleDateFormat("yyMMdd");

    private Config config;
    private Database database;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config();

        if (config.isMysql()) {
            database = new MYSQL();
        } else {
            database = new Players();
        }
    }

    @Override
    public void onDisable() {
        database.save();
    }

    public static VoteReward getInstance() {
        return instance;
    }

    public Config getConf() {
        return config;
    }
}
