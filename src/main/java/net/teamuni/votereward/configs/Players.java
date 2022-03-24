package net.teamuni.votereward.configs;

import static net.teamuni.votereward.VoteReward.getInstance;
import net.teamuni.votereward.PlayerInfo;
import net.teamuni.votereward.VoteReward;
import net.teamuni.votereward.mysql.Database;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Players extends Frame implements Database {
    public Players() {
        super("players");
        load();
    }

    @Override
    public void load() {
        super.load();

        getData();
    }

    @Override
    public void save() {
        // 기존에 남아있던 데이터를 삭제
        configFile.delete();
        super.load();

        config.set("saved_date", VoteReward.format.format(new Date()));
        for (PlayerInfo info : PlayerInfo.getInfos().values()) {
            // 내용추가
            config.set("players."+info.getUUID()+".recommend", info.getRec());
            config.set("players."+info.getUUID()+".reward", info.getReward());
        }

        if (configFile != null && config != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                getInstance().getLogger().log(Level.SEVERE, "players.yml를 저장할 수 없습니다. " + configFile, e);
            }
        }
    }

    private void getData() {
        String savedDate = config.getString("saved_date");

        // 만약 오늘 데이터가 아니라면 데이터 초기화
        if (!savedDate.equals(VoteReward.format.format(new Date()))) {
            PlayerInfo.setInfos(new HashMap<>());
            return;
        }

        getInstance().getLogger().info("저장된 데이터가 오늘 정보이므로 데이터를 불러옵니다.");

        HashMap<UUID, PlayerInfo> infos = new HashMap<>();
        for (String s : config.getConfigurationSection("players").getKeys(false)) {
            PlayerInfo info = new PlayerInfo(
                    UUID.fromString(s),
                    config.getStringList("players."+s+".recommend"),
                    config.getStringList("players."+s+".reward")
            );

            infos.put(UUID.fromString(s), info);
        }

        PlayerInfo.setInfos(infos);
    }
}
