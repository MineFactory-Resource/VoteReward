package net.teamuni.votereward.configs;


import net.teamuni.votereward.Vote;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config extends Frame{
    private int count;
    private String message;
    private String broadcast;
    private List<String> commands;
    private HashMap<String, String> messages;

    private boolean mysql;
    private HashMap<String, String> mysqlData;

    public Config() {
        super("config");
        load();
    }

    @Override
    protected void load() {
        super.load();

        getConfig();
        getMYSQL();
        getVotes();
    }

    private void getConfig() {
        count = config.getInt("config.recommend.count");
        message = ChatColor.translateAlternateColorCodes('&', config.getString("config.recommend.message"));
        broadcast = ChatColor.translateAlternateColorCodes('&', config.getString("config.recommend.broadcast"));
        commands = config.getStringList("config.recommend.commands");

        HashMap<String, String> m = new HashMap<>();
        m.put("not_rec", ChatColor.translateAlternateColorCodes('&', config.getString("config.messages.not_rec")));
        m.put("rec", ChatColor.translateAlternateColorCodes('&', config.getString("config.messages.rec")));
        m.put("rec_list", ChatColor.translateAlternateColorCodes('&', config.getString("config.messages.rec_list")));
        messages = m;
    }

    private void getMYSQL() {
        mysql = config.getBoolean("mysql.enabled");

        if (mysql) {
            HashMap<String, String> d = new HashMap<>();
            d.put("host", config.getString("mysql.host"));
            d.put("port", config.getString("mysql.port"));
            d.put("username", config.getString("mysql.username"));
            d.put("password", config.getString("mysql.password"));
            d.put("database", config.getString("mysql.database"));
            d.put("table", config.getString("mysql.table"));

            mysqlData = d;
        }
    }

    private void getVotes() {
        List<Vote> votes = new ArrayList<>();
        for (String s : config.getConfigurationSection("vote").getKeys(false)) {
            Vote v = new Vote(
                    s,
                    config.getString("vote."+s+".site"),
                    config.getString("vote."+s+".url"),
                    config.getString("vote."+s+".message"),
                    config.getString("vote."+s+".broadcast"),
                    config.getStringList("vote."+s+".commands")
            );
            votes.add(v);
        }

        Vote.setVotes(votes);
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isMysql() {
        return mysql;
    }

    public HashMap<String, String> getMysqlData() {
        return mysqlData;
    }
}
