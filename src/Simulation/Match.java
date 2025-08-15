package Simulation;

public class Match {
    Team team1;
    Team team2;
    int MatchId;
    int inningOvers;
    String matchStatus;


    public Match(Team team1, Team team2, int MatchId) {
        this.team1 = team1;
        this.team2 = team2;
        this.MatchId=MatchId;
    }

    public void setInningOvers(int inningOvers) {
        this.inningOvers = inningOvers;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    @Override
    public String toString() {
        return team1 + " vs " + team2;
    }


}
