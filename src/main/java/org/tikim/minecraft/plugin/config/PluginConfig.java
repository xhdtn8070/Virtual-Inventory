package org.tikim.minecraft.plugin.config;

import lombok.Getter;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class PluginConfig extends YamlConfig {
    @Getter
    private static final PluginConfig instance = new PluginConfig();

    private boolean autoSave;

    private int autoSaveTime;

    private boolean saveMessaging;

    private String saveMessage;

    private boolean loadMessaging;

    private String loadMessage;

    private int saveOncePerSeveralTimes;

    private int maxInventory;

    private PluginConfig() {
        Common.log("PluginConfig 로딩중");
        loadConfiguration(NO_DEFAULT, "config.yml");
        init();
    }

    public void init() {
        this.autoSave = getBoolean("auto-save", true);
        this.autoSaveTime = getInteger("auto-save-time", 10);

        this.saveMessaging = getBoolean("save-messaging", true);
        this.saveMessage = getString("save-message","&2[virtual Inventory] 가상 창고가 저장되었습니다.");

        this.loadMessaging = getBoolean("load-messaging", true);
        this.loadMessage = getString("load-message", "&6\t파일 명 : #{fileName}");

        this.saveOncePerSeveralTimes = getInteger("save-once-per-several-times", 3);
        this.maxInventory = getInteger("max-inventory", 10);
    }

}
