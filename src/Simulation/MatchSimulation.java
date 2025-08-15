package Simulation;

import DataStructure.LinkedListOfPlayer;
import DataStructure.StackOfBatsman;
import DataStructure.StackOfBowler;
import DataStructure.StackOfTeam;
import Validetion.*;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Stack;

import static DataBase.SQLquery.*;
import static DataBase.SQLquery.updateMatchStatus;


public class MatchSimulation {
    static LinkedListOfPlayer team1Players;
    static LinkedListOfPlayer team2Players;
    static LinkedListOfPlayer.Bowler Bowler;
    static Team team1;
    static Team team2;
    private static LinkedListOfPlayer.Player Batsman1;
    private static LinkedListOfPlayer.Player Batsman2;
    private static StackOfBatsman Bat1;
    private static StackOfBatsman Bat2;
    private static StackOfBowler Ball;
    private static StackOfTeam BatTeam;
    private static int MaxOverByBowler;
    static int getValidCount(Scanner sc, int min) {
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

    static String getValidName(Scanner sc) {
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
            Team batting = (team1 == tosswin) ? team2 : team1;
            System.out.println(tosswin.teamName + " decided to BALL first\n");
            return batting;
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
                    throw new InvalidRunException("Enter valid run [(0-4) or 6)]:");
                }
                return run;
            } catch (InvalidRunException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }

    public static void displayScorecard(int i, int j, Team BattingTeam,
                                        LinkedListOfPlayer.Player Batsman1,
                                        LinkedListOfPlayer.Player Batsman2,
                                        LinkedListOfPlayer.Bowler Bowler,
                                        int Target, int inning) {

        System.out.println("-----------------");
        String teamLine = BattingTeam.teamName + (BattingTeam.freeHitActive ? " [FREE HIT]" : "");
        System.out.println(teamLine + " ".repeat(Math.max(0, 25 - teamLine.length())));

        String scorePart = "(" + BattingTeam.totalRun + "/" + BattingTeam.wicketDown + ")";
        String overPart = "OVER: (" + i + "." + j + ")";
        String targetPart = (inning == 2) ? "Target " + Target : "";

        System.out.println(scorePart + " ".repeat(Math.max(0, 15 - scorePart.length())) +
                        overPart + " ".repeat(Math.max(0, 15 - overPart.length())) +
                        targetPart);

        double crr = (i == 0 && j == 0) ? 0.0 :
                (BattingTeam.totalRun * 6.0) / (i * 6 + j);
        System.out.println("CRR: " + String.format("%.2f", crr));


        System.out.println();
        System.out.println("Batter             R     B     4s     6s");
        printBatsmanWithSpacing(Batsman1);
        printBatsmanWithSpacing(Batsman2);


        System.out.println();
        System.out.println("Bowler             O     R     W");
        String bowlerName = Bowler.getPlayerName();
        System.out.println(
                bowlerName + " ".repeat(Math.max(0, 18 - bowlerName.length())) +
                        Bowler.getOversBowled() + " ".repeat(6 - String.valueOf(Bowler.getOversBowled()).length()) +
                        LinkedListOfPlayer.Bowler.getRunsGiven() + " ".repeat(7 - String.valueOf(LinkedListOfPlayer.Bowler.getRunsGiven()).length()) +
                        LinkedListOfPlayer.Bowler.getWickets());

        System.out.println("-----------------\n");
    }

    private static void printBatsmanWithSpacing(LinkedListOfPlayer.Player batsman) {
        String name = batsman.getPlayerName() + (batsman.isOnStrike() ? "*" : "");
        System.out.println(
                name + " ".repeat(Math.max(0, 18 - name.length())) +
                        batsman.getRunsScored() + " ".repeat(6 - String.valueOf(batsman.getRunsScored()).length()) +
                        batsman.getBallsFaced() + " ".repeat(6 - String.valueOf(batsman.getBallsFaced()).length()) +
                        batsman.getFours() + " ".repeat(7 - String.valueOf(batsman.getFours()).length()) +
                        batsman.getSixes());
    }


    public static void StrickRotete(LinkedListOfPlayer.Player Batsman1, LinkedListOfPlayer.Player Batsman2) {
        boolean temp = Batsman1.isOnStrike();
        Batsman1.setOnStrike(Batsman2.isOnStrike());
        Batsman2.setOnStrike(temp);
    }

