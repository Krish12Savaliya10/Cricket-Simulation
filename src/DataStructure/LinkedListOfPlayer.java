package DataStructure;

public class LinkedListOfPlayer {


    // Base Player class with common batting fields
    public  static class Player{
        protected String playerName;
        protected boolean onStrike;
        protected boolean isOut;
        protected boolean isPlaying;


        // Batting stats (common to all players)
        protected int runsScored;
        protected int ballsFaced;
        protected int sixes;
        protected int fours;

        // Bowling-specific stats
        protected int oversBowled;
        protected int wickets;
        protected int runsGiven;

        protected Player next;

        public  Player(String name) {
            this.playerName = name;
            this.onStrike = true;
            this.isOut = false;
            this.isPlaying=false;
            this.next = null;
            this.runsScored = 0;
            this.ballsFaced = 0;
            this.sixes = 0;
            this.fours = 0;
        }
        public  Player(Player Copy) {
            this.playerName = Copy.playerName;
            this.onStrike = Copy.onStrike;
            this.isOut = Copy.isOut;
            this.isPlaying=Copy.isPlaying;
            this.runsScored = Copy.runsScored;
            this.ballsFaced = Copy.ballsFaced;
            this.sixes = Copy.sixes;
            this.fours = Copy.fours;
        }

        public String getPlayerName() {return playerName;}

        public boolean isOnStrike() {
            return onStrike;
        }

        public void setOnStrike(boolean onStrike) {
            this.onStrike = onStrike;
        }

        public boolean isOut() {
            return isOut;
        }

        public void setOut(boolean out) {
            isOut = out;
        }

        public void setNext(Player next) {
            this.next = next;
        }

        public int getRunsScored() {
            return runsScored;
        }

        public void setRunsScored(int runsScored) {
            this.runsScored = runsScored;
        }

        public int getBallsFaced() {
            return ballsFaced;
        }

        public void setBallsFaced(int ballsFaced) {
            this.ballsFaced = ballsFaced;
        }

        public int getSixes() {
            return sixes;
        }

        public void setSixes(int sixes) {
            this.sixes = sixes;
        }

        public int getFours() {return fours;}

        public void setFours(int fours) {
            this.fours = fours;
        }

        public int getOversBowled() {return oversBowled;}

        public void setOversBowled(int oversBowled) {this.oversBowled = oversBowled;}

        public int getWickets() {return wickets;}

        public void setWickets(int wickets) {this.wickets = wickets;}

        public int getRunsGiven() {return runsGiven;}

        public void setRunsGiven(int runsGiven) {this.runsGiven = runsGiven;}
        public boolean isPlaying() {return isPlaying;}

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public  void displayStats(){}


    }

    // Batsman specialization (primarily a batter)
    public static class Batsman extends Player {
        public Batsman(String name) {
            super(name);
        }
        public Batsman(Batsman copy){
            super(copy);
        }

        @Override
        public void displayStats() {
            System.out.println("Batsman: " + playerName);
            System.out.println("Runs: " + runsScored + " Balls: " + ballsFaced);
            System.out.println("4s: " + fours + " 6s: " + sixes);
            System.out.println("Strike Rate: " +
                    (ballsFaced > 0 ? String.format("%.2f", (runsScored * 100.0) / ballsFaced) : "N/A"));
        }
    }

    // Bowler specialization (can bat too)
    public static class Bowler extends Player {
        public Bowler(Bowler copy){
            super(copy);
            this.oversBowled=copy.oversBowled;
            this.wickets=copy.wickets;
            this.runsGiven=copy.runsGiven;
        }
        public Bowler(String name) {
            super(name);
        }


        @Override
        public void displayStats() {
            System.out.println("Bowler: " + playerName);
            // Batting stats
            System.out.println("[Batting] Runs: " + runsScored + " Balls: " + ballsFaced);
            // Bowling stats
            System.out.println("[Bowling] Overs: " + oversBowled);
            System.out.println("Wickets: " + wickets + " Runs Given: " + runsGiven);
            System.out.println("Economy: " +
                    (oversBowled > 0 ? String.format("%.2f", runsGiven / (double) oversBowled) : "N/A"));
        }
    }

    // All-rounder specialization (both batting and bowling)
    public static class AllRounder extends Bowler {
        public AllRounder(String name) {
            super(name);
        }
        public AllRounder(AllRounder copy){
            super(copy);
        }


        @Override
        public void displayStats() {
            System.out.println("AllRounder: " + playerName);
            System.out.println("Runs: " + runsScored + " Balls: " + ballsFaced);
            System.out.println("4s: " + fours + " 6s: " + sixes);
            System.out.println("Strike Rate: " +
                    (ballsFaced > 0 ? String.format("%.2f", (runsScored * 100.0) / ballsFaced) : "N/A"));
            System.out.println("[Bowling] Overs: " + oversBowled);
            System.out.println("Wickets: " + wickets + " Runs Given: " + runsGiven);
            System.out.println("Economy: " +
                    (oversBowled > 0 ? String.format("%.2f", runsGiven / (double) oversBowled) : "N/A"));
        }
    }

    // LinkedList implementation
    private Player head;

    public void addBatsman(String name) {
        Batsman n = new Batsman(name);
        if (head == null) {
            head = n;
        } else {
            Player temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next=n;
        }
    }

    public void addBowler(String name) {
        Bowler n = new Bowler(name);
        if (head == null) {
            head = n;
        } else {
            Player temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next=n;
        }

    }

    public void addAllrounder(String name) {
        AllRounder n = new AllRounder(name);
        if (head == null) {
            head = n;
        } else {
            Player temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next=n;
        }
    }

    public void displayPlayerName() {
        Player temp = head;
        while (temp != null) {
            System.out.println(temp.playerName);
            temp = temp.next;
        }
    }

    public Player getPlayer(String name) {
        Player temp = head;
        while (temp != null && (!temp.playerName.equalsIgnoreCase(name))) {
            temp = temp.next;
        }
        return temp;
    }

    public void displayAvailablePlayersName() {
        Player temp = head;
        while (temp != null) {
            if(!temp.isOut() && !temp.isPlaying){
                System.out.print(temp.getPlayerName());
                if(temp instanceof Batsman)
                    System.out.println(" (Batsman)");
                else {
                    System.out.println( (temp instanceof AllRounder ? " (All-Rounder)" : " (Bowler)"));;
                }
            }
            temp = temp.next;
        }
    }
    public void displayBowlersAndAllRoundersName(Bowler bowler,int Max) {
        System.out.println("Available bowlers/all-rounders:");
        Player current = head;
        while (current != null) {
            if (current instanceof Bowler && current!=bowler && current.getOversBowled()<=Max) {
                System.out.print(current.getPlayerName() +
                        (current instanceof AllRounder ? " (All-Rounder)" : " (Bowler)"));
                System.out.println(" over remaing ("+(Max-current.getOversBowled())+")");
            }
            current = current.next;
        }
    }
    public void displayStats(String Name){
        Player temp = head;
        while (temp!=null && temp.playerName.equalsIgnoreCase(Name)){
            temp=temp.next;
        }
        if(temp!=null)
            temp.displayStats();
        else
            System.out.println(Name+" not found!");

    }

}