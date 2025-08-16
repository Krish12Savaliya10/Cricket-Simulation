package Simulation;

public class Match {
    Team team1;
    Team team2;
    int MatchId;
    int inningOvers;
    String matchStatus;
    String matchType;


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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    @Override
    public String toString() {
        return team1 + " vs " + team2;
    }


}
