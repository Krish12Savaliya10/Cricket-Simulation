package Simulation;

public class Match {
    Team team1;
    Team team2;
    int MatchId;


    public Match(Team team1, Team team2, int MatchId) {
        this.team1 = team1;
        this.team2 = team2;
        this.MatchId=MatchId;
    }

    @Override
    public String toString() {
        return team1 + " vs " + team2;
    }


}
