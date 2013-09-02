package com.google.ratel.core;

import java.util.HashMap;
import java.util.Map;

public enum Mode {

    PRODUCTION("production", 0),
    PROFILE("profile", 1),
    DEVELOPMENT("development", 2),
    DEBUG("debug", 3),
    TRACE("trace", 4);

    private String name;

    private int level;

    private final static Map<String, Mode> lookup = new HashMap<String, Mode>();

    static {
        for (Mode mode : values()) {
            lookup.put(mode.getName(), mode);
        }
    }

    Mode(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public static Mode getMode(String key) {
        if (key == null) {
            return null;
        }

        return lookup.get(key.toLowerCase());
    }

    public boolean isProductionModes() {
        return this.level <= PROFILE.level;
    }

    public boolean isDevelopmentModes() {
        return this.level >= DEVELOPMENT.level;
    }

    @Deprecated
    public boolean isAtleast(Mode mode) {
        return this.level >= mode.level;
    }
    
    public int getLevel() {
        return level;
    }
}
