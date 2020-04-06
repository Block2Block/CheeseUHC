package com.cultofcheese.uhc.entities;

import com.cultofcheese.uhc.UHC;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a team participating in the game.
 */
public class UHCTeam implements UHCParticipant {

    private static final String[] codes = new String[]{"1","2","3","4","5","6","7","8","9","a","b","c","d","e"};

    private List<UHCPlayer> members;
    private int teamKills;
    private final int id;

    public UHCTeam(int id) {
        members = new ArrayList<>();
        this.id = id;
    }

    public String getFormattedName() {
        return UHC.c(null, "&" + codes[(id % 14)] + "&lTeam " + (id+1) + " ");
    }

    public void addMember(UHCPlayer player) {
        for (UHCPlayer member : members) {
            member.getPlayer().sendMessage(UHC.c("Team Manager","&e" + player.getPlayer().getName() + "&r joined the team!"));
        }
        members.add(player);
    }

    public void removeMember(UHCPlayer player) {
        for (UHCPlayer member : members) {
            member.getPlayer().sendMessage(UHC.c("Team Manager","&e" + player.getPlayer().getName() + "&r left the team!"));
        }
        members.remove(player);
    }

    public int aliveMembers() {
        return (int) members.stream().filter(player -> !player.isDead()).count();
    }

    public int getId() {
        return id;
    }

    public List<UHCPlayer> getMembers() {
        return new ArrayList<>(members);
    }

    public void teleport(Location location) {
        for (UHCPlayer member : members) {
            member.getPlayer().teleport(location);
        }
    }

    /**
     * Increments the team kills statistic.
     */
    public void teamKill() {
        teamKills++;
    }

    public int getTeamKills() {
        return teamKills;
    }
}
