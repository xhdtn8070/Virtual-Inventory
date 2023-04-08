package org.tikim.minecraft.plugin.config;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class MessageConfig extends YamlConfig {

    @Getter
    private static final MessageConfig instance = new MessageConfig();

    private String init;

    private List<String> help;

    private List<String> info;

    private Inventory inventory;

    private Player player;

    private MessageConfig() {
        Common.log("MessageConfig 로딩중");
        loadConfiguration(NO_DEFAULT, "message.yml");
        init();
    }

    public void init() {

        this.init = getString("init", "&f[&dVI&f] &2Running &bVirtual Inventory {#version}.\n&9Use &a/vi help &9to view available commands.");

        this.help = getList("help", String.class);
        this.info = getList("info", String.class);

        SerializedMap inventoryMap = getMap("inventory");
        this.inventory = Inventory
                .builder()
                .start(inventoryMap.getString("start", "&aVirtual Inventroy start and load..."))
                .stop(inventoryMap.getString("stop", "&aVirtual Inventroy stop and save..."))
                .title(inventoryMap.getString("title", "#{playerName} 님의 인벤토리 #{inventoryName}"))
                .alreadyExists(inventoryMap.getString("already-exists", "&c이미 같은 이름의 가상 인벤토리가 존재합니다."))
                .noExists(inventoryMap.getString("no-exists", "&c플레이어의 인벤토리 #{inventoryName}가 존재하지 않습니다."))
                .createError(inventoryMap.getString("create-error", "&c창고를 더 이상 생성할 수 없습니다."))
                .remove(inventoryMap.getString("remove", "&a인벤토리가 삭제되었습니다.")).build();

        SerializedMap playerMap = getMap("player");
        this.player = Player
                .builder()
                .noExistsOnline(playerMap.getString("no-exists-online", "&c해당 플레이어가 온라인에 존재하지 않습니다."))
                .noExists(playerMap.getString("no-exists", "&c존재하지 않는 플레이어입니다."))
                .nonPlayer(playerMap.getString("non-player", "&c플레이어가 아닙니다. 플레이어로 입력해주시기 바랍니다."))
                .noAllowedPermission(playerMap.getString("no-allowed-permission", "&aYou do not have \"&e#{permission}&a\" permission to run that command"))
                .build();

    }

    @Getter
    @Builder
    public static class Inventory {

        private String start;
        private String stop;
        private String title;
        private String alreadyExists;
        private String noExists;
        private String createError;
        private String remove;

    }

    @Getter
    @Builder
    public static class Player {

        private String noExistsOnline;
        private String noExists;
        private String nonPlayer;
        private String noAllowedPermission;

    }
}
