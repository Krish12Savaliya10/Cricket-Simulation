package Simulation;

import DataStructure.LinkedListOfPlayer;

public class Team {
    public LinkedListOfPlayer players;
    int totalRun;
    int overPlayed;
    int wicketDown;
    int Target;
    String teamName;
    int win;
    int lose;
    int points;
    int matchPlayed;
    double NRR;
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
    public void setDefault(Team team){
        team.totalRun = 0;
        team.overPlayed = 0;
        team.wicketDown = 0;
        team.won = false;
        team.freeHitActive = false;
        team.Target=0;
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

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
