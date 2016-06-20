/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corbi.robot.objects;

import com.corbi.robot.utilities.UtilityMethods;

/**
 * represents a game that is being monitored by discord
 *
 * @author PogChamp
 */
public class Game {

    private String title;
    private String tier;
    private long startTime;
    private long time_played;
    private int amount_played;

    public Game(String name, long overallTime, int timesPlayed) {
        this.title = name;
        this.time_played = overallTime;
        this.amount_played = timesPlayed;
        this.startTime = System.currentTimeMillis();
        this.tier = calculateTier();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTime_played() {
        return time_played;
    }

    public void setTime_played(long time_played) {
        this.time_played = time_played;
    }

    public int getAmount_played() {
        return amount_played;
    }

    public void setAmount_played(int amount_played) {
        this.amount_played = amount_played;
    }
    @Override
    public String toString()
{
    return "*" + title + "* - Zeit gespielt: " + UtilityMethods.formatTime(time_played) + "; Anzahl Aufrufe: " + String.valueOf(amount_played);
}
}
