package com.cultofcheese.uhc.entities;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerStats {

    private final Player player;
    private int damageTaken;
    private int damageDealt;
    private int kills;
    private double distanceTravelled = 0;
    private int blocksPlaced = 0;
    private int blocksBroken = 0;
    private boolean ended;

    public PlayerStats(Player player) {
        this.player = player;
        ended = false;

        //Resetting the players statistics so it can be accurately tracked from game start.
        for (Statistic stat : Statistic.values()) {
            switch (stat.getType()) {
                case UNTYPED:
                    player.setStatistic(stat, 0);
                    break;
                case ITEM:
                case BLOCK:
                    for (Material material : Material.values()) {
                        if (material.isBlock()||stat.getType() == Statistic.Type.ITEM) {
                            try {
                                player.setStatistic(stat, material, 0);
                            } catch (IllegalArgumentException e) {
                                continue;
                            }
                        }
                    }
                    break;
                case ENTITY:
                    for (EntityType type : EntityType.values()) {
                        try {
                            player.setStatistic(stat, type, 0);
                        } catch (IllegalArgumentException e) {
                            continue;
                        }
                    }
                    break;

            }
        }
    }

    public void onEnd() {
        //Getting info on statistics that are hard to track using spigot API events.
        damageTaken = player.getStatistic(Statistic.DAMAGE_TAKEN);
        damageDealt = player.getStatistic(Statistic.DAMAGE_DEALT);
        kills = player.getStatistic(Statistic.PLAYER_KILLS);
        long total = 0;
        for (Statistic stat : Statistic.values()) {
            if (stat.name().toLowerCase().contains("_one_cm")) {
                total += player.getStatistic(stat);
            }
        }
        distanceTravelled = total / 100d;
        ended = true;
    }

    public void blockPlace() {
        if (!ended) {
            blocksPlaced++;
        }
    }

    public void blockBreak() {
        if (!ended) {
            blocksBroken++;
        }
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public int getBlocksBroken() {
        return blocksBroken;
    }

    public double getDistanceTravelled() {
        if (ended) {
            return distanceTravelled;
        } else {
            long total = 0;
            for (Statistic stat : Statistic.values()) {
                if (stat.name().toLowerCase().contains("_one_cm")) {
                    total += player.getStatistic(stat);
                }
            }
            return total / 100d;
        }
    }

    public int getDamageDealt() {
        if (ended) {
            return damageDealt;
        } else {
            return player.getStatistic(Statistic.DAMAGE_DEALT);
        }
    }

    public int getDamageTaken() {
        if (ended) {
            return damageTaken;
        } else {
            return player.getStatistic(Statistic.DAMAGE_TAKEN);
        }
    }

    public int getKills() {
        if (ended) {
            return kills;
        } else {
            return player.getStatistic(Statistic.PLAYER_KILLS);
        }
    }
}
