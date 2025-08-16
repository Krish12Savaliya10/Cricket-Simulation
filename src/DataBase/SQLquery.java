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

    //Insert user: INSERT INTO Users (full_name, email, password, role) VALUES (?, ?, ?, ?)

    //Login: SELECT * FROM Users WHERE email = ? AND password = ?


    //insert team: INSERT INTO Users (team_name) VALUES (name)
    public static void insertTeam(int teamId, String name, String email) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Teams (team_id,team_name,Host_id) VALUES (?,?,(select user_id from Users where email=?))";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,teamId);
        ps.setString(2,name);
        ps.setString(2,email);
        ps.executeUpdate();
    }

    public static void insertPlayer(int playerId,String name, String role, String teamName) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Players (player_id, player_name, role, team_id) VALUES (?,?, ?, (SELECT team_id FROM Teams WHERE team_name = ?))";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,playerId);
        ps.setString(2, name);
        ps.setString(3, role);
        ps.setString(4, teamName);
        ps.executeUpdate();
    }
    public static void insertSchedule(int match_id,int team1Id,int team2Id,String emailId,int matchNumber,int over, String matchType) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Matches(match_id ,Host_Id,team1_id,team2_id,Match_Number,inning_overs, match_type) VALUES (?,(select user_id from Users where email=?),?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, match_id);
        ps.setString(2, emailId);
        ps.setInt(3, team1Id );
        ps.setInt(4, team2Id);
        ps.setInt(5, matchNumber);
        ps.setInt(6, over);
        ps.setString(7,matchType);
        ps.executeUpdate();
    }

    public static void insertTournament(String tournament_name,int year,String email) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Tournaments(tournament_name ,year,host_id) VALUES (?,?,(select user_id from Users where email=?))";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, year );
        ps.setString(3, email);
        ps.executeUpdate();
    }
    public static void insertIntoPointsTable(String tournament_name,int tourYear,int team_id) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO PointsTable(tournament_id ,team_id) VALUES ((SELECT tournament_id from Tournaments where (tournament_name=? and year=?) ),?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, tourYear );
        ps.setInt(3, team_id);
        ps.executeUpdate();
    }
    public static void insertTeamMatchStats(int match_id, int  team_id, int tourYear, String tournament_name,String email) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO TeamMatchStats(tournament_id, match_id, team_id) " +
                "VALUES ((SELECT tournament_id FROM Tournaments WHERE tournament_name = ? AND year = ? AND host_id=(SELECT user_id from Users where email=?)), ?, ? ) ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, tourYear);
        ps.setString(3, email);
        ps.setInt(4, match_id);
        ps.setInt(5, team_id);
        ps.executeUpdate();
    }

    public static void insertPlayerMatchStats(int match_id,int player_id) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO PlayerMatchStats(match_id,player_id) " +
                "VALUES (?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, match_id);
        ps.setInt(2,player_id );
        ps.executeUpdate();
    }

    public static void insertBallByBall(int match_id, int innings, int over_number, int ball_number,
                                        int striker_id, int non_striker_id, int bowler_id,
                                        int runs_batsman, int extras_runs,
                                        Integer out_player_id,String ball_summary) throws SQLException {

        Connection con = getCon(); // Your method to get DB connection
        String sql = "INSERT INTO ball_by_ball (match_id, innings, over_number, ball_number, striker_id, " +
                "non_striker_id, bowler_id, runs_batsman, extras_runs, out_player_id, ball_summary) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
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
        ps.close();
    }


    public static void updateBattingStats(int matchId, int playerId, int runs, int ball, int four, int six) throws SQLException{
        Connection con = getCon();
        String sql = "UPDATE PlayerMatchStats SET "
            + "runs_scored = ?,balls_faced = ?, "
            + "fours =  ?, sixes = ? "
            + "WHERE match_id = ? AND player_id = ?";
    PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, runs);
        pst.setInt(2, ball);
        pst.setInt(3, four);
        pst.setInt(4, six);
        pst.setInt(5, matchId);
        pst.setInt(6, playerId);
        pst.executeUpdate();
}

    public static void updateBowlingStats(int matchId, int playerId, int wickets, double over, int runs_conceded) throws SQLException{
        Connection con = getCon();
        String sql = "UPDATE PlayerMatchStats SET "
                + "wickets = ?,overs_bowled = ?, "
                + "runs_conceded = ? "
                + "WHERE match_id = ? AND player_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, wickets);
        pst.setDouble(2, over);
        pst.setInt(3, runs_conceded);
        pst.setInt(4, matchId);
        pst.setInt(5, playerId);

        pst.executeUpdate();
    }


    public static void updateTeamBattingStats(int matchId, int teamId, int runs, int wicket, double overPlayed) throws SQLException{
        Connection con = getCon();
        String sql = "UPDATE TeamMatchStats SET "
                + "runs_scored = ?,wickets_lost = ?, "
                + "overs_played =  ?"
                + "WHERE match_id = ? AND team_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, runs);
        pst.setInt(2, wicket);
        pst.setDouble(3, overPlayed);
        pst.setInt(4, matchId);
        pst.setInt(5, teamId);
        pst.executeUpdate();
    }

    public static void updateBattingStatus(int match_id, int player_id, String status) throws SQLException {
        Connection con = getCon();
        String sql = "UPDATE PlayerMatchStats SET BattingStats = ? WHERE match_id = ? AND player_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, match_id);
        ps.setInt(3, player_id);
        ps.executeUpdate();
    }

    public static void updateMatchStatus(int match_id, String status) throws SQLException {
        Connection con = getCon();
        String sql = "UPDATE Matches SET match_status = ? WHERE match_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, match_id);
        ps.executeUpdate();
    }


    public static void updateWinner(int match_id, int winningTeamId) throws SQLException {
        Connection con = getCon();
        String sql = "UPDATE Matches SET winner_team_id = ? WHERE match_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, winningTeamId);
        ps.setInt(2, match_id);
        ps.executeUpdate();
    }

    public static void updateBallByBallNoBall(int match_id,int inning,int over,int ball,int scoredRun,int extras) throws SQLException {
        Connection con = getCon();
        String sql = "UPDATE ball_by_ball SET  runs_batsman = ?, extras_runs=? WHERE match_id = ? and over_number=? and ball_number=? and innings=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, scoredRun);
        ps.setInt(2, extras);
        ps.setInt(3,match_id);
        ps.setInt(4,over);
        ps.setInt(5,ball);
        ps.setInt(6,inning);
        ps.executeUpdate();
    }

    public static void updateBallByBallWicket(int match_id,int outPlayerId,int inning,int over,int ball,int scoredRun) throws SQLException {
        Connection con = getCon();
        String sql = "UPDATE ball_by_ball SET  runs_batsman = ?,out_player_id=? WHERE (match_id = ? and over_number=? and ball_number=? and innings=? and ball_summary='Wicket')";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, scoredRun);
        ps.setInt(2, outPlayerId);
        ps.setInt(3,match_id);
        ps.setInt(4,over);
        ps.setInt(5,ball);
        ps.setInt(6,inning);
        ps.executeUpdate();
    }

    public static void undoLastBallOfMatchInning(int matchId, int innings) throws SQLException {
        Connection con = getCon();
        String sql = "DELETE FROM ball_by_ball WHERE id = ( " +
                "SELECT id FROM (SELECT id FROM ball_by_ball " +
                "WHERE match_id = ? AND innings = ? " +
                "ORDER BY over_number DESC, ball_number DESC, timestamp DESC LIMIT 1) AS temp)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, matchId);
        ps.setInt(2, innings);
        ps.executeUpdate();
        ps.close();
    }

    public static ArrayList<Team> getTeamData(String email) throws SQLException {
        Connection con = getCon();
        ArrayList<Team> teams = new ArrayList<>();

        String sql = "SELECT DISTINCT t.team_id, t.team_name " +
                "FROM Matches m " +
                "JOIN Teams t ON (t.team_id = m.team1_id OR t.team_id = m.team2_id) " +
                "WHERE m.Host_Id = (SELECT user_id FROM Users WHERE email = ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Team teamFromDb = new Team(rs.getString("team_name"), rs.getInt("team_id"));
            teams.add(teamFromDb);
        }

        rs.close();
        ps.close();
        return teams;
    }

    public static ArrayList<Match> getSchedule(String email,ArrayList<Team> Teams) throws SQLException {
        //tournament.admin@cricket.org
        //Cricket@2025
        Connection con = getCon();
        ArrayList<Match> Schedule = new ArrayList<>();

        String sql = "SELECT M.match_id, M.team1_id, M.team2_id, M.inning_overs, M.match_status " +
                " FROM Matches M WHERE  M.Host_Id = (SELECT user_id FROM Users WHERE email = ? AND match_status <> 'COMPLETED')" +
                " ORDER BY M.Match_Number";


        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Match match= new Match(getTeamById(Teams,rs.getInt(2)), getTeamById(Teams,rs.getInt(3)), rs.getInt(1));
            match.setInningOvers(rs.getInt(4));
            match.setMatchStatus(rs.getString(5));
            Schedule.add(match);
        }

        rs.close();
        ps.close();
        return Schedule;
    }

    public static LinkedListOfPlayer getPlayersForTeamsInRoleOrder(Team team) throws SQLException {
        Connection con = getCon();
        LinkedListOfPlayer allPlayers = new LinkedListOfPlayer();

        String sql = "SELECT player_id, player_name, role " +
                "FROM Players " +
                "WHERE team_id = ? " +
                "ORDER BY FIELD(role, 'BATSMAN', 'ALLROUNDER', 'BOWLER')";

        PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, team.getTeamId());
            ResultSet rs = ps.executeQuery();
            String name;
            int id;
            while (rs.next()) {
                name=rs.getString("player_name");
                id=rs.getInt("player_id");

                if(rs.getString("role").equalsIgnoreCase("BATSMAN"))
                    allPlayers.addBatsman(name,id);
                else if (rs.getString("role").equalsIgnoreCase("ALLROUNDER"))
                    allPlayers.addAllrounder(name,id);
                else
                    allPlayers.addBowler(name,id);
            }
            rs.close();
        ps.close();
        return allPlayers;
    }

    public static void displayScorecardFromDB(int matchId) throws SQLException {
        Connection con = getCon();

        int inning = getCurrentInningFromDB(con, matchId);
        int strikerId = getCurrentStrikerId(con, matchId, inning);
        int bowlerId = getCurrentBowlerId(con, matchId, inning);


        String teamSql = " SELECT t.team_name, tm.runs_scored, tm.wickets_lost, tm.overs_played "+
        "FROM TeamMatchStats tm "+
        "JOIN Teams t ON tm.team_id = t.team_id "+
        "WHERE tm.match_id = ? "+
        "ORDER BY tm.team_id "+
        "LIMIT 1 OFFSET ?";
        PreparedStatement teamPs = con.prepareStatement(teamSql);
        teamPs.setInt(1, matchId);
        teamPs.setInt(2, inning - 1);
        ResultSet teamRs = teamPs.executeQuery();

        if (!teamRs.next()) {
            System.out.println("No data found for match " + matchId + " inning " + inning);
            return;
        }

        String teamName = teamRs.getString("team_name");
        int totalRun = teamRs.getInt("runs_scored");
        int wickets = teamRs.getInt("wickets_lost");
        double oversPlayed = teamRs.getDouble("overs_played");

        int i = (int) oversPlayed;
        int j = (int) Math.round((oversPlayed - i) * 10);

        System.out.println("-----------------");
        System.out.println(teamName);
        System.out.print("(" + totalRun + "/" + wickets + ")          OVER: (" + i + "." + j + ")");
        if (inning == 2) {
            System.out.println("     Target " + getTargetFromDB(con, matchId));
        } else {
            System.out.println();
        }

        double crr = (i == 0 && j == 0) ? 0.0 : (totalRun * 6.0) / (i * 6 + j);
        System.out.println("CRR: " + String.format("%.2f", crr));


        System.out.println();
        System.out.println("Batter             R     B     4s     6s");
        String batsmanSql = " SELECT p.player_id, p.player_name, s.runs_scored, s.balls_faced, s.fours, s.sixes "+
        "FROM PlayerMatchStats s "+
        "JOIN Players p ON s.player_id = p.player_id "+
        "JOIN Teams t ON p.team_id = t.team_id "+
        "WHERE s.match_id = ? AND t.team_name = ? "+
        "AND s.BattingStats != 'DNB' "+
        "ORDER BY s.BattingStats = 'PLAYING' DESC LIMIT 2";

        PreparedStatement batPs = con.prepareStatement(batsmanSql);
        batPs.setInt(1, matchId);
        batPs.setString(2, teamName);
        ResultSet batRs = batPs.executeQuery();

        while (batRs.next()) {
            String name = batRs.getString("player_name");
            if (batRs.getInt("player_id") == strikerId) name += "*";
            System.out.println(
                    name + " ".repeat(Math.max(0, 19 - name.length())) +
                            batRs.getInt("runs_scored") + " ".repeat(6 - String.valueOf(batRs.getInt("runs_scored")).length()) +
                            batRs.getInt("balls_faced") + " ".repeat(6 - String.valueOf(batRs.getInt("balls_faced")).length()) +
                            batRs.getInt("fours") + " ".repeat(7 - String.valueOf(batRs.getInt("fours")).length()) +
                            batRs.getInt("sixes")
            );
        }


        System.out.println();
        System.out.println("Bowler             O     R     W");
        String bowlerSql = " SELECT p.player_name, s.overs_bowled, s.runs_conceded, s.wickets "+
                            "FROM PlayerMatchStats s "+
                            "JOIN Players p ON s.player_id = p.player_id "+
                            "WHERE s.match_id = ? AND s.player_id = ? ";
        PreparedStatement bowlPs = con.prepareStatement(bowlerSql);
        bowlPs.setInt(1, matchId);
        bowlPs.setInt(2, bowlerId);
        ResultSet bowlRs = bowlPs.executeQuery();
        if (bowlRs.next()) {
            System.out.println(
                            bowlRs.getString("player_name") + " ".repeat(Math.max(0, 18 - bowlRs.getString("player_name").length())) +
                            bowlRs.getDouble("overs_bowled") + " ".repeat(6 - String.valueOf(bowlRs.getDouble("overs_bowled")).length()) +
                            bowlRs.getInt("runs_conceded") + " ".repeat(7 - String.valueOf(bowlRs.getInt("runs_conceded")).length()) +
                            bowlRs.getInt("wickets")
            );
        }

        System.out.println("-----------------");
    }

    private static int getCurrentInningFromDB(Connection con, int matchId) throws SQLException {
        String sql = "SELECT MAX(innings) FROM ball_by_ball WHERE match_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0 ? 1 : rs.getInt(1);
        }
        return 1;
    }

    private static int getCurrentStrikerId(Connection con, int matchId, int inning) throws SQLException {
        String sql = "SELECT striker_id FROM ball_by_ball WHERE match_id = ? AND innings = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ps.setInt(2, inning);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("striker_id");
        }
        return -1;
    }

    private static int getCurrentBowlerId(Connection con, int matchId, int inning) throws SQLException {
        String sql = "SELECT bowler_id FROM ball_by_ball WHERE match_id = ? AND innings = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ps.setInt(2, inning);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("bowler_id");
        }
        return -1;
    }

    private static int getTargetFromDB(Connection con, int matchId) throws SQLException {
        String sql = "SELECT runs_scored FROM TeamMatchStats WHERE match_id = ? ORDER BY team_id LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("runs_scored") + 1;
        }
        return 0;
    }




    public static void main(String[] args) throws SQLException {

        Connection con=SQLquery.getCon();
            if(con!=null) {
                System.out.println("Connection successful!");

                /*     User
                       CREATE TABLE Users (user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(25) UNIQUE NOT NULL,password VARCHAR(20)
                       NOT NULL,role ENUM('HOST', 'AUDIENCE') NOT NULL,
                       full_name VARCHAR(100)

                       Teams
                       CREATE TABLE Teams (team_id INT PRIMARY KEY AUTO_INCREMENT,
                       team_name VARCHAR(50) UNIQUE NOT NULL)


                       Players
                       CREATE TABLE Players (player_id INT PRIMARY KEY AUTO_INCREMENT,
                       player_name VARCHAR(50) NOT NULL,team_id INT NOT NULL,
                       role ENUM('BATSMAN', 'BOWLER', 'ALLROUNDER') NOT NULL,
                       FOREIGN KEY (team_id) REFERENCES Teams(team_id))

                       Schedule
                       CREATE TABLE Matches (
                       match_id INT PRIMARY KEY AUTO_INCREMENT,
                       team1_id INT NOT NULL, team2_id INT NOT NULL, winner_team_id INT,
                       match_status ENUM('UPCOMING', 'COMPLETED','LIVE') DEFAULT 'UPCOMING',
                       FOREIGN KEY (team1_id) REFERENCES Teams(team_id),
                       FOREIGN KEY (team2_id) REFERENCES Teams(team_id),
                       FOREIGN KEY (winner_team_id) REFERENCES Teams(team_id))

                       Tournament
                       CREATE TABLE Tournaments(
                       tournament_id INT PRIMARY KEY AUTO_INCREMENT,
                       tournament_name VARCHAR(50),
                       year YEAR,host VARCHAR(50))

                       Match by Match data of player
                       CREATE TABLE PlayerMatchStats (
                       stat_id INT PRIMARY KEY AUTO_INCREMENT,
                       match_id INT,player_id INT,
                       runs_scored INT DEFAULT 0,
                       balls_faced INT DEFAULT 0,
                       fours INT DEFAULT 0,
                       sixes INT DEFAULT 0,
                       wickets INT DEFAULT 0,
                       overs_bowled DECIMAL(4,1) DEFAULT 0.0,
                       runs_conceded INT DEFAULT 0,
                       FOREIGN KEY (match_id) REFERENCES Matches(match_id),
                       FOREIGN KEY (player_id) REFERENCES Players(player_id))

                       Match by Match data of Match
                       CREATE TABLE TeamMatchStats(
                       team_stat_id INT PRIMARY KEY AUTO_INCREMENT,
                       match_id INT,team_id INT,
                       runs_scored INT,wickets_lost INT
                       overs_played DECIMAL(3,1),
                       result ENUM('WIN', 'LOSS', 'DRAW'),
                       FOREIGN KEY (match_id) REFERENCES Matches(match_id),
                       FOREIGN KEY (team_id) REFERENCES Teams(team_id))


                       points table
                       CREATE TABLE PointsTable (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       tournament_id INT,team_id INT,
                       matches_played INT DEFAULT 0,
                       matches_won INT DEFAULT 0,
                       matches_lost INT DEFAULT 0,
                       matches_drawn INT DEFAULT 0,
                       points INT DEFAULT 0,
                       net_run_rate DECIMAL(3,2) DEFAULT 0.00,
                       FOREIGN KEY (tournament_id) REFERENCES Tournaments(tournament_id),
                       FOREIGN KEY (team_id) REFERENCES Teams(team_id))


                       Ball by ball
                       CREATE TABLE ball_by_ball (
                       id INT AUTO_INCREMENT PRIMARY KEY, match_id INT NOT NULL,
                       innings INT NOT NULL,over_number INT NOT NULL,
                       ball_number INT NOT NULL, striker_id INT NOT NULL,
                       non_striker_id INT NOT NULL,bowler_id INT NOT NULL,
                       runs_batsman INT DEFAULT 0,extras_runs INT DEFAULT 0,
                       is_wicket BOOLEAN DEFAULT FALSE,out_player_id INT,
                       timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)


                 */


    }

}
}
