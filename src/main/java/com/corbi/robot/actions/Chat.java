/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corbi.robot.actions;

import com.corbi.robot.main.Main;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

/**
 *
 * @author Stiglmair This class is designed to handle anything the bot wants to
 * write in a TextChannel. Any write to chat command should be done with this
 * class.
 */
public class Chat {

    /**
     *
     * @param channel @link #sendMessage(IChannel, String) channel
     * @throws HTTP429Exception
     * @throws DiscordException
     * @throws MissingPermissionsException This method's only reason for
     * existence is to make Daniel's Life just a tiny bit harder.
     */
    public static void insultDaniel(IChannel channel) throws HTTP429Exception, DiscordException, MissingPermissionsException {
        String[] insults = {"Daniel ist sehr speziell in der Wahl der Musiklautstärke. Tätsächlich ist für ihn alles unangenehm laut.",
            "Daniel kauft keine neuen Spiele, da er zu sehr an seiner einzigen Liebe hängt, der Kunst des Feedens."};
        Random randInt = new Random(System.currentTimeMillis());

        int index = randInt.nextInt(insults.length);
        sendMessage(channel, insults[index]);
    }

    /**
     * Writes a message, that is specifically aimed at improving noahs game be
     * it in league or real life
     *
     * @param channel @link #sendMessage(IChannel, String) channel
     * @throws HTTP429Exception
     * @throws DiscordException
     * @throws MissingPermissionsException
     */
    public static void tellBinsenweisheit(IChannel channel) throws HTTP429Exception, DiscordException, MissingPermissionsException {
        String[] binsenweisheiten = {"Kämpfe nie 1 gegen 3.", "Die ultimative Fähigkeit kann auch zum Fliehen eines aussichtlosen Kampfes genutzt werden."};
        Random randInt = new Random(System.currentTimeMillis());

        int index = randInt.nextInt(binsenweisheiten.length);
        String binsenweisheit = "Binsenweisheit " + String.valueOf(index + 1) + ": " + binsenweisheiten[index];
        sendMessage(channel, binsenweisheit);
    }

    /**
     * tells a random memorable quote
     *
     * @param channel @link #sendMessage(IChannel, String) channel
     */
    public static void tellQuotes(IChannel channel) {

    }

    /**
     * shows stats such as overall uptime on servers, time spent playing etc.
     *
     * @param channel @link #sendMessage(IChannel, String) channel 
     * @param args the arguments received with the command
     * @param user User who sent the command
     * @param guildID the id of the server
     * @return true if format of input was correct, false otherwise
     */
    public static boolean showStats(IChannel channel, IUser user, String guildID, String args[]) {
        if (args.length > 1 || args.length == 0) {
            return false;
        } else {
            switch (args[0]) {
                case "me":
                    showStatsUser(channel, user, guildID);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
    /**
     * show stats about the user, specifically his overall uptime
     * @param channel @link #showStats(IChannel, IUser, String, String[]) channel
     * @param user @link #showStats(IChannel, IUser, String, String[]) user
     * @param guildID @link #showStats(IChannel, IUser, String, String[]) guildID
     */
    private static void showStatsUser(IChannel channel, IUser user, String guildID) {
        long uptime = 0;
        try {
            uptime = Main.dbService.getUser(user.getID(), guildID).getUptime();
        } catch (SQLException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, "user could not be retrieved.", ex);
        }
        long second = (uptime / 1000) % 60;
        long minute = (uptime / (1000 * 60)) % 60;
        long hour = (uptime / (1000 * 60 * 60)) % 24;
        long day = (uptime / (1000 * 60 * 60 * 24));
        String time = String.format("%d Tage, %02d Stunden, %02d Minuten und %02d Sekunden", day, hour, minute, second);
        String personalStats = "Du hast insgesamt *" + time + "* auf diesem Server verbracht.";
        sendMessage(channel, personalStats);
    }
    /**
     * Sends a message that informs the user of the use of a wrong command
     * @param wrongCommand a non-supported command
     * @param channel @link #sendMessage(IChannel, String) channel
     */
    public static void showUnsupportedFormatMessage(String wrongCommand, IChannel channel) {
        String errorInfo = "The HydraBot does not support the command *" + wrongCommand + "*.";

        sendMessage(channel, errorInfo);
    }
    /**
     * sends a message that informs the user of the non-supported arguments for a supported command
     * @param command command that does not support the used arguments
     * @param wrongArgs non-supported arguments
     * @param channel @link #sendMessage(IChannel, String) channel
     */
    public static void showUnsupportedFormatMessage(String command, String[] wrongArgs, IChannel channel) {

        String errorInfo = "The HydraBot does not support the arguments *" + Arrays.toString(wrongArgs)
                + "* for the command *" + command + "*.";
        sendMessage(channel, errorInfo);

    }

    /**
     * The bot will send the specified message in the specified channel
     *
     * @param channel channel where the message is to be sent
     * @param content the content of the intended message
     *
     */
    public static void sendMessage(IChannel channel, String content) {
        try {
            new MessageBuilder(Main.client).withChannel(channel).withContent(content).build();
        } catch (HTTP429Exception | DiscordException | MissingPermissionsException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, "message could not be sent.", ex);
        }
    }
}
