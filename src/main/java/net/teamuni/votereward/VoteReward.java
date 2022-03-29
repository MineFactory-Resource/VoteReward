package net.teamuni.votereward;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.teamuni.votereward.configs.Config;
import net.teamuni.votereward.configs.Players;
import net.teamuni.votereward.mysql.Database;
import net.teamuni.votereward.mysql.MYSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public final class VoteReward extends JavaPlugin implements Listener {
    private static VoteReward instance;
    public static final SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

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

        getServer().getPluginManager().registerEvents(new VoteListeners(), this);
        this.getCommand("추천").setExecutor(new VoteCheckCommand());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String time = timeFormat.format(new Date());

            if (time.equals("00:00:00")) {
                // 00시에 투표한 플레이어 목록 초기화
                PlayerInfo.setInfos(new HashMap<>());
                this.getLogger().info("자정을 넘겨 투표 데이터가 초기화 되었습니다.");
            }
        }, 20, 20);
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
