package Simulation;

import DataStructure.LinkedListOfPlayer;

import java.util.ArrayList;

public class Team {
    public LinkedListOfPlayer players;
    int totalRun;
    double overPlayed;
    int wicketDown;
    int Target;
    String teamName;
    int win;
    int lose;
    int points;
    int matchPlayed;
    int teamId;
    boolean won;
    boolean freeHitActive;

    public Team(String teamName,int teamId) {
        this.teamName = teamName;
        this.teamId=teamId;
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
    public void setDefault(){
        this.totalRun = 0;
        this.overPlayed = 0;
        this.wicketDown = 0;
        this.won = false;
        this.freeHitActive = false;
        this.Target=0;
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

    public int getTeamId() {return teamId;}

    public void setTeamId(int teamId) {this.teamId = teamId;}

    public double getOverPlayed() {
        return overPlayed;
    }

    public void setOverPlayed(double overPlayed) {
        this.overPlayed = overPlayed;
    }

    public static Team getTeamById(ArrayList<Team> teams, int teamId) {
        for (Team team : teams) {
            if (team.getTeamId() == teamId) {
                return team;
            }
        }
        return null;
    }
}
