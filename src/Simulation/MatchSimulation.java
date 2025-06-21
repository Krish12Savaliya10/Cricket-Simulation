package Simulation;

import DataStructure.LinkedListOfPlayer;
import Validetion.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Stack;

class Team {
    int totalRun;
    int overPlayed;
    int wicketDown;
    int Target;
    String teamName;
    boolean won;
    boolean freeHitActive;

    public Team(String teamName) {
        this.teamName = teamName;
        totalRun = 0;
        overPlayed = 0;
        wicketDown = 0;
        won = false;
        freeHitActive = false;
    }
    public Team(Team Forstack){
        this.teamName =Forstack.teamName;
        this.totalRun = Forstack.totalRun;
        this.overPlayed = Forstack.overPlayed;
        this.wicketDown = Forstack.wicketDown;
        this.won = Forstack.won;
        this.freeHitActive = Forstack.freeHitActive;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                '}';
    }

    public String getTeamName() {
        return teamName;
    }
}

public class MatchSimulation {
    private static LinkedListOfPlayer team1Players;
    private static LinkedListOfPlayer team2Players;
    private static Team team1;
    private static Team team2;
    private static LinkedListOfPlayer.Player Batsman1;
    private static LinkedListOfPlayer.Player Batsman2;
    private static Stack<LinkedListOfPlayer.Player> Bat1;
    private static Stack<LinkedListOfPlayer.Player> Bat2;
    private static Stack<LinkedListOfPlayer.Bowler> Ball;
    private static Stack<Team> BatTeam;
    private static int MaxOverByBowler;
    private static void inputPlayers(Scanner sc, LinkedListOfPlayer teamPlayers, String teamName, int NumberOfPlayer) {
        int batsmenCount, allRoundersCount, bowlersCount;

        do {
            System.out.println("Enter number of batsmen for " + teamName + " (must be at least 2):");
            batsmenCount = getValidCount(sc, 2);

            System.out.println("Enter number of all-rounders for " + teamName + " (must be at least 0):");
            allRoundersCount = getValidCount(sc, 0);

            System.out.println("Enter number of bowlers for " + teamName + " (must be at least 2):");
            bowlersCount = getValidCount(sc, 2);

            if (batsmenCount + allRoundersCount + bowlersCount != NumberOfPlayer) {
                System.out.println("Total players must be exactly " + NumberOfPlayer + "! Current sum: " +
                        (batsmenCount + allRoundersCount + bowlersCount));
            }
        } while ((batsmenCount + allRoundersCount + bowlersCount) != NumberOfPlayer);

        sc.nextLine(); // Clear buffer

        System.out.println("\nEnter batsmen names:");
        for (int i = 1; i <= batsmenCount; i++) {
            System.out.print("Batsman " + i + ": ");
            String name = getValidName(sc);
            teamPlayers.addBatsman(name);
        }

        if (allRoundersCount != 0) {
            System.out.println("\nEnter all-rounders names:");
            for (int i = 1; i <= allRoundersCount; i++) {
                System.out.print("All-rounder " + i + ": ");
                String name = getValidName(sc);
                teamPlayers.addAllrounder(name);
            }
        }

        System.out.println("\nEnter bowlers names:");
        for (int i = 1; i <= bowlersCount; i++) {
            System.out.print("Bowler " + i + ": ");
            String name = getValidName(sc);
            teamPlayers.addBowler(name);
        }
    }

    private static int getValidCount(Scanner sc, int min) {
        while (true) {
            try {
                int count = sc.nextInt();
                if (count < min) {
                    throw new InvalidCountException("Minimum " + min + " required");
                }
                return count;
            } catch (InvalidCountException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input");
                sc.nextLine();
            }
        }
    }

