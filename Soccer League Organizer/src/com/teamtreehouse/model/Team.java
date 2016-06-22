package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Jeremiah on 6/16/2016.
 */
public class Team implements Comparable<Team>{
    public static final int MAX_PLAYERS = 11;

    private String mTeamName;
    private String mCoach;
    private TreeSet<Player> mPlayers;

    public Team(String teamName, String coach) {
        mTeamName = teamName;
        mCoach = coach;
        mPlayers = new TreeSet<>();
    }

    public String getTeamName() {
        return mTeamName;
    }

    public String getCoach() {
        return mCoach;
    }

    public TreeSet<Player> getPlayers() {
        return mPlayers;
    }

    public void addPlayer(Player player) {
        mPlayers.add(player);
    }

    public int getExperiencedPlayerCount() {
        int xpPlayerCount = 0;
        for (Player player : mPlayers) {
            if (player.isPreviousExperience()) {
                xpPlayerCount += 1;
            }
        }
        return xpPlayerCount;
    }

    public int getInexperiencedPlayerCount() {
        return mPlayers.size() - getExperiencedPlayerCount();
    }

    public int getExperiencePercentage() {
        if (mPlayers.isEmpty()) {
            return 0;
        } else{
            int xpCount = getExperiencedPlayerCount() * 100;
            double xpLevel = xpCount / mPlayers.size();
            return (int) xpLevel;
        }
    }

    @Override
    public int compareTo(Team other) {
        return mTeamName.compareTo(other.mTeamName);
    }
}
