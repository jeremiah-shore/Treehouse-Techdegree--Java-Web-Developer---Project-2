import com.teamtreehouse.controller.Prompter;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.view.Display;

import java.util.TreeSet;

public class LeagueManager {

    public static void main(String[] args) {
        //initial setup
        Player[] registeredPlayers = Players.load();
        TreeSet<Player> availablePlayers = new TreeSet<>();
        for (Player player : registeredPlayers) {
            availablePlayers.add(player);
        }
        TreeSet<Team> teams = new TreeSet<>();
        Prompter prompter = new Prompter();

        //display initial greeting
        Display.greeting();
        prompter.enterToContinue();

        //main menu loop
        boolean isStillManaging = true;
        //display main menu
        do switch (prompter.mainMenu()) {
            case COACH_MENU:
                switch (prompter.coachMenu()) {
                    case PRINT_TEAM_ROSTER:
                        if (!teams.isEmpty()) { //todo: create a NoTeamsFound exception that can be used elsewhere?
                            Team selectedTeam = prompter.teamSelectionMenu(teams);
                            Display.teamRoster(selectedTeam);
                        } else {
                            Display.noTeamsAvailableError();
                        }
                        break;
                    case EXIT_TO_MAIN_MENU:
                        break; //EXIT by breaking
                    default:
                        break;
                }
                break;
            case LEAGUE_ORGANIZER_MENU:
                boolean isStillOrganizing = true;
                do {
                    Team selectedTeam;
                    Player selectedPlayer;
                    Display.leagueSynopsis(registeredPlayers.length, availablePlayers.size(), teams);
                    switch (prompter.organizerMenu(teams)) {
                        case CREATE_NEW_TEAM:
                            if (teams.size() * Team.MAX_PLAYERS < registeredPlayers.length) {
                                teams.add(prompter.promptForTeamCreation());
                            } else {
                                Display.tooManyTeamsError(teams);
                            }
                            break;
                        case ADD_PLAYERS_TO_TEAM:
                            selectedPlayer = prompter.playerSelectionMenu(availablePlayers);
                            selectedTeam = prompter.teamSelectionMenu(teams);
                            if (selectedTeam.getPlayers().size() < 11) {
                                selectedTeam.addPlayer(selectedPlayer);
                                availablePlayers.remove(selectedPlayer);
                                Display.playerAddConfirmation(selectedPlayer, selectedTeam);
                            } else {
                                Display.tooManyPlayersOnTeamError();
                            }
                            break;
                        case REMOVE_PLAYERS_FROM_TEAM:
                            selectedTeam = prompter.teamSelectionMenu(teams);
                            if (selectedTeam.getPlayers().size() == 0) {
                                Display.noPlayersFoundError();
                            } else {
                                selectedPlayer = prompter.playerSelectionMenu(selectedTeam.getPlayers());
                                selectedTeam.getPlayers().remove(selectedPlayer);
                                availablePlayers.add(selectedPlayer);
                                Display.playerRemoveConfirmation(selectedPlayer, selectedTeam);
                            }
                            break;
                        case TEAM_FAIRNESS_REPORT:
                            //show roster by height, height summary, and experience summary
                            selectedTeam = prompter.teamSelectionMenu(teams);
                            Display.basicTeamInfo(selectedTeam);
                            Display.playersByHeight(selectedTeam.getPlayers());
                            Display.heightCountSummary(selectedTeam.getPlayers());
                            Display.teamExperienceSummary(selectedTeam);
                            break;
                        case LEAGUE_BALANCE_REPORT:
                            //for each team in the league, show a height distribution and experience summary
                            for (Team team : teams) {
                                Display.basicTeamInfo(team);
                                Display.heightDistributionSummary(team.getPlayers());
                                Display.teamExperienceSummary(team);
                            }
                            break;
                        case EXIT_TO_MAIN_MENU:
                            isStillOrganizing = false;
                            break;
                    }
                } while (isStillOrganizing);
                break;
            case EXIT_PROGRAM:
                //todo: confirm exit before processing
                switch (prompter.yesNoMenu("Are you sure you want to exit?")) {
                    case YES:
                        Display.goodbye();
                        isStillManaging = false;
                        break;
                    case NO:
                        break;
                }
        } while(isStillManaging);
        System.exit(0);
    }

}
