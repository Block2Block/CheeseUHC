package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.UHCTeam;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandTeam implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "create":
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    boolean allTeamsFull = true;
                                    for (UHCTeam team : CacheManager.getGame().getTeams()) {
                                        if (team.aliveMembers() < CacheManager.getGame().getConfig().getTeamSize()) {
                                            allTeamsFull = false;
                                            break;
                                        }
                                    }
                                    if (allTeamsFull || !CacheManager.getGame().getConfig().isForceFill()) {
                                        if (CacheManager.getGame().getPlayers().get(player).getTeam() == null) {
                                            UHCTeam team = new UHCTeam(CacheManager.newTeam());
                                            team.addMember(CacheManager.getGame().getPlayers().get(player));
                                            for (UHCPlayer otherPlayer : CacheManager.getGame().getPlayers().values()){
                                                otherPlayer.registerTeam(team);
                                            }
                                            CacheManager.getGame().addTeam(team);
                                            CacheManager.getGame().getPlayers().get(player).setTeam(team);
                                            player.sendMessage(UHC.c("Teams","You have created a new team: " + team.getFormattedName()));
                                        } else {
                                            player.sendMessage(UHC.c("Teams","You are already in a team. Please leave the team using &e/team leave&r in order to create a team."));
                                        }
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You cannot create a new team until all current teams are full."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to create a team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot create a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    case "invite":
                        if (args.length != 2) {
                            player.sendMessage(UHC.c("Teams","Invalid syntax! Correct syntax: &e/team invite [player]"));
                            return true;
                        }
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    if (CacheManager.getGame().getPlayers().get(player).getTeam() != null) {
                                        if (CacheManager.getGame().getPlayers().get(player).getTeam().aliveMembers() < CacheManager.getGame().getConfig().getTeamSize()) {
                                            Player invitee = Bukkit.getPlayer(args[1]);
                                            if (invitee != null && invitee.isOnline()) {
                                                if (!invitee.equals(player)) {
                                                    if (!CacheManager.getInvites().containsKey(invitee)) {
                                                        CacheManager.getInvites().put(invitee, CacheManager.getGame().getPlayers().get(player).getTeam());
                                                        invitee.sendMessage(UHC.c("Teams","You have been invited to a team by &r" + player.getName() + "&r. Use &e/team accept&r or &e/team decline&r to respond to the invite. The invite expires in 60 seconds"));
                                                        new BukkitRunnable(){
                                                            @Override
                                                            public void run() {
                                                                if (CacheManager.getInvites().containsKey(invitee)) {
                                                                    if (CacheManager.getInvites().get(invitee) == CacheManager.getGame().getPlayers().get(player).getTeam()) {
                                                                        CacheManager.getInvites().remove(invitee);
                                                                        player.sendMessage(UHC.c("Teams","Your invitation to " + invitee.getName() + "has expired."));
                                                                        invitee.sendMessage(UHC.c("Teams","The invitation has expired."));
                                                                    }
                                                                }
                                                            }
                                                        }.runTaskLaterAsynchronously(UHC.get(), 1200);
                                                        player.sendMessage(UHC.c("Teams","You have invited &e" + invitee.getName() + " &rto the team."));
                                                    } else {
                                                        player.sendMessage(UHC.c("Teams","That player already has an outstanding invite. You must wait for it to expire for them to receive another one."));
                                                    }
                                                } else {
                                                    player.sendMessage(UHC.c("Teams","You cannot invite yourself to your team!"));
                                                }
                                            } else {
                                                player.sendMessage(UHC.c("Teams","That person is not online or does not exist."));
                                            }
                                        } else {
                                            player.sendMessage(UHC.c("Teams","Your team is full. Kick a team member or create a new team in order to invite people."));
                                        }
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You must be in a team to invite someone. Use &e/team create&r to create a team."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to invite people to a team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot invite someone to a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    case "kick":
                        if (args.length != 2) {
                            player.sendMessage(UHC.c("Teams","Invalid syntax! Correct syntax: &e/team kick [player]"));
                            return true;
                        }
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    if (CacheManager.getGame().getPlayers().get(player).getTeam() != null) {
                                        Player kickee = Bukkit.getPlayer(args[1]);
                                        if (kickee != null && kickee.isOnline()) {
                                            if (!kickee.equals(player)) {
                                                UHCPlayer uhcKickee = CacheManager.getGame().getPlayers().get(kickee);
                                                if (uhcKickee.getTeam() == CacheManager.getGame().getPlayers().get(player).getTeam()) {
                                                    kickee.sendMessage(UHC.c("Teams","You were kicked from the team."));
                                                    player.sendMessage(UHC.c("Teams", "You kicked &e" + kickee.getName() + "&r from the team."));
                                                    for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                                        uhcPlayer.removeFromTeam(kickee, uhcKickee.getTeam());
                                                    }
                                                    uhcKickee.getTeam().removeMember(uhcKickee);
                                                    uhcKickee.leaveTeam();
                                                } else {
                                                    player.sendMessage(UHC.c("Teams","That user is not in your team."));
                                                }
                                            } else {
                                                player.sendMessage(UHC.c("Teams","You cannot kick yourself from your team!"));
                                            }
                                        } else {
                                            player.sendMessage(UHC.c("Teams","That person is not online or does not exist."));
                                        }
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You must be in a team to invite someone. Use &e/team create&r to create a team."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to kick people from the team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot kick someone from a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    case "leave":
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    if (CacheManager.getGame().getPlayers().get(player).getTeam() != null) {
                                        UHCPlayer member = CacheManager.getGame().getPlayers().get(player);
                                        for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                            uhcPlayer.removeFromTeam(player, member.getTeam());
                                        }
                                        UHCTeam team = member.getTeam();
                                        member.getTeam().removeMember(member);
                                        member.setTeam(null);

                                        if (team.aliveMembers() == 0) {
                                            CacheManager.getGame().removeTeam(team);
                                            for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                                uhcPlayer.unregisterTeam(team);
                                            }
                                        }
                                        player.sendMessage(UHC.c("Teams","You left your team."));
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You must be in a team to leave one. Use &e/team create&r to create a team."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to leave a team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot leave a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    case "accept":
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    if (CacheManager.getInvites().containsKey(player)) {
                                        if (CacheManager.getGame().getPlayers().get(player).getTeam() != null) {
                                            UHCPlayer member = CacheManager.getGame().getPlayers().get(player);
                                            UHCTeam newTeam = CacheManager.getInvites().get(player);
                                            for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                                uhcPlayer.removeFromTeam(player, member.getTeam());
                                                uhcPlayer.addToTeam(player, newTeam);
                                            }
                                            UHCTeam team = member.getTeam();
                                            member.getTeam().removeMember(member);
                                            newTeam.addMember(member);
                                            member.setTeam(newTeam);
                                            if (team.aliveMembers() == 0) {
                                                CacheManager.getGame().removeTeam(team);
                                                for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                                    uhcPlayer.unregisterTeam(team);
                                                }
                                            }
                                            player.sendMessage(UHC.c("Teams","You accepted the invitation."));

                                        } else {
                                            UHCPlayer member = CacheManager.getGame().getPlayers().get(player);
                                            UHCTeam newTeam = CacheManager.getInvites().get(player);
                                            for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
                                                uhcPlayer.addToTeam(player, newTeam);
                                            }
                                            member.setTeam(newTeam);
                                            newTeam.addMember(member);
                                            player.sendMessage(UHC.c("Teams","You accepted the invitation."));
                                        }
                                        CacheManager.getInvites().remove(player);
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You do not have an outstanding invite."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to leave a team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot accept an invitation to a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    case "decline":
                        if (CacheManager.getGame() != null) {
                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (!CacheManager.getGame().getConfig().isRandomTeams()) {
                                    if (CacheManager.getInvites().containsKey(player)) {
                                        player.sendMessage(UHC.c("Teams","You declined the invitation."));
                                        CacheManager.getInvites().remove(player);
                                    } else {
                                        player.sendMessage(UHC.c("Teams","You do not have an outstanding invite."));
                                    }
                                } else {
                                    player.sendMessage(UHC.c("Teams","Teams are random, so you are unable to leave a team."));
                                }
                            } else {
                                player.sendMessage(UHC.c("Teams","You cannot decline an invitation to a team when the event is in Solo mode."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Teams","You cannot use this command until the configuration has been parsed and the game chunks have been generated."));
                        }
                        break;
                    default:
                        if (CacheManager.getGame().getConfig().isTeamed()) {
                            player.sendMessage(UHC.c("Teams", "Available commands:\n" +
                                    "&e/team create&r - Create a new team\n" +
                                    "&e/team invite [player]&r - Invite a player to the team.\n" +
                                    "&e/team kick [player]&r - Kick a player from the team.\n" +
                                    "&e/team leave&r - Leave your current team.\n" +
                                    "&e/team accept&r - Accept an incoming invitation.\n" +
                                    "&e/team decline&r - Decline an incoming invitation."));
                        } else {
                            player.sendMessage(UHC.c("Teams","This command is disabled as the server is in Solo mode."));
                        }
                }
            } else {
                if (CacheManager.getGame().getConfig().isTeamed()) {
                    player.sendMessage(UHC.c("Teams", "Available commands:\n" +
                            "&e/team create&r - Create a new team\n" +
                            "&e/team invite [player]&r - Invite a player to the team.\n" +
                            "&e/team kick [player]&r - Kick a player from the team.\n" +
                            "&e/team leave&r - Leave your current team.\n" +
                            "&e/team accept&r - Accept an incoming invitation.\n" +
                            "&e/team decline&r - Decline an incoming invitation."));
                } else {
                    player.sendMessage(UHC.c("Teams","This command is disabled as the server is in Solo mode."));
                }
            }
        }
        return true;
    }
}
