/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stiglmair.hydra.objects;

import com.stiglmair.hydra.main.Main;
import com.stiglmair.hydra.utilities.UtilityMethods;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Presences;

/**
 * identifies a user that ever was or is currently on the server
 *
 * @author PogChamp
 */
public class User {

    private String name;
    private long uptime;
    private String userID;
    private String tier;
    private final long loginTime;
    private long lastUpdate;
    private Game game;

    public User(long uptime, String userID, String name) {
        this.uptime = uptime;
        this.userID = userID;
        this.loginTime = System.currentTimeMillis();
        this.name = name;
        lastUpdate = loginTime;
        calculateTier();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLoginTime() {
        return loginTime;
    }

    /**
     * updates the uptime of the user for this object, then writes it on the
     * database
     */
    private void updateUptime() {
        long time_passed = System.currentTimeMillis() - lastUpdate; //time since last update
        try {
            uptime = Main.userService.getUser(userID).getUptime() + time_passed; // value from db + currentTime - time of last update (=loginTime if there was no update)
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, "Could not retrieve User.", ex);
        }
        lastUpdate = System.currentTimeMillis();
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private void calculateTier() {
        String[] tiers = {"McShitsen", "DansGame", "Dödelbär", "MrPoppyButthole", "Fan Grill", "BabyRageBoy", "Beach Boy", "Kazoo Kid", "Average Joe",
            "Quality Shit Poster", "Big City Kid", "Top Notch Memer", "Navy Seal", "Undercover agent working for bagool", "Bobby Ryan", "Korean", "PogChamp", "Person mit zuviel Zeit und zu wenig Privatleben", "Genji OTP"};
        long uptime_hours = uptime / 1000 / 60 / 60; //millseconds / 1000 = seconds / 60 = minutes / 60 = hours
        long linear_scaling_factor = 365 * 6 / tiers.length; // 365 * 6 hours is the estimate of the uptime of a power user in a year (6 hours a day online)
        tier = tiers[(int) Math.min(uptime_hours / linear_scaling_factor, tiers.length - 1)];//selects an according tier; if uptime_hours > 365 * 6 the highest availabe tier will be selected --> no ArrayOutOfBounds
    }

    /**
     * updates the object then writes the data to the server
     */
    public void save() {
        updateUptime();
        try {
            Main.userService.updateUser(userID, name, uptime);
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, "could not update user.", ex);
        }
        calculateTier();//update tier after uptime was adjusted
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date loginDate = new Date(loginTime);
        return UtilityMethods.highlightBold(name) + ", Uptime: " + UtilityMethods.highlightItalic(UtilityMethods.formatTime(uptime)) + ", Tier: " + UtilityMethods.highlightBold(tier);
    }

    /**
     * Updates the uptime of all users within the list then writes it to the
     * database
     *
     * @param users list of users to be updated
     */
    /**
     * updates all user objects within the list then writes the data to the
     * database
     *
     * @param users list of users to be saved
     */
    public static void saveUsers(List<User> users) {

        for (User user : users) {
            user.save();
        }
    }

    /**
     * retrieves all currently online users
     *
     * @param users a list of users
     * @return a list with online, non-bot users
     */
    public static List<IUser> getOnlineUsers(List<IUser> users) {
        List<IUser> onlineUsers = new ArrayList<>();
        for (IUser user : users) {
            if (!(user.isBot())
                    && (user.getPresence().equals(Presences.ONLINE)
                    || user.getPresence().equals(Presences.STREAMING))) {//true if user is online and not a bot, false otherwise
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
}