    private static String getValidName(Scanner sc) {
        while (true) {
            try {
                String name = sc.nextLine().trim().toUpperCase();
                if (name.isEmpty()) {
                    throw new InvalidNameException("Name cannot be empty");
                }
                if (name.length() > 15) {
                    throw new InvalidNameException("Maximum 15 characters allowed");
                }
                return name;
            } catch (InvalidNameException e) {
                System.out.println(e.getMessage());
                System.out.print("Please enter again: ");
            }
        }
    }

    public static Team whoBat(Scanner sc, Team team1, Team team2, Team tosswin) {
        int choice = getValidChoice(sc,
                "What would you decide [1 for BAT, 2 for BALL]? ", 1, 2);

        if (choice == 1) {
            System.out.println(tosswin.teamName + " decided to BAT first\n");
            return tosswin;
        } else {
            Team fieldingTeam = (team1 == tosswin) ? team2 : team1;
            System.out.println(tosswin.teamName + " decided to BALL first\n");
            return fieldingTeam;
        }
    }

    static int getValidChoice(Scanner sc, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int choice = sc.nextInt();
                if (choice < min || choice > max) {
                    throw new InvalidChoiceException("Please enter between " + min + " and " + max);
                }
                sc.nextLine(); // Clear buffer
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
                sc.nextLine();
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static int getNoballChoice(Scanner sc) {
        while (true) {
            try {
                int run = sc.nextInt();
                if (run < 0 || run > 6 || run == 5) {
                    throw new InvalidRunException("Enter valid run (0-4 or 6):");
                }
                return run;
            } catch (InvalidRunException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }

    public static void displayScorecard(int i, int j, Team BattingTeam, LinkedListOfPlayer.Player Batsman1,
                                        LinkedListOfPlayer.Player Batsman2, LinkedListOfPlayer.Player Bowler,
                                        int Target, int inning) {
        System.out.println("-----------------");
        System.out.println(BattingTeam.teamName + (BattingTeam.freeHitActive ? " [FREE HIT]" : ""));

        System.out.print("(" + BattingTeam.totalRun + "/" + BattingTeam.wicketDown + ")          OVER: (" + i + "." + j + ")");
        if ((inning == 2))
            System.out.println("     Target " + Target);
        else
            System.out.println();

        double crr = (i == 0 && j == 0) ? 0.0 :
                (BattingTeam.totalRun * 6.0) / (i * 6 + j);
        System.out.println("CRR: " + String.format("%.2f", crr));

        System.out.println();
        System.out.println("Batter");
        printBatsman(Batsman1, Batsman1.isOnStrike());
        printBatsman(Batsman2, Batsman2.isOnStrike());

        System.out.println();
        System.out.println("Bowler");
        System.out.println(Bowler.getPlayerName() + "          " +
                Bowler.getWickets() + "-" + Bowler.getRunsGiven() +
                " (" + Bowler.getOversBowled() + "." + j + ")");
        System.out.println("-----------------\n");
    }

    private static void printBatsman(LinkedListOfPlayer.Player batsman, boolean onStrike) {
        System.out.println(batsman.getPlayerName() +
                (onStrike ? "*" : " ") + "         " +
                batsman.getRunsScored() + "(" + batsman.getBallsFaced() +
                ")     4's(" + batsman.getFours() + ")" +
                "   6's(" + batsman.getSixes() + ")");
    }

    public static void StrickRotete(LinkedListOfPlayer.Player Batsman1, LinkedListOfPlayer.Player Batsman2) {
        boolean temp = Batsman1.isOnStrike();
        Batsman1.setOnStrike(Batsman2.isOnStrike());
        Batsman2.setOnStrike(temp);
    }

    private static void handleNormalBall(Team battingTeam, LinkedListOfPlayer.Player batsman,
                                         LinkedListOfPlayer.Bowler bowler, Stack<String> overStack,
                                         int run,String msg) {
        System.out.println(msg);
        batsman.setRunsScored(batsman.getRunsScored() + run);
        batsman.setBallsFaced(batsman.getBallsFaced() + 1);
        bowler.setRunsGiven(bowler.getRunsGiven() + run);
        battingTeam.totalRun += run;
        overStack.push("" + run);
        if (run == 4)
            batsman.setFours(batsman.getFours() + 1);
        else if (run == 6)
            batsman.setSixes(batsman.getSixes() + 1);
        if(run%2!=0){
            StrickRotete(Batsman1,Batsman2);
        }
        battingTeam.freeHitActive = false;
    }

    private static void handleNoBall(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player batsman,
                                     LinkedListOfPlayer.Bowler bowler, Stack<String> overStack) {
        System.out.println("No ball! Free hit awarded for next ball!");
        battingTeam.freeHitActive = true;
        battingTeam.totalRun += 1;
        bowler.setRunsGiven(bowler.getRunsGiven() + 1);

        System.out.println("Enter e/E if Extra runs and Enter o/O if Runout in Noball else press [ENTER]:");
        String inNoball = sc.nextLine();
        if (inNoball.equalsIgnoreCase("E")) {
            int extra = Extras(sc, battingTeam, batsman, bowler);
            overStack.push("No/" + extra);
        } else if (inNoball.equalsIgnoreCase("O")) {
            handleRunout(sc, battingTeam, Batsman1.isOnStrike() ? Batsman1 : Batsman2,
                    Batsman1.isOnStrike() ? Batsman2 : Batsman1, bowler);
            battingTeam.freeHitActive = false;
        } else {
            System.out.println("Enter runs scored off the no ball (0-4)/6");
            int runs = getNoballChoice(sc);
            handleNoBall(battingTeam, batsman, bowler, runs);
            overStack.push("No/" + runs);
        }
    }

    private static void handleWide(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player batsman,
                                   LinkedListOfPlayer.Bowler bowler, Stack<String> overStack) {
        System.out.println("Wide!");
        System.out.println("Enter y/Y if Extra runs in wide else press [ENTER]:");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            int extra = Extras(sc, battingTeam, batsman, bowler);
            overStack.push("W/" + extra);
        } else {
            overStack.push("W");
        }
        battingTeam.totalRun += 1;
        bowler.setRunsGiven(bowler.getRunsGiven() + 1);
    }

    private static void handleExtras(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player batsman,
                                     LinkedListOfPlayer.Bowler bowler, Stack<String> overStack) {
        int extra = Extras(sc, battingTeam, batsman, bowler);
        overStack.push("E/" + extra);
    }
    private static boolean handleWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                        Stack<String> overStack, int totalPlayers, boolean isTeam1Batting) {
        // First ask if this is a runout (valid in all cases, including free hit)
        System.out.println("Enter y/Y if this is a Runout else press [ENTER]:");
        boolean isRunout = sc.nextLine().equalsIgnoreCase("y");

        if (isRunout) {
            return handleRunoutWicket(sc, battingTeam, bowler, overStack, totalPlayers);
        } else if (!battingTeam.freeHitActive) {
            return handleRegularWicket(sc, battingTeam, bowler, overStack, totalPlayers,isTeam1Batting);
        } else {
            System.out.println("Free hit active - cannot be out except runout!");
            return false;
        }
    }

    private static boolean handleRunoutWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                              Stack<String> overStack, int totalPlayers) {
        System.out.println("Runout!");
        battingTeam.wicketDown++;
        LinkedListOfPlayer.Player outPlayer = Batsman1.isOnStrike() ? Batsman1 : Batsman2;

        int runs = handleRunout(sc, battingTeam, outPlayer,
                (outPlayer == Batsman1 ? Batsman2 : Batsman1), bowler);
        overStack.push("RO/" + runs);

        return battingTeam.wicketDown == totalPlayers - 1;
    }

