package net.teamuni.votereward;

import org.bukkit.ChatColor;

import java.util.List;

public class Vote {
    private static List<Vote> votes;

    private final String site;
    private final String message;
    private final String broadcast;
    private final List<String> commands;

    public Vote(String site, String message, String broadcast, List<String> commands) {
        this.site = site;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
        this.broadcast = ChatColor.translateAlternateColorCodes('&', broadcast);
        this.commands = commands;
    }

    public static void setVotes(List<Vote> votes) {
        Vote.votes = votes;
    }

    public String getSite() {
        return site;
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
}
