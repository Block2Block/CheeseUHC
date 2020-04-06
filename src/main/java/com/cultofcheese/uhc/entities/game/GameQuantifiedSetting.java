package com.cultofcheese.uhc.entities.game;

import com.cultofcheese.uhc.UHC;
import org.bukkit.ChatColor;

public enum GameQuantifiedSetting {

    BORDER_TIME("&6&lBorder Time", new Object[]{1, 5, 10}),
    BORDER_SLOW_BLOCK_SEC("&6&lBorder Slow Blocks/Sec", new Object[]{0.05f, 0.1f, 0.25f}),
    BORDER_FAST_BLOCK_SEC("&6&lBorder Fast Blocks/Sec", new Object[]{0.05f, 0.1f, 0.25f}),
    BORDER_SPEED_UP_DISTANCE("&6&lBorder Speed Up Distance", new Object[]{1, 10, 50}),
    BORDER_SIZE("&6&lBorder Size", new Object[]{1, 10, 50}),
    PVP_TIMER("&6&lPvP Timer", new Object[]{1, 5, 10}),
    DAMAGE_TIMER("&6&lDamage Timer", new Object[]{1, 5, 10}),
    DAMAGE_AMOUNT("&6&lDamage Amount", new Object[]{0.25f, 0.5f, 1f}),
    TEAM_SIZE("&6&lMax Team Size", new Object[]{1, 2, 5}),
    NETHER_DAMAGE_TIMER("&6&lNether Damage Timer", new Object[]{1, 5, 10}),
    NETHER_DAMAGE_AMOUNT("Nether Damage Amount", new Object[]{0.25f, 0.5f, 1f});

    String name;
    Object[] values;

    GameQuantifiedSetting(String name, Object[] values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public Object[] getValues() {
        return values;
    }

    public static GameQuantifiedSetting getByName(String name) {
        for (GameQuantifiedSetting setting : GameQuantifiedSetting.values()) {
            if (ChatColor.stripColor(UHC.c(null, setting.getName())).equals(name)) {
                return setting;
            }
        }

        return null;
    }
}
