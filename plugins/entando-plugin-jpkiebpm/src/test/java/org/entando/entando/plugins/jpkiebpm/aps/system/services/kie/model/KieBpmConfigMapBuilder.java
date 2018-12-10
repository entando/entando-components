package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import java.util.HashMap;

public class KieBpmConfigMapBuilder {

    private final HashMap<String, KieBpmConfig> configMap;

    public KieBpmConfigMapBuilder() {
        this.configMap = new HashMap<>();
    }

    public KieBpmConfigMapBuilder addConfig(String name) {
        return addConfig(name, true);
    }

    public KieBpmConfigMapBuilder addConfig(String name, boolean active) {
        KieBpmConfig config = new KieBpmConfig();
        config.setName(name);
        config.setActive(active);
        configMap.put(name, config);

        return this;
    }

    public HashMap<String, KieBpmConfig> build() {
        return configMap;
    }
}
