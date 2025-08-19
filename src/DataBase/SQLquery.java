package DataBase;

import DataStructure.LinkedListOfPlayer;
import Simulation.Match;
import Simulation.Team;

import java.sql.*;
import java.util.ArrayList;


import static Simulation.Team.getTeamById;

public class SQLquery {
    public static Connection getCon() throws SQLException {
        String url = "jdbc:mysql://localhost:8889/cricket Simulation";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    public static void insertTeam(int teamId, String name, String email) throws SQLException {
        String sql = "INSERT INTO Teams (team_id,team_name,Host_id) VALUES (?,?,(select user_id from Users where email=?))";

        try(Connection con = getCon();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setInt(1,teamId);
        ps.setString(2,name);
        ps.setString(3,email);
        ps.executeUpdate();
        }
    }

    public static void insertPlayer(int playerId,String name, String role, String teamName) throws SQLException {
        String sql = "INSERT INTO Players (player_id, player_name, role, team_id) VALUES" +
                " (?,?, ?, (SELECT team_id FROM Teams WHERE team_name = ?))";

        try(Connection con = getCon();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setString(2, name);
            ps.setString(3, role);
            ps.setString(4, teamName);
            ps.executeUpdate();
        }
    }
    public static void insertSchedule(int match_id,int team1Id,int team2Id,String emailId,int matchNumber,int over, String matchType, int typeId) throws SQLException {
        String sql = "INSERT INTO Matches(match_id ,Host_Id,team1_id,team2_id,Match_Number,inning_overs, match_type, match_type_id) VALUES" +
                " (?,(select user_id from Users where email=?),?,?,?,?,?,?)";

        try(Connection con = getCon();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, match_id);
            ps.setString(2, emailId);
            ps.setInt(3, team1Id);
            ps.setInt(4, team2Id);
            ps.setInt(5, matchNumber);
            ps.setInt(6, over);
            ps.setString(7, matchType);
            ps.setInt(8, typeId);
            ps.executeUpdate();
        }
    }

