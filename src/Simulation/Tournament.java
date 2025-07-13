package Simulation;

import DataStructure.LinkedListOfPlayer;

import java.util.*;
import java.util.ArrayList;

import static Simulation.MatchSimulation.getValidCount;
import static Simulation.MatchSimulation.getValidName;

class Match {
    Team team1;
    Team team2;
    int points;
    int wins;
    int losses;
    double NRR;

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        this.points = 0;
        this.wins = 0;
        this.losses = 0;
        this.NRR = 0.0;
    }

    @Override
    public String toString() {
        return team1 + " vs " + team2;
    }


}

public class Tournament {

    static ArrayList<Match> Schedule = new ArrayList<>();
    static ArrayList<Team> Teams = new ArrayList<>();
    static ArrayList<Team> Group1 = new ArrayList<>();
    static ArrayList<Team> Group2 = new ArrayList<>();
    static HashMap<Team, LinkedListOfPlayer> TeamSet=new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter 1 for Single Match");
        System.out.println("Enter 2 for Series");
        System.out.println("Enter 3 for Tournament");
        int choice = sc.nextInt();
        System.out.println("Enter number of player player in match per team (6 to 11)");
        int playerCount=sc.nextInt();
        int backup=0;
        if(choice!=1){
        System.out.println("Enter number of Backup player (0 to 3)");
        backup=sc.nextInt();}
        sc.nextLine();

