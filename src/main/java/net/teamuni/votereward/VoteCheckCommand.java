package net.teamuni.votereward;

import static net.teamuni.votereward.VoteReward.getInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteCheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            PlayerInfo info = PlayerInfo.getInfos().get(player.getUniqueId());

            if (info == null) {
                Vote.showVoteList(player);
            } else {
                if (info.getRec().size() == Vote.getVotes().size()) {
                    player.sendMessage(getInstance().getConf().getMessages().get("rec"));
                } else {
                    Vote.showVoteList(player);
                }
            }
        }
        return true;
    }
}
