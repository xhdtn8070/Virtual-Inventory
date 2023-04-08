package org.tikim.minecraft.plugin.config;

import java.util.List;
import lombok.Getter;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class TabConfig extends YamlConfig {
    @Getter
    private static final TabConfig instance = new TabConfig();

    private List<String> tabs;

    private TabConfig() {
        Common.log("tabConfig 로딩중");
        loadConfiguration(NO_DEFAULT, "tab.yml");
        init();
    }

    public void init() {
        this.tabs = getList("init", String.class);
    }

}