    private static boolean handleRegularWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                               Stack<String> overStack, int totalPlayers, boolean isTeam1Batting) {
        System.out.println("Wicket!");
        battingTeam.wicketDown++;
        LinkedListOfPlayer.Player outPlayer = Batsman1.isOnStrike() ? Batsman1 : Batsman2;

        overStack.push("W");
        bowler.setWickets(bowler.getWickets() + 1);

        if (battingTeam.wicketDown == totalPlayers - 1)
            return true;

        return replaceBatsman(sc, battingTeam, outPlayer, isTeam1Batting);
    }

    private static boolean replaceBatsman(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player outPlayer,
                                          boolean isTeam1Batting) {
        LinkedListOfPlayer playerList = isTeam1Batting ? team1Players : team2Players;
        playerList.displayAvailablePlayersName();

        outPlayer.setPlaying(false);
        outPlayer.setOut(true);

        LinkedListOfPlayer.Player newBatsman = getValidPlayer(sc, playerList, "new batsman");
        newBatsman.setPlaying(true);

        if (outPlayer == Batsman1) {
            Batsman1 = newBatsman;
            Batsman1.setOnStrike(true);
        } else {
            Batsman2 = newBatsman;
            Batsman2.setOnStrike(false);
        }

        // Clear free hit status after any wicket (even runout during free hit)
        battingTeam.freeHitActive = false;

        return false;
    }

    private static int handleRunout(Scanner sc, Team team, LinkedListOfPlayer.Player striker,
                                    LinkedListOfPlayer.Player nonStriker, LinkedListOfPlayer.Bowler bowler) {
        System.out.println("Enter runs scored before runout:");
        int runs = getValidChoice(sc, "Enter runs (0-2):", 0, 2);

        team.totalRun += runs;
        striker.setRunsScored(striker.getRunsScored() + runs);
        bowler.setRunsGiven(bowler.getRunsGiven() + runs);

        System.out.println("Who got run out? [1 for Striker / 2 for Non-Striker]");
        int whoRunOut = getValidChoice(sc, "Choose 1 or 2:", 1, 2);

        System.out.println("Which end was the runout? [1 for Keeper's End / 2 for Bowler's End]");
        int runoutEnd = getValidChoice(sc, "Choose 1 or 2:", 1, 2);

        LinkedListOfPlayer currentTeam = (team == team1) ? team1Players : team2Players;
        currentTeam.displayAvailablePlayersName();

        LinkedListOfPlayer.Player newBatsman = getValidPlayer(sc, currentTeam, "new batsman");
        newBatsman.setPlaying(true);


        // Determine which batsman is out
        LinkedListOfPlayer.Player outPlayer = (whoRunOut == 1) ? striker : nonStriker;
        outPlayer.setOut(true);
        outPlayer.setPlaying(false);

        // Replace the out player
        if (outPlayer == Batsman1) {
            Batsman1 = newBatsman;
        } else {
            Batsman2 = newBatsman;
        }

        // Set strike based on runout end
        boolean strikerKeepsEnd = (runoutEnd == 1); // Keeper's end means striker keeps end
        if (strikerKeepsEnd) {
            if (whoRunOut == 1) { // Striker out at keeper's end
                Batsman1.setOnStrike(true);
                Batsman2.setOnStrike(false);
            } else { // Non-striker out at keeper's end
                Batsman1.setOnStrike(false);
                Batsman2.setOnStrike(true);
            }
        } else { // Bowler's end
            if (whoRunOut == 1) { // Striker out at bowler's end
                Batsman1.setOnStrike(false);
                Batsman2.setOnStrike(true);
            } else { // Non-striker out at bowler's end
                Batsman1.setOnStrike(true);
                Batsman2.setOnStrike(false);
            }
        }

        return runs;
    }



    private static void playInnings(Scanner sc, Team BattingTeam, boolean isTeam1Batting,
                                    int totalOvers, int inning, int NumberOfPlayer, int target){
        int j = 0;
        int i;
        final int DOTBALL = 0;
        final int SINGLE = 1;
        final int DOUBLE = 2;
        final int ThrEERUNS = 3;
        final int BOUNDRY = 4;
        final int SIX = 6;
        LinkedListOfPlayer.Bowler Bowler = getValidBowler(sc, isTeam1Batting ? team2Players : team1Players, null);
        Stack<String> ThisOver = new Stack<>();

        outerloop: for (i = 0; i < totalOvers; i++) {
            if (!ThisOver.isEmpty()) {
                System.out.println("Over " + (i) + ":" + ThisOver);
            }
            ThisOver.clear();

            for (j = 0; j < 6; j++) {


                if (!ThisOver.isEmpty()) {
                    System.out.println("Over " + (i + 1) + ":" + ThisOver);
                }
                displayScorecard(i, j, BattingTeam, Batsman1, Batsman2, Bowler, target, inning);

                LinkedListOfPlayer.Player CurrentBatsman = Batsman1.isOnStrike() ? Batsman1 : Batsman2;
                String runInput = getValidRunInput(sc);

                BatTeam.push(new Team(BattingTeam));
                Ball.push(new LinkedListOfPlayer.Bowler(Bowler));
                Bat1.push(new LinkedListOfPlayer.Player(Batsman1));
                Bat2.push(new LinkedListOfPlayer.Player(Batsman2));

                switch (runInput) {
                    case "0":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOTBALL, "DotBall");
                        break;

                    case "1":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, SINGLE, "Single");
                        break;

                    case "2":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOUBLE, "Double");
                        break;

                    case "3":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, ThrEERUNS, "Three runs");
                        break;

                    case "4":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, BOUNDRY , "Boundary");
                        break;

                    case "6":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, SIX, "Six");
                        break;

                    case "N":
                        handleNoBall(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver);
                        j--;
                        break;

                    case "W":
                        handleWide(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver);
                        j--;
                        break;

                    case "E":
                        handleExtras(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver);
                        break;

                    case "O":
                        boolean end = handleWicket(sc, BattingTeam, Bowler, ThisOver, NumberOfPlayer, isTeam1Batting);
                        if (end)
                            break outerloop;
                        break;

                    case "U":
                        if(i==0 && j==0){
                            System.out.println("It's a first ball");
                            j--;
                        }
                        else {
                            Bat1.pop();
                            Batsman1=Bat1.pop();

                            Bat2.pop();
                            Batsman2=Bat2.pop();

                            Ball.pop();
                            Bowler=Ball.pop();

                            BatTeam.pop();
                            BattingTeam=BatTeam.pop();

                            ThisOver.pop();
                            if(j==0){
                                j=4;
                                i--;
                            }
                            else
                                j-=2;
                        }
                        break;

                    default:
                        System.out.println("Invalid input, ball will be repeated");
                        j--;
                        break;
                }
                if(j==5){
                    System.out.println("Enter u/U for change last ball input else press [ENTER]");
                    if(sc.nextLine().equalsIgnoreCase("U")){
                        Batsman1=Bat1.pop();
                        Batsman2=Bat2.pop();
                        Bowler=Ball.pop();
                        BattingTeam=BatTeam.pop();
                        ThisOver.pop();
                        j--;
                    }
                }


                if (inning == 2 && BattingTeam.totalRun >= target) {
                    BattingTeam.won = true;
                    break outerloop;
                }
            }

            System.out.println("Over " + (i + 1) + " completed.");
            Bowler.setOversBowled(Bowler.getOversBowled() + 1);
            if (i < totalOvers - 1) {
                Bowler = getValidBowler(sc, isTeam1Batting ? team2Players : team1Players, Bowler);
            }
        }

        if (inning == 1) {
            System.out.println("Inning 1 completed.");
            team1.Target = team1.totalRun + 1;
        } else {
            System.out.println("Match ended.");
        }

        if (i == totalOvers) {
            displayScorecard(totalOvers, 0, BattingTeam, Batsman1, Batsman2, Bowler, target, inning);
        } else {
            displayScorecard(i, j, BattingTeam, Batsman1, Batsman2, Bowler, target, inning);
        }
    }

    private static LinkedListOfPlayer.Player getValidPlayer(Scanner sc, LinkedListOfPlayer team, String role) {
        while (true) {
            System.out.print("Enter " + role + " name: ");
            String name = sc.nextLine().toUpperCase();
            LinkedListOfPlayer.Player player = team.getPlayer(name);

            if (player == null) {
                System.out.println("Player not found!");
                team.displayAvailablePlayersName();
                continue;
            }

            if (player.isOut()) {
                System.out.println("Player already out or playing!");
                continue;
            }

            return player;
        }
    }
    private static LinkedListOfPlayer.Bowler getValidBowler(Scanner sc, LinkedListOfPlayer team, LinkedListOfPlayer.Bowler previousBowler) {
        team.displayBowlersAndAllRoundersName(previousBowler,MaxOverByBowler);

        while (true) {
            System.out.print("Enter new bowler name: ");
            String name = sc.nextLine().toUpperCase();
            LinkedListOfPlayer.Player player = team.getPlayer(name);

            if (player == null) {
                System.out.println("Player not found!");
                team.displayBowlersAndAllRoundersName(previousBowler,MaxOverByBowler);
                continue;
            }

            if (player == previousBowler) {
                System.out.println("Same bowler cannot bowl consecutive overs!");
                continue;
            }

            if (!(player instanceof LinkedListOfPlayer.Bowler)) {
                System.out.println("Selected player cannot bowl!");
                continue;
            }

            return (LinkedListOfPlayer.Bowler) player;
        }
    }

    private static String getValidRunInput(Scanner sc) {
        while (true) {
            System.out.println("Enter run");
            System.out.print("0 1 2 3 4 6 \nN/n for <Noball> W/w for <wide> O/o for <Out> E/e for <Extras>\nU/u for <Undo>\nEnter: ");
            String input = sc.nextLine().toUpperCase();

            if (input.matches("[012346NWOEU]")) {
                return input;
            }

            System.out.println("Invalid input! Please try again.");
        }
    }

    private static void handleNoBall(Team team, LinkedListOfPlayer.Player batsman, LinkedListOfPlayer.Bowler bowler, int runs) {
        team.totalRun += (1 + runs);
        batsman.setRunsScored(batsman.getRunsScored() + runs);
        bowler.setRunsGiven(bowler.getRunsGiven() + 1 + runs);

        if (runs == 6) {
            batsman.setSixes(batsman.getSixes() + 1);
        } else if (runs == 4) {
            batsman.setFours(batsman.getFours() + 1);
        }

        if (runs % 2 == 1) {
            StrickRotete(batsman, (batsman == Batsman1 ? Batsman2 : Batsman1));
        }
    }

    private static int Extras(Scanner sc, Team team, LinkedListOfPlayer.Player batsman, LinkedListOfPlayer.Bowler bowler) {
        System.out.println("Enter runs scored ball (0-4)");
        int runs = getValidChoice(sc, "Extras", 0, 4);
        team.totalRun += runs;
        bowler.setRunsGiven(bowler.getRunsGiven() + runs);

        if (runs % 2 == 1) {
            StrickRotete(batsman, (batsman == Batsman1 ? Batsman2 : Batsman1));
        }
        return runs;
    }



    private static void displayMatchResult(Team BattingTeam, int numberOfPlayer) {
        System.out.println("\n=== MATCH RESULT ===");
        if (BattingTeam.won) {
            System.out.println(BattingTeam.teamName + " won by " + (numberOfPlayer - BattingTeam.wicketDown - 1) + " wicket(s).");
        } else if (BattingTeam == team1) {
            System.out.println(team2.teamName + " won by " + (team2.totalRun - team1.totalRun ) + " runs.");
        } else if (BattingTeam == team2) {
            System.out.println(team1.teamName + " won by " + (team1.totalRun - team2.totalRun ) + " runs.");
        } else {
            System.out.println("Match drawn");
        }
    }

    public static void Stimuletion() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        Bat1=new Stack<>();
        Bat2=new Stack<>();
        Ball=new Stack<>();
        BatTeam=new Stack<>();

        System.out.println("Enter name for Team 1:");
        String team1Name = getValidName(sc);
        System.out.println("Enter name for Team 2:");
        String team2Name = getValidName(sc);

        team1 = new Team(team1Name);
        team2 = new Team(team2Name);

        System.out.println("Enter number of players in each team:");
        int NumberOfPlayer = getValidChoice(sc, "Enter player count 6 to 11:", 6, 11);

        team1Players = new LinkedListOfPlayer();
        team2Players = new LinkedListOfPlayer();

        System.out.println("\nEnter players for " + team1Name);
        inputPlayers(sc, team1Players, team1Name, NumberOfPlayer);

        System.out.println("\nEnter players for " + team2Name);
        inputPlayers(sc, team2Players, team2Name, NumberOfPlayer);

        System.out.println("\n" + team1Name + " Players:");
        team1Players.displayPlayerName();

        System.out.println("\n" + team2Name + " Players:");
        team2Players.displayPlayerName();

        TossThread toss = new TossThread(sc, team1, team2);
        toss.start();
        toss.join();
        Team BattingTeam = whoBat(sc, team1, team2, toss.getWinner());
        boolean isTeam1Batting = BattingTeam == team1;

        System.out.println("Enter Number Of Overs (2-90):");
        int over = getValidChoice(sc, "", 2, 90);

        MaxOverByBowler=getValidChoice(sc,"Max over by singal Bowler",1,over/2);

        System.out.println("\n=== FIRST INNINGS START ===");
        if (isTeam1Batting) {
            team1Players.displayAvailablePlayersName();
        } else {
            team2Players.displayAvailablePlayersName();
        }
        Batsman1 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 1");
        Batsman1.setPlaying(true);

        Batsman2 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 2");
        Batsman2.setPlaying(true);
        Batsman2.setOnStrike(false);

        playInnings(sc, BattingTeam, isTeam1Batting, over, 1, NumberOfPlayer, 0);

        int Target = BattingTeam.totalRun + 1;
        BattingTeam = (BattingTeam == team1) ? team2 : team1;
        isTeam1Batting = !isTeam1Batting;

        System.out.println("\n=== SECOND INNINGS START ===");
        if (isTeam1Batting) {
            team1Players.displayAvailablePlayersName();
        } else {
            team2Players.displayAvailablePlayersName();
        }
        Batsman1 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 1");
        Batsman1.setPlaying(true);

        Batsman2 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 2");
        Batsman2.setPlaying(true);
        Batsman2.setOnStrike(false);

        playInnings(sc, BattingTeam, isTeam1Batting, over, 2, NumberOfPlayer, Target);

        displayMatchResult(BattingTeam, NumberOfPlayer);
        sc.close();
    }

    public static void main(String[] args) throws InterruptedException {
        Stimuletion();
    }
}

class TossThread extends Thread {
    private final Scanner sc;
    private final Team team1;
    private final Team team2;
    private Team winner;

    public TossThread(Scanner sc, Team team1, Team team2) {
        this.sc = sc;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Team getWinner() {
        return winner;
    }

    @Override
    public void run() {
        System.out.println("------ " + team1.teamName + " Vs " + team2.teamName + " ------");
        System.out.println("Toss time");

        int tossChoice = MatchSimulation.getValidChoice(sc,
                team1.teamName + " Enter 0 for HEAD and 1 for TAIL: ", 0, 1);

        System.out.println(team1.teamName + " calls " + (tossChoice == 0 ? "HEAD" : "TAIL"));
        System.out.println("Coin in the air...\n");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int coinResult = (int) (Math.random() * 2);
        winner = (tossChoice == coinResult) ? team1 : team2;

        System.out.println("It's " + (coinResult == 0 ? "HEAD" : "TAIL"));
        System.out.println(winner.teamName + " won the toss");
    }
}
