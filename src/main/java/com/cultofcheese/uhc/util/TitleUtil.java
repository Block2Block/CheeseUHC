package com.cultofcheese.uhc.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * Util class for dealing with sending titles, as the 1.8 Spigot API does not come with a method built in.
 */
public class TitleUtil {

    public static void sendTitle(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime, ChatColor titleColor, ChatColor subtitleColor, boolean titleBold, boolean subTitleBold)
    {
        try
        {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\",\"color\":\"" + titleColor.name().toLowerCase() + "\",\"bold\":\"" + ((titleBold)?"true":"false") + "\"}");
            Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\",\"color\":\"" + subtitleColor.name().toLowerCase() + "\",\"bold\":\"" + ((subTitleBold)?"true":"false") + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            if (subtitle != null) {
                Object subtitlePacket = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatSubtitle, fadeInTime, showTime, fadeOutTime);
                sendPacket(player, subtitlePacket);
            }

            Object titlePacket = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeInTime, showTime, fadeOutTime);
            sendPacket(player, titlePacket);
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void sendHotBar(Player player, String message, ChatColor color, boolean bold) {
        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\",\"color\":\"" + color.name().toLowerCase() + "\",\"bold\":\"" + ((bold)?"true":"false") + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);

            Object packet = titleConstructor.newInstance(chatTitle, (byte) 2);
            sendPacket(player, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name)
    {
        try
        {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