    private static void handleNormalBall(Team battingTeam, LinkedListOfPlayer.Player batsman,
                                         LinkedListOfPlayer.Bowler bowler, Stack<String> overStack,
                                         int run, String msg) {
        System.out.println(msg);
        batsman.setRunsScored(batsman.getRunsScored() + run);
        batsman.setBallsFaced(batsman.getBallsFaced() + 1);
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() + run);
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
                                     LinkedListOfPlayer.Bowler bowler, Stack<String> overStack,int matchId,
                                     int inning,int over,int ball) throws SQLException {
        System.out.println("No ball! Free hit awarded for next ball!");
        battingTeam.freeHitActive = true;
       // battingTeam.totalRun += 1;
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() + 1);

        System.out.println("Enter e/E if Extra runs and Enter o/O if Runout in Noball else press [ENTER]:");
        String inNoball = sc.nextLine();
        if (inNoball.equalsIgnoreCase("E")) {
            int extra = Extras(sc, battingTeam, batsman, bowler);
            updateBallByBallNoBall(matchId,inning,over,ball,0,(extra+1));
            overStack.push("No/" + extra);
        } else if (inNoball.equalsIgnoreCase("O")) {
            handleRunout(sc, battingTeam, Batsman1.isOnStrike() ? Batsman1 : Batsman2,
                    Batsman1.isOnStrike() ? Batsman2 : Batsman1, bowler,matchId,inning,over,ball);
            battingTeam.freeHitActive = false;
        } else {
            System.out.println("Enter runs scored off the no ball (0-4)/6");
            int runs = getNoballChoice(sc);
            updateBallByBallNoBall(matchId,inning,over,ball,runs,1);
            handleNoBall(battingTeam, batsman, bowler, runs);
            overStack.push("No/" + runs);
        }
    }

    private static int handleWide(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player batsman,
                                   LinkedListOfPlayer.Bowler bowler, Stack<String> overStack) {
        System.out.println("Wide!");
        System.out.println("Enter y/Y if Extra runs in wide else press [ENTER]:");
        int extra=0;
        if (sc.nextLine().equalsIgnoreCase("y")) {
            extra = Extras(sc, battingTeam, batsman, bowler);
            overStack.push("W/" + extra);

        } else {
            overStack.push("W");
        }
        battingTeam.totalRun += 1;
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() + 1);
        return extra+1;
    }

    private static int handleExtras(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player batsman,
                                     LinkedListOfPlayer.Bowler bowler, Stack<String> overStack) {
        int extra = Extras(sc, battingTeam, batsman, bowler);
        overStack.push("E/" + extra);
        return extra;
    }
    private static boolean handleWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                        Stack<String> overStack, int totalPlayers, boolean isTeam1Batting,int matchID, int inning,int over,int ball) throws SQLException {
        // First ask if this is a runout (valid in all cases, including free hit)
        System.out.println("Enter y/Y if this is a Runout else press [ENTER]:");
        boolean isRunout = sc.nextLine().equalsIgnoreCase("y");

        if (isRunout) {
            return handleRunoutWicket(sc, battingTeam, bowler, overStack, totalPlayers,matchID,inning,over,ball);
        } else if (!battingTeam.freeHitActive) {
            return handleRegularWicket(sc, battingTeam, bowler, overStack, totalPlayers,isTeam1Batting,matchID,inning,over,ball);
        } else {
            System.out.println("Free hit active - cannot be out except runout!");
            return false;
        }
    }

    private static boolean handleRunoutWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                              Stack<String> overStack, int totalPlayers,int matchID, int inning,int over,int ball) throws SQLException {
        System.out.println("Runout!");
        battingTeam.wicketDown++;
        LinkedListOfPlayer.Player onStrinck = Batsman1.isOnStrike() ? Batsman1 : Batsman2;

        int runs = handleRunout(sc, battingTeam, onStrinck,
                (onStrinck == Batsman1 ? Batsman2 : Batsman1), bowler,matchID,inning,over,ball);
        overStack.push("RO/" + runs);

        return battingTeam.wicketDown == totalPlayers - 1;
    }

    private static boolean handleRegularWicket(Scanner sc, Team battingTeam, LinkedListOfPlayer.Bowler bowler,
                                               Stack<String> overStack, int totalPlayers, boolean isTeam1Batting,int matchID,int inning,int over,int ball) throws SQLException {
        System.out.println("Wicket!");
        battingTeam.wicketDown++;
        LinkedListOfPlayer.Player outPlayer = Batsman1.isOnStrike() ? Batsman1 : Batsman2;
        updateBallByBallWicket(matchID,outPlayer.getPlayerId(),inning,over,ball,0);

        overStack.push("O");
        bowler.setWickets(LinkedListOfPlayer.Bowler.getWickets() + 1);

        if (battingTeam.wicketDown == totalPlayers - 1)
            return true;

        return replaceBatsman(sc, battingTeam, outPlayer, isTeam1Batting,matchID);
    }

    private static boolean replaceBatsman(Scanner sc, Team battingTeam, LinkedListOfPlayer.Player outPlayer,
                                          boolean isTeam1Batting,int matchID) throws SQLException {
        LinkedListOfPlayer playerList = isTeam1Batting ? team1Players : team2Players;
        playerList.displayAvailablePlayersName();
        updateBattingStatus(matchID,outPlayer.getPlayerId(),"OUT");
        outPlayer.setPlaying(false);
        outPlayer.setOut(true);

        LinkedListOfPlayer.Player newBatsman = getValidPlayer(sc, playerList, "new batsman");
        newBatsman.setPlaying(true);
        updateBattingStatus(matchID,newBatsman.getPlayerId(),"PLAYING");


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
                                    LinkedListOfPlayer.Player nonStriker, LinkedListOfPlayer.Bowler bowler,int matchID,int inning,int over,int ball) throws SQLException {
        System.out.println("Enter runs scored before runout:");
        int runs = getValidChoice(sc, "Enter runs (0-2):", 0, 2);

        team.totalRun += runs;
        striker.setRunsScored(striker.getRunsScored() + runs);
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() + runs);

        System.out.println("Who got run out? [1 for Striker / 2 for Non-Striker]");
        int whoRunOut = getValidChoice(sc, "Choose 1 or 2:", 1, 2);

        System.out.println("Which end was the runout? [1 for Keeper's End / 2 for Bowler's End]");
        int runoutEnd = getValidChoice(sc, "Choose 1 or 2:", 1, 2);

        LinkedListOfPlayer currentTeam = (team == team1) ? team1Players : team2Players;
        currentTeam.displayAvailablePlayersName();

        LinkedListOfPlayer.Player newBatsman = getValidPlayer(sc, currentTeam, "new batsman");
        newBatsman.setPlaying(true);
        updateBattingStatus(matchID,newBatsman.getPlayerId(),"PLAYING");


        // Determine which batsman is out
        LinkedListOfPlayer.Player outPlayer = (whoRunOut == 1) ? striker : nonStriker;
        updateBattingStatus(matchID,outPlayer.getPlayerId(),"OUT");
        outPlayer.setOut(true);
        outPlayer.setPlaying(false);
        updateBallByBallWicket(matchID,outPlayer.getPlayerId(),inning,over,ball,runs);


        // Replace the out player
        if (outPlayer == Batsman1 && runoutEnd==1) {
            Batsman1 = newBatsman;
            Batsman1.setOnStrike(true);
            Batsman2.setOnStrike(false);
        } else if(outPlayer == Batsman1 && runoutEnd==2){
            Batsman1 = newBatsman;
            Batsman2.setOnStrike(true);
            Batsman1.setOnStrike(false);
        } else if (outPlayer == Batsman2 && runoutEnd==1) {
            Batsman2 = newBatsman;
            Batsman2.setOnStrike(true);
            Batsman1.setOnStrike(false);
        }
        else {
            Batsman2 = newBatsman;
            Batsman1.setOnStrike(true);
            Batsman2.setOnStrike(false);
        }

        // Set strike based on runout end

        return runs;
    }



    private static void playInnings(Scanner sc, Team BattingTeam, boolean isTeam1Batting,
                                    int totalOvers, int inning, int NumberOfPlayer, int target,int matchId) throws SQLException {
        int j = 0;
        int i;
        final int dotBall = 0;
        final int SINGLE = 1;
        final int DOUBLE = 2;
        final int threeRuns = 3;
        final int FOUR = 4;
        final int SIX = 6;

         Bowler = getValidBowler(sc, isTeam1Batting ? team2Players : team1Players, null);
        Stack<String> ThisOver = new Stack<>();

        outerLoop: for (i = 0; i < totalOvers; i++) {

            ThisOver.clear();

            for (j = 0; j < 6; j++) {


                displayScorecard(i, j, BattingTeam, Batsman1, Batsman2, Bowler, target, inning);
                if (!ThisOver.isEmpty()) {
                    System.out.println("Over " + (i + 1) + ":" + ThisOver);
                }

                LinkedListOfPlayer.Player CurrentBatsman = Batsman1.isOnStrike() ? Batsman1 : Batsman2;
                LinkedListOfPlayer.Player nonStricker= (!Batsman1.isOnStrike()) ? Batsman1 : Batsman2;
                String runInput = getValidRunInput(sc);

                BatTeam.push(new Team(BattingTeam));
                Ball.push(new LinkedListOfPlayer.Bowler(Bowler));
                Bat1.push(new LinkedListOfPlayer.Player(Batsman1));
                Bat2.push(new LinkedListOfPlayer.Player(Batsman2));

                switch (runInput) {
                    case "0":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, dotBall, "DotBall");
                            insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                    Bowler.getPlayerId(),dotBall,0,null,"DotBall");
                        break;

                    case "1":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, SINGLE, "Single");
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),SINGLE,0,null,"Single");

                        break;

                    case "2":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOUBLE, "Double");
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),DOUBLE,0,null,"Double");

                        break;

                    case "3":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, threeRuns, "Three runs");
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOUBLE, "Double");
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),threeRuns,0,null,"Three runs");

                        break;

                    case "4":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, FOUR , "Boundary");
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOUBLE, "Double");
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),FOUR,0,null,"Boundary");

                        break;

                    case "6":
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, SIX, "Six");
                        handleNormalBall(BattingTeam, CurrentBatsman, Bowler, ThisOver, DOUBLE, "Double");
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),SIX,0,null,"Six");

                        break;

                    case "N":
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),dotBall,0,null,"NoBall");
                        handleNoBall(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver,matchId,inning,i,j);

                        j--;
                        break;

                    case "W":
                       int wideRuns = handleWide(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver);
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),0,wideRuns,null,"Wide");
                        j--;
                        break;

                    case "E":
                        int extras=handleExtras(sc, BattingTeam, CurrentBatsman, Bowler, ThisOver);
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),0,extras,null,"Extras");

                        break;

                    case "O":
                        insertBallByBall(matchId,inning,i,j, CurrentBatsman.getPlayerId(),nonStricker.getPlayerId(),
                                Bowler.getPlayerId(),0,0,null,"Wicket");
                        boolean end = handleWicket(sc, BattingTeam, Bowler, ThisOver, NumberOfPlayer, isTeam1Batting,matchId,inning,i,j);
                        if (end)
                            break outerLoop;
                        break;

                    case "U":
                        if(i==0 && j==0){
                            System.out.println("It's a first ball");
                            j--;
                        }
                        else {
                            Bat1.pop();
                            Batsman1 = Bat1.pop();

                            Bat2.pop();
                            Batsman2 = Bat2.pop();

                            Ball.pop();
                            Bowler = Ball.pop();

                            BatTeam.pop();
                            BattingTeam = BatTeam.pop();

                            String checkUndo = ThisOver.pop();
                            undoLastBallOfMatchInning(matchId,inning);
                            if (!checkUndo.matches("[NW]")) {
                                    j -= 2;
                            }
                        }
                        break;

                    default:
                        System.out.println("Invalid input, ball will be repeated");
                        j--;
                        break;
                }
                updateBattingStats(matchId,Batsman1.getPlayerId(),Batsman1.getRunsScored(),Batsman1.getBallsFaced(), Batsman1.getFours(), Batsman1.getSixes());
                updateBattingStats(matchId,Batsman2.getPlayerId(),Batsman2.getRunsScored(),Batsman2.getBallsFaced(), Batsman2.getFours(), Batsman2.getSixes());
                updateTeamBattingStats(matchId,BattingTeam.teamId,BattingTeam.totalRun,BattingTeam.wicketDown,Double.parseDouble(i+"."+((j==-1)?0:j)));
                updateBowlingStats(matchId,Bowler.getPlayerId(), LinkedListOfPlayer.Bowler.getWickets(),Double.parseDouble(i+"."+j), LinkedListOfPlayer.Bowler.getRunsGiven());

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
                    break outerLoop;
                }
            }
            StrickRotete(Batsman1,Batsman2);

            System.out.println("Over " + (i + 1) + " completed.");
            Bowler.setOversBowled(Bowler.getOversBowled() + 1);
            if (i < totalOvers - 1) {
                Bowler = getValidBowler(sc, isTeam1Batting ? team2Players : team1Players, Bowler);
            }
        }
        updateBattingStats(matchId,Batsman1.getPlayerId(),Batsman1.getRunsScored(),Batsman1.getBallsFaced(), Batsman1.getFours(), Batsman1.getSixes());
        updateBattingStats(matchId,Batsman2.getPlayerId(),Batsman2.getRunsScored(),Batsman2.getBallsFaced(), Batsman2.getFours(), Batsman2.getSixes());
        updateTeamBattingStats(matchId,BattingTeam.teamId,BattingTeam.totalRun,BattingTeam.wicketDown,Double.parseDouble(i+"."+j));
        updateBowlingStats(matchId,Bowler.getPlayerId(), LinkedListOfPlayer.Bowler.getWickets(),Double.parseDouble(i+"."+j), LinkedListOfPlayer.Bowler.getRunsGiven());
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
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() +1 + runs);

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
        bowler.setRunsGiven(LinkedListOfPlayer.Bowler.getRunsGiven() + runs);

        if (runs % 2 == 1) {
            StrickRotete(batsman, (batsman == Batsman1 ? Batsman2 : Batsman1));
        }
        return runs;
    }



    private static void displayMatchResult(Team BattingTeam, int numberOfPlayer,Match match) throws SQLException {

        System.out.println("\n=== MATCH RESULT ===");
        if (BattingTeam.won) {
            updateWinner(match.MatchId, BattingTeam.teamId);
            System.out.println(BattingTeam.teamName + " won by " + (numberOfPlayer - BattingTeam.wicketDown - 1) + " wicket(s).");
            BattingTeam.setWin(BattingTeam.getWin()+1);
            BattingTeam.setPoints(BattingTeam.getPoints()+2);
            if(BattingTeam==team1)
                team2.setLose(team2.getLose()+1);
            else
                team1.setLose(team1.getLose()+1);
        } else if (BattingTeam == team1) {
            System.out.println(team2.teamName + " won by " + (team2.totalRun - team1.totalRun ) + " runs.");
            team2.setWin(team2.getWin()+1);
            updateWinner(match.MatchId, team2.teamId);
            team2.setPoints(team2.getPoints()+2);
            team1.setLose(team1.getLose()+1);
        } else if (BattingTeam == team2) {
            System.out.println(team1.teamName + " won by " + (team1.totalRun - team2.totalRun ) + " runs.");
            team1.setWin(team1.getWin()+1);
            updateWinner(match.MatchId, team1.teamId);
            team1.setPoints(team1.getPoints()+2);
            team2.setLose(team2.getLose()+1);
        } else {
            System.out.println("Match drawn");
            team1.setPoints(team1.getPoints()+1);
            team2.setPoints(team2.getPoints()+1);
        }
        updateMatchStatus(match.MatchId, "COMPLETED");
        match.setMatchStatus("COMPLETED");

    }

    public static void Simulation(Scanner sc, Team Team1, Team Team2, LinkedListOfPlayer Team1Players, LinkedListOfPlayer Team2Players, int over, int NumberOfPlayer,Match match) throws InterruptedException, SQLException {
        updateMatchStatus(match.MatchId, "LIVE");
        Bat1=new StackOfBatsman();
        Bat2=new StackOfBatsman();
        Ball=new StackOfBowler();
        BatTeam=new StackOfTeam();

        team1 = Team1;
        String team1Name=team1.getTeamName();
        team2 = Team2;
        String team2Name =team2.getTeamName();

        team1Players = Team1Players;
        team2Players = Team2Players;

        System.out.println("\n" + team1Name + " Players:");
        team1Players.displayPlayerName();

        System.out.println("\n" + team2Name + " Players:");
        team2Players.displayPlayerName();

        TossThread toss = new TossThread(sc, team1, team2);
        toss.start();
        toss.join();
        Team BattingTeam = whoBat(sc, team1, team2, toss.getWinner());
        boolean isTeam1Batting = BattingTeam == team1;

        MaxOverByBowler=getValidChoice(sc,"Max over by singal Bowler",1,over/2);

        System.out.println("\n=== FIRST INNINGS START ===");
        if (isTeam1Batting) {
            team1Players.displayAvailablePlayersName();
        } else {
            team2Players.displayAvailablePlayersName();
        }
        Batsman1 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 1");
        updateBattingStatus(match.MatchId, Batsman1.getPlayerId(),"PLAYING");
        Batsman1.setPlaying(true);

        Batsman2 = getValidPlayer(sc, isTeam1Batting ? team1Players : team2Players, "opener 2");
        updateBattingStatus(match.MatchId, Batsman2.getPlayerId(),"PLAYING");
        Batsman2.setPlaying(true);
        Batsman2.setOnStrike(false);

        playInnings(sc, BattingTeam, isTeam1Batting, over, 1, NumberOfPlayer, 0, match.MatchId);

        int Target = BattingTeam.totalRun +1;
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

        playInnings(sc, BattingTeam, isTeam1Batting, over, 2, NumberOfPlayer, Target, match.MatchId);

        displayMatchResult(BattingTeam, NumberOfPlayer,match);
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