package com.teamtreehouse.view;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.*;

/**
 * Created by Jeremiah on 6/22/2016.
 */
public class Display {

    /**** SALUTATION MESSAGES ****/

    public static void greeting() {
        System.out.println("Soccer League Organizer\n");
        System.out.println("created by Jeremiah Shore\nbuilt for the teamtreehouse.com Java Web Developer Techdegree program.\n");
    }

    public static void goodbye() { System.out.println("Thank you for using the Soccer League Organizer. Goodbye!\n");}

    /**** ERROR MESSAGES ****/

    public static void noPlayersFoundError() {
        System.out.println("No players were found.");
    }

    public static void noTeamsAvailableError() {
        System.out.println("No teams have been created yet. Please have your organizer create one before continuing.");
    }

    public static void tooManyTeamsError(Set<Team> teams) {
        System.out.printf("\nThere are currently %d teams, which can accommodate %d max players each.\n", teams.size(), Team.MAX_PLAYERS);
        System.out.printf("This means that %d players can currently be accommodated, and there are %d total registered players.\n", teams.size()*Team.MAX_PLAYERS, Players.load().length);
        System.out.printf("More teams cannot be created until at least %d more players are registered.\n", (teams.size()*Team.MAX_PLAYERS + 1) - Players.load().length);
    }

    public static void tooManyPlayersOnTeamError() {
        System.out.printf("There are already %d players on that team. Please select another team or remove players first.\n", Team.MAX_PLAYERS);
    }

    /**** CONFIRMATION MESSAGES ****/

    public static void playerAddConfirmation(Player player, Team team) {
        playerInfoInline(player);
        System.out.printf(", has been added to the team %s and removed from the list of available players.\n", team.getTeamName());
    }

    public static void playerRemoveConfirmation(Player player, Team team) {
        playerInfoInline(player);
        System.out.printf(", has been removed from the team %s and added to the list of available players.\n", team.getTeamName());
    }

    /**** PLAYER ORGANIZER DISPLAYS ****/

    public static void playerInfoInline(Player player) {
        System.out.printf("%s, %s: height of %d inches, experienced(%s)",
                player.getLastName(),
                player.getFirstName(),
                player.getHeightInInches(),
                player.isPreviousExperience());
    }

    public static void playersTable(Set<Player> players) { //displays alphabetically because that is the natural ordering imposed by Comparable and its compareTo method
        if (players.isEmpty()) {
            noPlayersFoundError();
            return;
        }
        System.out.printf("## - LAST NAME       FIRST NAME      HEIGHT (in.)    EXPERIENCED?\n"); //col width of 15 chars + a single space
        int counter = 1;
        for (Player player : players) {
            System.out.printf("%2d - %-15s %-15s %-15s %-15s\n",
                    counter++,
                    player.getLastName(),
                    player.getFirstName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience());
        }
    }

    public static void playersByHeight(Set<Player> players) {
        System.out.println("PLAYER LISTING - ORDERED BY ASCENDING HEIGHT");
        ArrayList<Player> playersByHeight= new ArrayList<>(players); //converted into an array list so that the Comparator BY_HEIGHT can be used
        Collections.sort(playersByHeight, Player.BY_HEIGHT);
        players = new LinkedHashSet<Player>(playersByHeight);
        playersTable(players); //requires a set and a LinkedHashSet will maintain ordering
    }

    /**** TEAM ORGANIZER DISPLAYS ****/

    public static void basicTeamInfo(Team team) {
        System.out.printf("\nTEAM NAME: %s\nCOACHED BY: %s\n", team.getTeamName(), team.getCoach());
    }

    public static void teamRoster(Team team) {
        basicTeamInfo(team);
        playersTable(team.getPlayers());
    }

    /**** DATA SUMMARY DISPLAYS ****/

    public static void heightCountSummary(Set<Player> players) {
        //create and populate a TreeMap with height count data
        TreeMap<Integer, Integer> heightCounts = new TreeMap<>();
        for(Player player : players) heightCounts.put(player.getHeightInInches(), 0); //define heights that are present and initialize counts
        for(Player player : players) {
            for(Map.Entry<Integer, Integer> entry : heightCounts.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                if (player.getHeightInInches() == key) {
                    heightCounts.put(key, value + 1);
                }
            }
        }

        //display the height count data
        System.out.println("\nHEIGHT(in.) COUNT");
        for(Map.Entry<Integer, Integer> entry : heightCounts.entrySet()) {
            System.out.printf("%-12d%-5d\n", entry.getKey(), entry.getValue());
        }
    }

    public static void heightDistributionSummary(Set<Player> players) {
        //obtain the min and max height of all registered players
        Player[] allRegisteredPlayers = Players.load();
        ArrayList<Player> playerList = new ArrayList<Player>(Arrays.asList(allRegisteredPlayers));
        Collections.sort(playerList, Player.BY_HEIGHT);
        int minHeight = playerList.get(0).getHeightInInches();
        int maxHeight = playerList.get(playerList.size() - 1).getHeightInInches();

        //create groupings dynamically based on min and max heights of all registered players
        int heightGroupingIncrement = 3;
        LinkedList<String> heightGroupLabels = new LinkedList<>();
        for (int i = minHeight;  i <= maxHeight; i += heightGroupingIncrement + 1) { //+1 so the next grouping does not start with the same number as the last of the previous
            heightGroupLabels.add(i + " to " + (i + heightGroupingIncrement)); //creates entries such as "36 to 39" or "40 to 43"
        }

        //count the grouping of each player
        heightGroupingIncrement++; //this is necessary so that the following math is INCLUSIVE of beginning and end numbers in each range
        int[] groupCounts = new int[heightGroupLabels.size()];
        for (Player player : players) {
            int diff = player.getHeightInInches() - minHeight;
            int groupPlacement = (int) ((diff / heightGroupingIncrement ) * 100) / 100; //divides the difference between actual height and min height to determine the grouping, truncates so there is no rounding
            groupCounts[groupPlacement] += 1;
        }

        //display the results of the groupings
        System.out.println("\nHEIGHT SUMMARY");
        for (String groupLabel : heightGroupLabels) {
            System.out.printf("%-20s: %d\n",
                    groupLabel,
                    groupCounts[heightGroupLabels.indexOf(groupLabel)]);
        }
        System.out.printf("average height: %.0f inches\n", Player.getAverageHeight(players));
    }

    public static void teamExperienceSummary(Team team) {
        System.out.println("\nEXPERIENCE SUMMARY");
        System.out.printf("experienced players:   %-2d\n", team.getExperiencedPlayerCount());
        System.out.printf("inexperienced players: %-2d\n", team.getInexperiencedPlayerCount());
        int xpLevel = team.getExperiencePercentage();
        System.out.printf("team experience level: %d%%\n", xpLevel);
    }

    public static void leagueSynopsis(int registeredPlayers, int unassignedPlayers, Set<Team> teamSet) {
        ArrayList<Team> teams = new ArrayList<>(teamSet);
        System.out.printf("\nThere are %d registered players, %d of which are currently not assigned to a team.\n", registeredPlayers, unassignedPlayers);
        if (teams.isEmpty()) {
            System.out.printf("There are currently no teams.\n");
        } else if (teams.size() == 1) {
            System.out.printf("There is currently 1 team. The only team is %s.\n", teams.get(0).getTeamName());
        }
        else {
            System.out.printf("There are currently %d teams. ", teams.size());
            System.out.printf("The existing teams are: ");
            for (Team team : teams) {
                System.out.print(team.getTeamName());
                if (teams.indexOf(team) != teams.size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.println("\n");
                }
            }
        }
    }

}