        switch (choice) {
            case 1 :
                TwoTeamsMatches(sc, 1,(playerCount+backup));
                printSchedule();
                break;

            case 2 :
                System.out.print("Enter number of matches (2 to 5): ");
                int n = sc.nextInt();
                sc.nextLine();
                TwoTeamsMatches(sc, n,(playerCount+backup));
                printSchedule();
                break;

            case 3 :
                System.out.println("Enter number of teams (even number for group, min 6, max 12): ");
                int num = sc.nextInt();
                sc.nextLine();
                boolean isGroup=false;
                String teamName;
                Team addTeam;
                for (int i = 0; i < num; i++) {
                    System.out.print("Enter team " + (i + 1) + " name: ");
                    teamName=sc.nextLine().toUpperCase();
                    addTeam=new Team(teamName);
                    Teams.add(addTeam);
                    TeamSet.put(addTeam,new LinkedListOfPlayer());
                    inputPlayers(sc,TeamSet.get(addTeam),teamName,(playerCount+backup));

                }

                    System.out.println("Enter 1 for non group tournament "+num*(num-1)/2+" matches");
                    System.out.println("Enter 2 for group tournament");
                    int TournamentType=sc.nextInt();
                    generateGroups(num);
                    if (TournamentType==1){
                        crossGroupMatches(Group1,Group2);
                        sameGroupMatches(Group2,Group1);
                        printSchedule();
                    } else if (TournamentType==2) {
                        isGroup=true;
                        System.out.println("Group 1: " + Group1);
                        System.out.println("Group 2: " + Group2);
                        System.out.println("Choose Schedule Type:");
                        System.out.println("1. Same Group Matches Only " + (Group1.size()*(Group1.size()-1)) + " Matches"); // 20
                        System.out.println("2. Other Group Matches Only " + (Group1.size()*Group1.size()) + " Matches"); // 25
                        System.out.println("3. 1 match with group and 2 match with other group " + (Group1.size()*(Group1.size()-1) + 2*(Group1.size()*Group1.size())) + " Matches"); // 60
                        System.out.println("4. 2 match with group and 1 match with other group " + (2*(Group1.size()*(Group1.size()-1)) + (Group1.size()*Group1.size())) + " Matches"); // 45
                        System.out.println("5. Play with all other teams twice " + (2*( (Group1.size()*(Group1.size()-1)) + (Group1.size()*Group1.size()) )) + " Matches"); // 70
                        int opt = sc.nextInt();

                        switch (opt) {
                            case 1:
                                sameGroupMatches(Group1, Group2);
                                break;
                            case 2:
                                crossGroupMatches(Group1, Group2);
                                break;
                            case 3:
                                crossGroupMatches(Group1, Group2);
                                Shuffle();
                                sameGroupMatches(Group1, Group2);
                                Shuffle();
                                crossGroupMatches(Group2, Group1);
                                break;
                            case 4:
                                sameGroupMatches(Group1, Group2);
                                Shuffle();
                                crossGroupMatches(Group1, Group2);
                                Shuffle();
                                sameGroupMatches(Group2, Group1);
                                break;
                            case 5:
                                sameGroupMatches(Group1,Group2);
                                Shuffle();
                                crossGroupMatches(Group2,Group1);
                                Shuffle();
                                sameGroupMatches(Group2,Group1);
                                Shuffle();
                                crossGroupMatches(Group1,Group2);
                                break;

                            default:
                                System.out.println("Invalid option selected");
                                break;
                        }
                        printSchedule();

                }
                System.out.println("Point table option");
                if(isGroup){
                    System.out.println("Enter 1 for group points table");
                    System.out.println("Enter 2 for single points table");
                    int PointsTableChoice=sc.nextInt();
                    if(PointsTableChoice==1){
                        singlePointsTable(sc);
                    }
                    else{
                        System.out.println("add after pointsTable database");
                    }
                }
                else{
                    System.out.println("add after pointsTable database");
                }

        }
        while(!Schedule.isEmpty()){

        }
    }

     static void TwoTeamsMatches(Scanner sc, int matches,int numberOfPlayer) {
        System.out.print("Enter Team 1 name: ");
        String team1name=sc.nextLine();
        Team team1 = new Team(team1name);
         System.out.print("Enter Team 2 name: ");
         String team2name=sc.nextLine();
         Team team2 = new Team(team2name);
         TeamSet.put(team1,new LinkedListOfPlayer());
         inputPlayers(sc,TeamSet.get(team1),team1name,numberOfPlayer);
         TeamSet.put(team2,new LinkedListOfPlayer());
         inputPlayers(sc,TeamSet.get(team2),team2name,numberOfPlayer);


         for (int i = 0; i < matches; i++) {
            Schedule.add(new Match(team1, team2));
        }
    }
    static void Shuffle(){
        Collections.shuffle(Group1);
        Collections.shuffle(Group2);
    }

    static void generateGroups(int totalTeams) {
        Collections.shuffle(Teams);
        int mid = totalTeams / 2;
        Group1.addAll(Teams.subList(0, mid));
        Group2.addAll(Teams.subList(mid, totalTeams));
    }

    static void sameGroupMatches(ArrayList<Team> group1,ArrayList<Team> group2) {
        ArrayList<Match> Team1Schedule=new ArrayList<>();
        ArrayList<Match> Team2Schedule=new ArrayList<>();
        int size = group1.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if((int)(Math.random()*2+1)%2==1) {
                    Team1Schedule.add(new Match(group1.get(i), group1.get(j)));
                    Team2Schedule.add(new Match(group2.get(i), group2.get(j)));
                }
                else{
                    Team1Schedule.add(new Match(group1.get(j), group1.get(i)));
                    Team2Schedule.add(new Match(group2.get(j), group2.get(i)));
                }
            }
        }
        Collections.shuffle(Team1Schedule);
        Collections.reverse(Team1Schedule);
        Collections.shuffle(Team2Schedule);
        Collections.reverse(Team2Schedule);
        while(!Team1Schedule.isEmpty()){
            Schedule.add(Team1Schedule.removeFirst());
            Schedule.add(Team2Schedule.removeFirst());
        }


    }


    static void crossGroupMatches(ArrayList<Team> group1,ArrayList<Team> group2) {
        for(int i=0;i<group1.size();i++){
            if(i!=0) {
                group2.addLast(group2.removeFirst());
            }
            for(int j=0;j<group2.size();j++){
                if((int)(Math.random()*2+1)%2==1)
                    Schedule.add(new Match(group1.get(j), group2.get(j)));
                else
                    Schedule.add(new Match(group2.get(j), group1.get(j)));

            }
        }

    }

    static void printSchedule() {
        System.out.println("\n--- Final Tournament Schedule ---");
        int matchNo = 1;
        for (Match m : Schedule) {
            System.out.println("Match " + matchNo++ + ": " + m.team1.getTeamName() + " vs " + m.team2.getTeamName());
        }
    }

    static void singlePointsTable(Scanner sc) {
        System.out.println("Enter 1 for IPL type playoff");
        System.out.println("Enter 2 for icc type playoff");
        int playoffChoice = sc.nextInt();
        if (playoffChoice == 1) {
            //team according to points table(from database)
            System.out.println("add semi final 1(1 vs 2)[winner{F1} in final]/[lost{S1} in semi final 2]");
            System.out.println("add eliminator (3 vs 4)[winner{S1} in semi final 2]");
            System.out.println("add semi final 2(S1 vs S2)[winner{F2} in final");
            System.out.println("add final(F1 vs F2)");
        } else {
            System.out.println("add semi final 1(1 vs 4)[winner{F1} in final]");
            System.out.println("add semi final 2(2 vs 3)[winner{F2} in final");
            System.out.println("add final(F1 vs F2)");
        }
    }
    static void groupPointsTable() {
        //teams according to both group points table(from database)
        System.out.println("add demi final 1(Group1 1st vs Group2 2nd)[winner{F1} in final");
        System.out.println("add demi final 2(Group2 1st vs Group1 2nd)[winner{F2} in final");
        System.out.println("add final(F1 vs F2)");
    }
    static void inputPlayers(Scanner sc, LinkedListOfPlayer teamPlayers, String teamName, int NumberOfPlayer) {
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
}