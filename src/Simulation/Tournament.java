package Simulation;

import java.util.*;
import java.util.ArrayList;
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter 1 for Single Match");
        System.out.println("Enter 2 for Series");
        System.out.println("Enter 3 for Tournament");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 :
                TwoTeamsMatches(sc, 1);
                break;

            case 2 :
                System.out.print("Enter number of matches (2 or 5): ");
                int n = sc.nextInt();
                TwoTeamsMatches(sc, n);
            break;

            case 3 :
                System.out.println("Enter number of teams (even number for group, min 3, max 12): ");
                int num = sc.nextInt();
                sc.nextLine();
                for (int i = 0; i < num; i++) {
                    System.out.print("Enter team " + (i + 1) + " name: ");
                    Teams.add(new Team(sc.nextLine()));
                }

                if (num % 2 == 1) {
                    generateNonGroup(num);
                }
                else {
                    System.out.println("Enter 1 for non group tournament");
                    System.out.println("Enter 2 for group tournament");
                    int TournamentType=sc.nextInt();
                    if (TournamentType==1){
                        generateNonGroup(num);
                    } else if (TournamentType==2) {
                        generateGroups(num);
                        System.out.println("Group 1: " + Group1);
                        System.out.println("Group 2: " + Group2);

                        System.out.println("Choose Schedule Type:");
                        System.out.println("1. Same Group Matches Only (G-1)");
                        System.out.println("2. Other Group Matches Only");
                        System.out.println("3. 1 match with group and 2 match with other group");
                        System.out.println("4. 2 match with group and 1 match with other group");
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
                                sameGroupMatches(Group1, Group2);
                                crossGroupMatches(Group2, Group1);
                                break;
                            case 4:
                                sameGroupMatches(Group1, Group2);
                                crossGroupMatches(Group1, Group2);
                                sameGroupMatches(Group2, Group1);
                                break;
                            default:
                                System.out.println("Invalid option selected");
                                break;
                        }
                    }

                }

            }

        System.out.println("\nFinal Match Schedule:");
        printSchedule();
    }

    static void TwoTeamsMatches(Scanner sc, int matches) {
        System.out.print("Enter Team 1 name: ");
        Team team1 = new Team(sc.nextLine());
        System.out.print("Enter Team 2 name: ");
        Team team2 = new Team(sc.nextLine());

        for (int i = 0; i < matches; i++) {
            Schedule.add(new Match(team1, team2));
        }
    }

    static void generateNonGroup(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Schedule.add(new Match(Teams.get(i), Teams.get(j)));
            }
        }
    }

    static void generateGroups(int totalTeams) {
        Collections.shuffle(Teams);
        int mid = totalTeams / 2;
        Group1.addAll(Teams.subList(0, mid));
        Group2.addAll(Teams.subList(mid, totalTeams));
    }

    static void sameGroupMatches(ArrayList<Team> group1,ArrayList<Team> group2) {
        int size = group1.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Schedule.add(new Match(group1.get(i), group1.get(j)));
                Schedule.add(new Match(group2.get(i), group2.get(j)));
            }
        }
    }

    static void crossGroupMatches(ArrayList<Team> group1,ArrayList<Team> group2) {
        for (Team t1 : group1) {
            for (Team t2 : group2) {
                Schedule.add(new Match(t1, t2));
            }
        }
        int groupSize=group1.size();
        int index=0;
        ArrayList<Match> temp=new ArrayList<>();
        while(Schedule.size()!=temp.size()) {
            temp.add(Schedule.get(index));
            index=(index+groupSize+1)%Schedule.size();
        }
            Schedule.clear();
        Schedule.addAll(temp);
        temp.clear();
    }

    static void printSchedule() {
        System.out.println("\n--- Final Tournament Schedule ---");
        int matchNo = 1;
        for (Match m : Schedule) {
            System.out.println("Match " + matchNo++ + ": " + m.team1.getTeamName() + " vs " + m.team2.getTeamName());
        }
    }

}