    public static void insertTournament(int tournamentId,String tournament_name,int year,String email) throws SQLException {
        String sql = "INSERT INTO Tournaments(tournament_name ,year,host_id, tournament_id) VALUES " +
                "(?,?,(select user_id from Users where email=?),?)";

        try(Connection con = getCon();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tournament_name);
            ps.setInt(2, year);
            ps.setString(3, email);
            ps.setInt(4, tournamentId);
            ps.executeUpdate();
        }
    }
    public static void insertIntoPointsTable(String tournament_name,int tourYear,int team_id, String groupName) throws SQLException {
        String sql = "INSERT INTO PointsTable(tournament_id ,team_id, group_name) VALUES " +
                "((SELECT tournament_id from Tournaments where (tournament_name=? and year=?) ),?,?)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tournament_name);
            ps.setInt(2, tourYear);
            ps.setInt(3, team_id);
            ps.setString(4, groupName);
            ps.executeUpdate();
        }
    }
    public static void insertTeamMatchStats(int match_id, int  team_id,int tournament_id) throws SQLException {
        String sql = "INSERT INTO TeamMatchStats(tournament_id, match_id, team_id) " +
                "VALUES (?, ?, ? ) ";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournament_id);
            ps.setInt(2, match_id);
            ps.setInt(3, team_id);
            ps.executeUpdate();
        }
    }

    public static void insertPlayerMatchStats(int match_id,int player_id) throws SQLException {
        String sql = "INSERT INTO PlayerMatchStats(match_id,player_id) " +
                "VALUES (?, ?)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, match_id);
            ps.setInt(2, player_id);
            ps.executeUpdate();
        }
    }

    public static void insertBallByBall(int match_id, int innings, int over_number, int ball_number,
                                        int striker_id, int non_striker_id, int bowler_id,
                                        int runs_batsman, int extras_runs,
                                        Integer out_player_id,String ball_summary) throws SQLException {

        String sql = "INSERT INTO ball_by_ball (match_id, innings, over_number, ball_number, striker_id, " +
                "non_striker_id, bowler_id, runs_batsman, extras_runs, out_player_id, ball_summary) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, match_id);
            ps.setInt(2, innings);
            ps.setInt(3, over_number);
            ps.setInt(4, ball_number);
            ps.setInt(5, striker_id);
            ps.setInt(6, non_striker_id);
            ps.setInt(7, bowler_id);
            ps.setInt(8, runs_batsman);
            ps.setInt(9, extras_runs);
            ps.setString(11, ball_summary);

            // Set NULL if no wicket (nullable out_player_id)
            if (out_player_id != null) {
                ps.setInt(10, out_player_id);
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
        }
    }


    public static void updateBattingStats(int matchId, int playerId, int runs, int ball, int four, int six) throws SQLException{
        String sql = "UPDATE PlayerMatchStats SET "
            + "runs_scored = ?,balls_faced = ?, "
            + "fours =  ?, sixes = ? "
            + "WHERE match_id = ? AND player_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, runs);
            ps.setInt(2, ball);
            ps.setInt(3, four);
            ps.setInt(4, six);
            ps.setInt(5, matchId);
            ps.setInt(6, playerId);
            ps.executeUpdate();
        }
}

    public static void updateBowlingStats(int matchId, int playerId, int wickets, double over, int runs_conceded) throws SQLException{
        String sql = "UPDATE PlayerMatchStats SET "
                + "wickets = ?,overs_bowled = ?, "
                + "runs_conceded = ? "
                + "WHERE match_id = ? AND player_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, wickets);
            ps.setDouble(2, over);
            ps.setInt(3, runs_conceded);
            ps.setInt(4, matchId);
            ps.setInt(5, playerId);

            ps.executeUpdate();
        }
    }


    public static void updateTeamBattingStats(int matchId, int teamId, int runs, int wicket, double overPlayed) throws SQLException{
        String sql = "UPDATE TeamMatchStats SET runs_scored = ?,wickets_lost = ?, "
                + "overs_played =  ? WHERE match_id = ? AND team_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, runs);
            ps.setInt(2, wicket);
            ps.setDouble(3, overPlayed);
            ps.setInt(4, matchId);
            ps.setInt(5, teamId);
            ps.executeUpdate();
        }
    }

    public static void updateBattingStatus(int match_id, int player_id, String status) throws SQLException {
        String sql = "UPDATE PlayerMatchStats SET BattingStats = ? WHERE match_id = ? AND player_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, match_id);
            ps.setInt(3, player_id);
            ps.executeUpdate();
        }
    }

    public static void updateMatchStatus(int match_id, String status) throws SQLException {
        String sql = "UPDATE Matches SET match_status = ? WHERE match_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, match_id);
            ps.executeUpdate();
        }
    }


    public static void updateWinner(int match_id, int winningTeamId) throws SQLException {
        String sql = "UPDATE Matches SET winner_team_id = ? WHERE match_id = ?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, winningTeamId);
            ps.setInt(2, match_id);
            ps.executeUpdate();
        }
    }

    public static void updateTeamMatchResult(int match_id, int teamId, String result) throws SQLException {
        String sql = "UPDATE TeamMatchStats SET result = ? WHERE match_id = ? and team_id=?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, result);
            ps.setInt(2, match_id);
            ps.setInt(3, teamId);
            ps.executeUpdate();
        }
    }

    public static void updateGroupName(int tournamentId,int teamId) throws SQLException {
        String sql = "UPDATE PointsTable SET group_name='Group2' WHERE team_id=? and tournament_id=?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, tournamentId);
            ps.executeUpdate();
        }
    }

    public static void updatePointsTable(int points,int matches_won,int matches_lost,int matches_drawn,double nrr,int teamId, int tournamentId) throws SQLException {
        String sql = "UPDATE PointsTable SET points =(points+?), matches_won=(matches_won+?), matches_lost=(matches_lost+?)" +
                ", matches_drawn=(matches_drawn+?), net_run_rate=(net_run_rate+?)  WHERE team_id=? and tournament_id=?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, points);
            ps.setInt(2, matches_won);
            ps.setInt(3, matches_lost);
            ps.setInt(4, matches_drawn);
            ps.setDouble(5, nrr);
            ps.setInt(6, teamId);
            ps.setInt(7, tournamentId);
            ps.executeUpdate();
        }
    }

    public static void updateBallByBallNoBall(int match_id,int inning,int over,int ball,int scoredRun,int extras) throws SQLException {
        String sql = "UPDATE ball_by_ball SET  runs_batsman = ?, extras_runs=? WHERE match_id = ? and over_number=?" +
                " and ball_number=? and innings=?";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, scoredRun);
            ps.setInt(2, extras);
            ps.setInt(3, match_id);
            ps.setInt(4, over);
            ps.setInt(5, ball);
            ps.setInt(6, inning);
            ps.executeUpdate();
        }
    }

    public static void updateBallByBallWicket(int match_id,int outPlayerId,int inning,int over,int ball,int scoredRun) throws SQLException {
        String sql = "UPDATE ball_by_ball SET  runs_batsman = ?,out_player_id=? WHERE " +
                "(match_id = ? and over_number=? and ball_number=? and innings=? and ball_summary='Wicket')";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, scoredRun);
            ps.setInt(2, outPlayerId);
            ps.setInt(3, match_id);
            ps.setInt(4, over);
            ps.setInt(5, ball);
            ps.setInt(6, inning);
            ps.executeUpdate();
        }
    }

    public static void undoLastBallOfMatchInning(int matchId, int innings) throws SQLException {
        String sql = "DELETE FROM ball_by_ball WHERE id = ( " +
                "SELECT id FROM (SELECT id FROM ball_by_ball " +
                "WHERE match_id = ? AND innings = ? " +
                "ORDER BY over_number DESC, ball_number DESC, timestamp DESC LIMIT 1) AS temp)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ps.setInt(2, innings);
            ps.executeUpdate();
        }
    }

    public static ArrayList<Team> getTeamData(String email) {
        ArrayList<Team> teams = new ArrayList<>();

        String sql = "SELECT DISTINCT t.team_id, t.team_name " +
                "FROM Matches m " +
                "JOIN Teams t ON (t.team_id = m.team1_id OR t.team_id = m.team2_id) " +
                "WHERE m.Host_Id = (SELECT user_id FROM Users WHERE email = ?)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Team teamFromDb = new Team(rs.getString("team_name"), rs.getInt("team_id"));
                teams.add(teamFromDb);
            }
            return teams;
        }
        catch (SQLException e){
            System.out.println("No data found for teams");
            return teams;
        }
    }

    public static ArrayList<Match> getSchedule(String email,ArrayList<Team> Teams) {

        ArrayList<Match> Schedule = new ArrayList<>();

        String sql = "SELECT M.match_id, M.team1_id, M.team2_id, M.inning_overs, M.match_status, M.match_type " +
                " FROM Matches M WHERE  M.Host_Id = (SELECT user_id FROM Users WHERE email = ? AND match_status <> 'COMPLETED')" +
                " ORDER BY M.Match_Number";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Match match = new Match(getTeamById(Teams, rs.getInt(2)), getTeamById(Teams, rs.getInt(3)), rs.getInt(1));
                match.setInningOvers(rs.getInt(4));
                match.setMatchStatus(rs.getString(5));
                match.setMatchType(rs.getString(6));
                Schedule.add(match);
            }
            return Schedule;
        }
        catch (SQLException e){
            System.out.println("No data found for schedule");
            return Schedule;
        }
    }

    public static LinkedListOfPlayer getPlayersForTeamsInRoleOrder(Team team) throws SQLException {
        LinkedListOfPlayer allPlayers = new LinkedListOfPlayer();

        String sql = "SELECT player_id, player_name, role " +
                "FROM Players " +
                "WHERE team_id = ? " +
                "ORDER BY FIELD(role, 'BATSMAN', 'ALLROUNDER', 'BOWLER')";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, team.getTeamId());
            ResultSet rs = ps.executeQuery();
            String name;
            int id;
            while (rs.next()) {
                name = rs.getString("player_name");
                id = rs.getInt("player_id");

                if (rs.getString("role").equalsIgnoreCase("BATSMAN"))
                    allPlayers.addBatsman(name, id);
                else if (rs.getString("role").equalsIgnoreCase("ALLROUNDER"))
                    allPlayers.addAllrounder(name, id);
                else
                    allPlayers.addBowler(name, id);
            }
            return allPlayers;
        }
    }

    public static ArrayList<Team> getTopTeams(int tournamentId, boolean groupMode) throws SQLException {
        ArrayList<Team> topTeams = new ArrayList<>();
        String sql;

        try (Connection con = getCon()) {
            if (groupMode) {
                sql = "SELECT team_id, team_name " +
                        "FROM PointsTable pt " +
                        "JOIN Teams t ON pt.team_id = t.team_id " +
                        "WHERE pt.tournament_id = ? AND pt.group_name = ? " +
                        "ORDER BY pt.points DESC, pt.net_run_rate DESC LIMIT 2";


                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, tournamentId);
                    ps.setString(2, "A");  // Group A
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        topTeams.add(new Team(rs.getString("team_name"), rs.getInt("team_id")));
                    }
                }

                // Group B
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, tournamentId);
                    ps.setString(2, "B");  // Group B
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        topTeams.add(new Team(rs.getString("team_name"), rs.getInt("team_id")));
                    }
                }

            } else {
                sql = "SELECT team_id, team_name " +
                        "FROM PointsTable pt " +
                        "JOIN Teams t ON pt.team_id = t.team_id " +
                        "WHERE pt.tournament_id = ? " +
                        "ORDER BY pt.points DESC, pt.net_run_rate DESC LIMIT 4";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, tournamentId);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        topTeams.add(new Team(rs.getString("team_name"), rs.getInt("team_id")));
                    }
                }
            }
        }
        return topTeams;
    }


    public static int getTournamentId(String email) throws SQLException {
        String sql = "SELECT tournament_id FROM Tournaments WHERE host_id=(select user_id from Users where email=?)";

        try(Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getInt("tournament_id");
        }

    }
}
