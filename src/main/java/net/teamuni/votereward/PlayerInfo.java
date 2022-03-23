package net.teamuni.votereward;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerInfo {
    public static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";
    private static HashMap<UUID, PlayerInfo> infos;

    private final UUID uuid;
    private List<String> rec;
    private List<String> reward;

    public PlayerInfo(UUID uuid, List<String> rec, List<String> reward) {
        this.uuid = uuid;
        this.rec = rec;
        this.reward = reward;
    }

    public static HashMap<UUID, PlayerInfo> getInfos() {
        return infos;
    }

    public static void setInfos(HashMap<UUID, PlayerInfo> infos) {
        PlayerInfo.infos = infos;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<String> getRec() {
        return rec;
    }

    public List<String> getReward() {
        return reward;
    }
}
