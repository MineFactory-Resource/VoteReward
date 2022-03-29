package net.teamuni.votereward;

import static net.teamuni.votereward.VoteReward.getInstance;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Vote {
    private static List<Vote> votes;

    private final String name;
    private final String site;
    private final String url;
    private final String message;
    private final String broadcast;
    private final List<String> commands;

    public Vote(String name, String site, String url, String message, String broadcast, List<String> commands) {
        this.name = name;
        this.site = site;
        this.url = url;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
        this.broadcast = ChatColor.translateAlternateColorCodes('&', broadcast);
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public static List<Vote> getVotes() {
        return votes;
    }

    public static void setVotes(List<Vote> votes) {
        Vote.votes = votes;
    }

    public String getSite() {
        return site;
    }

    public String getURL() {
        return url;
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

    public static void showVoteList(Player player) {
        PlayerInfo info = PlayerInfo.getInfos().get(player.getUniqueId());

        ComponentBuilder text = new ComponentBuilder(getInstance().getConf().getMessages().get("rec_list"));

        int i = 0;
        for (Vote vote : Vote.getVotes()) {
            if (info == null || !info.getRec().contains(vote.getSite())) {
                if (i > 0)
                    text.append(", ").underlined(false).bold(false).italic(false).strikethrough(false).color(net.md_5.bungee.api.ChatColor.WHITE);

                text.append("[" + vote.getName() + "]").event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + vote.getURL()))
                        .underlined(true).bold(false).italic(false).strikethrough(false).color(net.md_5.bungee.api.ChatColor.GRAY);
                i++;
            }
        }
        BaseComponent[] message = text.create();

        player.sendMessage(getInstance().getConf().getMessages().get("not_rec"));
        player.spigot().sendMessage(message);
    }
}
