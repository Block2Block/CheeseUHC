package com.cultofcheese.uhc.entities;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class UHCPlayer implements UHCParticipant {

    private final Player player;
    private PlayerStats stats;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private boolean dead;
    private UHCTeam team;

    public UHCPlayer(Player player) {
        dead = false;
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(player.getName(), "dummy");
        objective.setDisplayName(UHC.c(null, "&e&l-= &6&lUHC &e&l=-"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void onGameStart() {
        //Loading stats
        stats = new PlayerStats(player);
    }

    public PlayerStats getPlayerStats() {
        return stats;
    }

    public void onDeath() {
        dead = true;
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(Bukkit.getWorld("uhc"), 0, Bukkit.getWorld("uhc").getHighestBlockYAt(0,0) + 2, 0));
        CacheManager.getSpectators().add(player);
        stats.onEnd();
    }

    public void joinTeam(UHCTeam team) {
        this.team = team;
        Team sbTeam = scoreboard.getTeam(team.getId() + "");
        if (sbTeam == null) {
            sbTeam = scoreboard.registerNewTeam(team.getId() + "");
            sbTeam.setAllowFriendlyFire(false);
            sbTeam.setPrefix(team.getFormattedName());
        }
        sbTeam.addPlayer(this.player);
    }

    public void setTeam(UHCTeam team) {
        this.team = team;
    }

    public void leaveTeam() {
        Team sbTeam = scoreboard.getTeam(team.getId() + "");
        sbTeam.removePlayer(player);
        team = null;
    }

    public void addToTeam(Player player, UHCTeam team) {
        Team sbTeam = scoreboard.getTeam(team.getId() + "");
        sbTeam.addPlayer(player);
    }

    public void removeFromTeam(Player player, UHCTeam team) {
        Team sbTeam = scoreboard.getTeam(team.getId() + "");
        sbTeam.removePlayer(player);
    }

    public void registerTeam(UHCTeam team) {
        Team sbTeam = scoreboard.registerNewTeam(team.getId() + "");
        sbTeam.setAllowFriendlyFire(false);
        sbTeam.setPrefix(team.getFormattedName());
        for (UHCPlayer player : team.getMembers()) {
            sbTeam.addPlayer(player.getPlayer());
        }
    }

    public void unregisterTeam(UHCTeam team) {
        Team sbTeam = scoreboard.getTeam(team.getId() + "");
        sbTeam.unregister();
    }

    public boolean isDead() {
        return dead;
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public UHCTeam getTeam() {
        return team;
    }
}
