package net.teamuni.votereward.mysql;

import static net.teamuni.votereward.VoteReward.getInstance;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.votereward.PlayerInfo;
import net.teamuni.votereward.VoteReward;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

public class MYSQL implements Database{
    private final HikariDataSource ds;

    private final String CREATE;
    private final String SELECT;
    private final String UPSERT;

    public MYSQL() {
        HikariConfig config = new HikariConfig();

        HashMap<String, String> d = getInstance().getConf().getMysqlData();

        String url = "jdbc:mysql://"
                + d.get("host") + ":"
                + d.get("port") + "/"
                + d.get("database") + "?characterEncoding=utf8&useSSL=false";

        config.setJdbcUrl(url);
        config.setUsername(d.get("username"));
        if (!d.get("password").isEmpty())
            config.setPassword(d.get("password"));

        ds = new HikariDataSource(config);

        CREATE = "CREATE TABLE IF NOT EXISTS `" + d.get("table") + "` ("
                + "`uuid` VARCHAR(36) PRIMARY KEY,"
                + "`recommend` TEXT,"
                + "`reward` TEXT"
                + ");";
        SELECT = "SELECT * FROM `" + d.get("table") + "`;";
        UPSERT = "INSERT INTO `" + d.get("table") + "` (`uuid`, `recommend`, `reward`) VALUES ('%1$s', '%2$s', '%3$s') "
                + "ON DUPLICATE KEY UPDATE `recommend` = '%2$s', `reward` = '%3$s';";

        load();
    }

    private void checkTable() {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE);

        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        HashMap<UUID, PlayerInfo> infos = new HashMap<>();

        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT)) {
            while (rs.next()) {
                if (!Pattern.matches(PlayerInfo.UUID_PATTERN, rs.getString("uuid"))) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    List<String> rec = Arrays.asList(rs.getString("recommend").split(", "));
                    List<String> reward = Arrays.asList(rs.getString("reward").split(", "));

                    PlayerInfo info = new PlayerInfo(uuid, rec, reward);

                    infos.put(uuid, info);

                } else if (rs.getString("uuid").equals("SAVEDDATE")) {
                    String savedDate = rs.getString("recommend");

                    if (!savedDate.equals(VoteReward.format.format(new Date()))) {
                        return;
                    }
                }
                PlayerInfo.setInfos(infos);

            }
        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(String.format(UPSERT, "SAVEDDATE", VoteReward.format.format(new Date()), VoteReward.format.format(new Date())));

            for (PlayerInfo info : PlayerInfo.getInfos().values()) {
                String rec = String.join(", ", info.getRec());
                String reward = String.join(", ", info.getReward());

                stmt.execute(String.format(UPSERT, info.getUUID().toString(), rec, reward));
            }

        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
