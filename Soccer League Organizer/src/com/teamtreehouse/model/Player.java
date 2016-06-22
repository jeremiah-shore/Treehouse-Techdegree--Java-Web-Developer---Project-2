package com.teamtreehouse.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

public class Player implements Comparable<Player>,  Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private int heightInInches;
    private boolean previousExperience;

    public Player(String firstName, String lastName, int heightInInches, boolean previousExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.heightInInches = heightInInches;
        this.previousExperience = previousExperience;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getHeightInInches() {
        return heightInInches;
    }

    public boolean isPreviousExperience() {
        return previousExperience;
    }

    public static float getAverageHeight(Set<Player> players) {
        if (players.isEmpty()) {
            return 0f;
        } else {
            float totalHeight = 0f;
            for (Player player : players) {
                totalHeight += player.getHeightInInches();
            }
            return totalHeight / players.size();
        }
    }

    @Override
    public int compareTo(Player other) {
        //compare by last name then first name
        if (lastName.compareTo(other.lastName) != 0) {
            return lastName.compareTo(other.lastName);
        } else {
            return firstName.compareTo(other.firstName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (heightInInches != player.heightInInches) return false;
        if (previousExperience != player.previousExperience) return false;
        if (!firstName.equals(player.firstName)) return false;
        return lastName.equals(player.lastName);

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + heightInInches;
        result = 31 * result + (previousExperience ? 1 : 0);
        return result;
    }

    public static final Comparator<Player> BY_HEIGHT =
            new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getHeightInInches() - o2.getHeightInInches();
                }
            };
}
