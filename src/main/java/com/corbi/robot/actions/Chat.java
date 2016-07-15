/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corbi.robot.actions;

import com.corbi.robot.main.Main;
import com.corbi.robot.objects.Game;
import com.corbi.robot.objects.User;
import com.corbi.robot.utilities.UtilityMethods;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * This method's only reason for existence is to make Daniel's Life just a tiny bit harder.
     * @param channel @link #sendMessage(IChannel, String) channel
     * @throws HTTP429Exception
     * @throws DiscordException
     * @throws MissingPermissionsException 
     */
    public static void insultDaniel(IChannel channel) throws HTTP429Exception, DiscordException, MissingPermissionsException {
        String[] insults = {"Daniel ist sehr speziell in der Wahl der Musiklautstärke. Tätsächlich ist für ihn alles unangenehm laut.",
            "Daniel kauft keine neuen Spiele, da er zu sehr an seiner einzigen Liebe hängt, der Kunst des Feedens."};
        Random randInt = new Random(System.currentTimeMillis());

        int index = randInt.nextInt(insults.length);
        sendMessage(channel, insults[index]);
    }

    /**
     * Writes a message, that is specifically aimed at improving anbodys game be
     * it in league or real life
     *
     * @param channel @link #sendMessage(IChannel, String) channel
     * @throws HTTP429Exception
     * @throws DiscordException
     * @throws MissingPermissionsException
     */
    public static void tellBinsenweisheit(IChannel channel) throws HTTP429Exception, DiscordException, MissingPermissionsException {
        String[] binsenweisheiten = {"Ein Kampf, in dem die zahlenmäßige Unterlegenheit zwei oder mehr beträgt, ist kein Kampf, sondern eine Dummheit.",
            "Die ultimative Fähigkeit kann auch zum Fliehen eines aussichtlosen Kampfes genutzt werden.", "Sollte eine Person angerufen werden, so bite diese darum, dir ihren Gesprächspartner mitzuteilen. "
            + "Dies hat den Vorteil eines angenehmen Themas sollte es zu sogenanntem \"Smalltalk\" kommen."};
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
        String id = user.getID();
        if (args.length > 2 || args.length == 0) {
            return false;
        } else {
            switch (args[0]) {
                case "me":
                    showStatsMe(channel, id, guildID);
                    break;
                case "all":
                    showStatsAll(channel, guildID);
                    break;
                case "name":
                    if (args.length == 2) {//showStatsByName needs a parameter
                        showStatsByName(channel, args[1], guildID);
                        break;
                    } else {
                        return false;
                    }
                case "ranking": 
                    if(args.length == 2 && UtilityMethods.isInteger(args[1])) {//showStatsRanking needs second parameter
                    showStatsRanking(channel,(int) Integer.parseInt(args[1]), guildID);//selects the top n users by uptime
                    break;
                    }
                    else{
                        return false;
                    }
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * show stats about the user, specifically his overall uptime
     *
     * @param channel @link #showStats(IChannel, IUser, String, String[])
     * channel
     * @param id of the user that sent the request
     * @param guildID @link #showStats(IChannel, IUser, String, String[])
     * guildID
     */
    private static void showStatsMe(IChannel channel, String id, String guildID) {
        User user = null;
        List<Game> games = null;
        try {
            user = Main.userService.getUser(id, guildID);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "User could not be retrieved.", ex);
        }
        String personalStats = user.toString() + System.lineSeparator() + getGamesMessage(id, guildID);
        sendMessage(channel, personalStats);
    }

    /**
     * returns Users which equal the specified name
     *
     * @param channel @link #showStats(IChannel, IUser, String, String[]) channel
     * @param name name, of which the stats are to be shown
     * @param guildID @link #showStats(IChannel, IUser, String, String[])
     * guildID
     */
    private static void showStatsByName(IChannel channel, String name, String guildID) {
        List<User> users = new ArrayList<>();
        try {
            users = Main.userService.getUserByName(name, guildID);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "User could not be retrieved by name.", ex);
        }
        if (!(users.isEmpty())) {
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append(user.toString()).append(System.lineSeparator()).append(getGamesMessage(user.getId(), user.getGuildID()));
            }
            sendMessage(channel, sb.toString());
        } else {
            sendMessage(channel, "Die Person mit dem Namen *" + name + "* existiert genau so wenig wie deine Freundin.");
        }
    }
    
    /**
     * shows the stats of the top n users
     * @param channel @link #showStats(IChannel, IUser, String, String[]) channel
     * @param limit {@link com.corbi.robot.actions.DBServices.UserService#getRankingByUptime(String, int} limit
     * @param guildID @link #showStats(IChannel, IUser, String, String[]) guildID
     */
    private static void showStatsRanking(IChannel channel, int limit, String guildID)
    {
        List<User> users = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            users = Main.userService.getRankingByUptime(guildID, limit);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "Users could not be retrieved by ranking.", ex);
        }
        for(int i = 0; i < users.size(); i++)
        {
            sb.append("**").append(String.valueOf(i+1)).append("**: ").append(users.get(i).toString()).append(System.lineSeparator());// bold rank number: user.toString() + linebreak
        }
        sendMessage(channel, sb.toString());
    }

    /**
     * shows the combined stats of all users
     * @param channel @link #showStats(IChannel, IUser, String, String[])
     * channel
     * @param guildID @link #showStats(IChannel, IUser, String, String[])
     * guildID
     */
    private static void showStatsAll(IChannel channel, String guildID) {
        long uptime = 0;
        List<Game> games = null;
        try {
            uptime = Main.userService.getUptimeAll(guildID);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "could not retrieve data for all users", ex);
        }
        try {
            games = Main.gameService.getGamesAll(guildID);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "could not retrieve game data for all users.", ex);
        }
        StringBuilder sb = new StringBuilder();
        if (games != null) {
            sb.append("Eure verschwendete Zeit teilt ihr anscheinend wie folgt auf:");
        }
        for (int i = 0; i < games.size(); i++) {
            sb.append(System.lineSeparator()).append(String.valueOf(i)).append(". ").append(games.get(i).toString());
        }
        String statsAll = "Ihr habt insgesamt *" + UtilityMethods.formatTime(uptime) + "* auf diesem Server verschwendet.";
        sendMessage(channel, statsAll);
    }

    /**
     * Sends a message that informs the user of the use of a wrong command
     *
     * @param wrongCommand a non-supported command
     * @param channel @link #sendMessage(IChannel, String) channel
     */
    public static void showUnsupportedFormatMessage(String wrongCommand, IChannel channel) {
        String errorInfo = "The HydraBot does not support the command *" + wrongCommand + "*.";

        sendMessage(channel, errorInfo);
    }

    /**
     * sends a message that informs the user of the non-supported arguments for
     * a supported command
     *
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
            Logger.getGlobal().log(Level.SEVERE, "message could not be sent.", ex);
        }
    }
    /**
     * A utility method that will retrieve and format the games for a given user
     * @param id unique id for an user
     * @param guildID Server from which the request was sent
     * @return a formatted String that contains all information for games from a single user
     */
    private static String getGamesMessage(String id, String guildID) {
        List<Game> games = null;
        try {
            games = Main.gameService.getGames(id, guildID);
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "games could not be retrieved.", ex);
        }
        StringBuilder sb = new StringBuilder();
        if (games != null) {
            sb.append("Die verschwendete Zeit wird wie folgt aufgeteilt:");
            for (int i = 0; i < games.size(); i++) {
                sb.append(System.lineSeparator()).append(String.valueOf(i)).append(". ").append(games.get(i).toString());
            }
        }
        return sb.toString();
    }
}
