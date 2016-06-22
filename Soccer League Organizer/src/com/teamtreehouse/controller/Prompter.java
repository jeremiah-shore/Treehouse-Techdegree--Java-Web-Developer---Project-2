package com.teamtreehouse.controller;

import com.teamtreehouse.model.*;
import com.teamtreehouse.model.menus.CoachMenu;
import com.teamtreehouse.model.menus.MainMenu;
import com.teamtreehouse.model.menus.OrganizerMenu;
import com.teamtreehouse.model.menus.YesNoMenu;
import com.teamtreehouse.view.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Jeremiah on 6/16/2016.
 */
public class Prompter {
    private BufferedReader mReader;

    public Prompter() {
        mReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**** HELPER METHODS ****/

    private String readLine() {
        return readLine("");
    } //todo: evaluate if this can be removed since the getValidStringInput method was created, would need to add getValidIntInput or change isValidInt

    private String readLine(String message) {
        System.out.print(message + "\n\n> ");
        String input = "";
        try {
            input = mReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something has gone seriosuly wrong :(");
        }
        return input;

    }

    private boolean isValidInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            System.out.println("Your selection was not valid, a whole number is required. Please try again.\n");
            return false;
        }
    }

    private boolean isValidSelection(String input, int lowerLimit, int upperLimit) {
        if(!isValidInt(input)) return false;
        int selection = Integer.parseInt(input);
        if(selection < lowerLimit || selection > upperLimit) {
            System.out.printf("Your selection was not valid, a whole number between %d and %d is required. Please try again.\n", lowerLimit, upperLimit);
            return false;
        } else {
            return true;
        }
    }

    private String getValidStringInput() {
        return getValidStringInput("");
    }

    private String getValidStringInput(String message) {
        String input = readLine(message);
        if (input.isEmpty()) {
            System.out.println("Your input was not valid and cannot be empty. Please try again");
            return getValidStringInput(message);
        } else if (!input.matches("^[-a-zA-Z' ]*$")) {
            System.out.println("Your input was not valid and must contain only alphabetical characters. Please try again");
            return getValidStringInput(message);
        } else {
            return input;
        }
    }

    private void displayMenuOption(int index, String text) {
        System.out.printf("%d - %s\n", index, text.replace('_', ' '));
    }

    /**** INPUT PROMPTS ****/

    public void enterToContinue() {
        readLine("\n\nPress ENTER to continue");
    }

    public Team promptForTeamCreation() {
        String teamName = getValidStringInput("What is the team's name?");
        String coach = getValidStringInput("What is the coach's full name?");
        System.out.printf("Creating team %s...\n", teamName);
        return new Team(teamName, coach);
    }

    /**** SELECTION PROMPTS ****/

    public Team teamSelectionMenu(Set<Team> teams) {
        ArrayList<Team> teamList = new ArrayList<>();
        System.out.println("Which team?\n");
        for (Team team : teams) {
            teamList.add(team);
            System.out.printf("%d - %s\n",
                    teamList.indexOf(team) + 1,
                    team.getTeamName());
        }

        String selection = readLine();
        if(isValidSelection(selection, 1, teamList.size())) {
            return teamList.get(Integer.parseInt(selection) - 1);
        } else {
            return teamSelectionMenu(teams);
        }
    }

    public Player playerSelectionMenu(Set<Player> players) {
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player player: players) {
            playerList.add(player);
        }
        System.out.println("Which player?");
        Display.playersTable(players);

        String selection = readLine();
        if(isValidSelection(selection, 1, playerList.size())) {
            return playerList.get(Integer.parseInt(selection) - 1);
        } else {
            return playerSelectionMenu(players);
        }
    }

    /**** MENU PROMPTS ****/

    //todo: if possible, find a way to pass enumerators and receive them generically, and then refactor into a single menu with conditional logic if the enum is OrganizerMenu
    //todo: refactor the code for selection validation and its return

    public YesNoMenu yesNoMenu(String message) {
        System.out.println(message);
        displayMenuOption(1, "YES");
        displayMenuOption(2, "NO");
        String input = readLine();
        if (isValidSelection(input, 1, 2)) {
            switch (Integer.parseInt(input)) {
                case 1: return YesNoMenu.YES;
                case 2: return YesNoMenu.NO;
            }
        }
        return yesNoMenu(message);
    }

    public MainMenu mainMenu() {
        System.out.print("\n**** MAIN MENU ****\n");
        int optionCounter = 1;
        LinkedList<MainMenu> menuOptions = new LinkedList<>();
        for(MainMenu option : MainMenu.values()) {
            menuOptions.add(option);
            displayMenuOption(optionCounter, option.toString());
            optionCounter++;
        }

        String selection = readLine();
        if(isValidSelection(selection, 1, menuOptions.size())) {
            return menuOptions.get(Integer.parseInt(selection) - 1);
        } else {
            return mainMenu();
        }
    }

    public CoachMenu coachMenu () {
        System.out.print("\n**** COACH MENU ****\n");
        int optionCounter = 1;
        LinkedList<CoachMenu> menuOptions = new LinkedList<>();
        for(CoachMenu option : CoachMenu.values()) {
            menuOptions.add(option);
            displayMenuOption(optionCounter, option.toString());
            optionCounter++;
        }

        String selection = readLine();
        if(isValidSelection(selection, 1, menuOptions.size())) {
            return menuOptions.get(Integer.parseInt(selection) - 1);
        } else {
            return coachMenu();
        }
    }

    public OrganizerMenu organizerMenu(Set<Team> teams) {
        System.out.print("\n**** ORGANIZER MENU ****\n");
        int optionCounter = 1;
        LinkedList<OrganizerMenu> menuOptions = new LinkedList<>();
        for (OrganizerMenu option : OrganizerMenu.values()) {
            if (teams.isEmpty() && option != OrganizerMenu.CREATE_NEW_TEAM) {
                option = OrganizerMenu.EXIT_TO_MAIN_MENU;
                menuOptions.add(option);
                displayMenuOption(optionCounter, option.toString());
                break;
            } else {
                menuOptions.add(option);
                displayMenuOption(optionCounter, option.toString());
                optionCounter++;
            }
        }

        String selection = readLine();
        if(isValidSelection(selection, 1, menuOptions.size())) {
            return menuOptions.get(Integer.parseInt(selection) - 1);
        } else {
            return organizerMenu(teams);
        }
    }

}
