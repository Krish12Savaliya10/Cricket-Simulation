package Simulation;

public class Tournament {
    public static void main(String[] args) {
//flow of code

// Main menu
        System.out.println("Enter 1 for Series or Single Match");
        System.out.println("Enter 2 for Tournament");

// If input is 1
        System.out.println("Series or Single Match");
        System.out.println("Enter number of matches (between 1 to 5)");
        System.out.println("Generate schedule.");
        //return

// If input is 2
        System.out.println("Tournament Mode");
        System.out.println("Enter number of teams");

// Tournament match type choice
        System.out.println("1 Each team plays with all other teams");
        System.out.println("2 Group match tournament");

// If choice is 1
        System.out.println("Each team will play (n - 1) matches");

// If choice is 2 (group match format)
        System.out.println("Number of matches ÷ 2 = Number of groups (g)");
        System.out.println("Choose group tournament format:");
        System.out.println("1 G-1 format play with group only");
        System.out.println("2 3G-1 format 2 matches with other group and 1 match with group");
        System.out.println("3 3G-2 format 1 match with other group and 2 matches with group");

        System.out.println("Generate schedule.");
        System.out.println("Manage point table");
        System.out.println("Playoff");

        //setup 1
        System.out.println("1st vs 4th  → Semi Final 1");
        System.out.println("2nd vs 3rd  → Semi Final 2");
        System.out.println("Winners of both semi-finals will play the Final");

        //setup 2
        System.out.println("Top 2 teams play in Semi Final 1.");
        System.out.println("Winner of Semi Final 1 directly qualifies for the Final.");
        System.out.println("3rd and 4th place teams play an Eliminator match.");
        System.out.println("Winner of Eliminator plays the loser of Semi Final 1 in Semi Final 2.");
        System.out.println("Winner of Semi Final 2 qualifies for the Final.");


    }
}
