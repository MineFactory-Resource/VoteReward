package net.teamuni.votereward;

import static net.teamuni.votereward.VoteReward.getInstance;

import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class VoteListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 추천 기록이 남아있을 경우
        if (PlayerInfo.getInfos().size() != 0 && PlayerInfo.getInfos().containsKey(event.getPlayer().getUniqueId())) {
            Player player = event.getPlayer();
            PlayerInfo info = PlayerInfo.getInfos().get(player.getUniqueId());

            // 서버 투표를 모두 참여하지 않았을 경우
            if (info.getRec().size() < Vote.getVotes().size()) {
                Vote.showVoteList(player);
            }

            countReward(player);

            // 보상받지 못한 아이템 보상 지급
            for (String rec : info.getRec()) {
                if (!info.getReward().contains(rec)) {
                    for (Vote vote : Vote.getVotes()) {
                        if (vote.getSite().equalsIgnoreCase(rec)) {
                            for (String cmd : vote.getCommands()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                            }

                            if (!vote.getMessage().isEmpty())
                                player.sendMessage(vote.getMessage());

                            info.addReward(vote.getSite());
                        }
                    }
                }
            }

        // 추천 기록이 없을 경우
        } else {
            Player player = event.getPlayer();

            Vote.showVoteList(player);
        }
    }

    @EventHandler
    public void onVotifier(VotifierEvent event) {
        for (Vote vote : Vote.getVotes()) {
            if (vote.getSite().equalsIgnoreCase(event.getVote().getServiceName())) {
                OfflinePlayer offlinePlayer = null;
                for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                    if (op.getName().equalsIgnoreCase(event.getVote().getUsername())) {
                        offlinePlayer = op;
                    }
                }

                // 서버에 접속한 적이 없는 닉네임일 경우 무시
                if (offlinePlayer == null) return;

                // PlayerInfo 확인용 없을 경우 생성 후 rec 추가
                if (PlayerInfo.getInfos().containsKey(offlinePlayer.getUniqueId())) {
                    PlayerInfo info = PlayerInfo.getInfos().get(offlinePlayer.getUniqueId());

                    if (info.getRec().contains(vote.getSite()) && info.getReward().contains(vote.getSite()))
                        return;

                    info.addRec(vote.getSite());
                } else {
                    PlayerInfo info = new PlayerInfo(offlinePlayer.getUniqueId(), new ArrayList<>(), new ArrayList<>());

                    info.addRec(vote.getSite());
                    PlayerInfo.addInfo(info);
                }

                if (!vote.getBroadcast().isEmpty())
                    Bukkit.broadcastMessage(vote.getBroadcast().replace("%player%", event.getVote().getUsername()));



                // 플레이어가 접속 중일 경우 보상 바로 지급
                if (offlinePlayer.isOnline()) {
                    Player player = offlinePlayer.getPlayer();

                    countReward(player);

                    PlayerInfo info = PlayerInfo.getInfos().get(player.getUniqueId());

                    for (String cmd : vote.getCommands()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                    }

                    if (!vote.getMessage().isEmpty())
                        player.sendMessage(vote.getMessage());

                    info.addReward(vote.getSite());

                }
            }
        }
    }

    // n회 추천하였을 경우 지급되는 보상 지급
    private void countReward(Player player) {
        PlayerInfo info = PlayerInfo.getInfos().get(player.getUniqueId());

        // 투표를 count만큼 했을 경우 추가 보상 지급
        if ((getInstance().getConf().getCount() <= info.getRec().size()) && (getInstance().getConf().getCount() > info.getReward().size())) {
            if (!getInstance().getConf().getBroadcast().isEmpty())
                Bukkit.broadcastMessage(getInstance().getConf().getBroadcast().replace("%player%", player.getName()));

            if (!getInstance().getConf().getMessage().isEmpty())
                player.sendMessage(getInstance().getConf().getMessage());

            for (String cmd : getInstance().getConf().getCommands())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }
    }
}
