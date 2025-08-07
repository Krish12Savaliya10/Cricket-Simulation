package DataBase;

import java.sql.*;
import java.util.Scanner;

public class SQLquery {
    public static Connection getCon() throws SQLException {
        String url = "jdbc:mysql://localhost:8889/cricket Simulation";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    //Insert user: INSERT INTO Users (full_name, email, password, role) VALUES (?, ?, ?, ?)
    public static void signup() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Enter Email ID: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.print("Enter Role (HOST / AUDIENCE / AUTHOR): ");
        String role = sc.nextLine().toUpperCase();

        Connection con = getCon();
        String sql = "INSERT INTO Users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, fullName);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, role);

        try {
            ps.executeUpdate();
            System.out.println("Signup successful!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Email already exists. Please login.");
        }
        con.close();
    }

    //Login: SELECT * FROM Users WHERE email = ? AND password = ?
    public static String login() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Email ID: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Connection con = getCon();
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Login successful! Welcome, " + rs.getString("full_name"));
            String role= rs.getString("role");
            System.out.println("Role: " + role);
            return role;
        } else {
            System.out.println("Invalid credentials.Enter valid input");
            return login();
        }

    }

    //insert team: INSERT INTO Users (team_name) VALUES (name)
    public static void insertTeam(int teamId,String name) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Teams (team_id,team_name) VALUES (?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,teamId);
        ps.setString(2,name);
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
    public static void insertSchedule(int match_id,int team1Id,int team2Id) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Matches(match_id ,team1_id,team2_id) VALUES (?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, match_id);
        ps.setInt(2, team1Id );
        ps.setInt(3, team2Id);
        ps.executeUpdate();
    }
    public static void insertTournament(String tournament_name,int year,String host) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO Tournaments(tournament_name ,year,host) VALUES (?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, year );
        ps.setString(3, host);
        ps.executeUpdate();
    }
    public static void insertIntoPointsTable(String tournament_name,int tourYear,int team_id) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO PointsTable(tournament_id ,team_id) VALUES (?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, tourYear );
        ps.setInt(3, team_id);
        ps.executeUpdate();
    }
    public static void insertTeamMatchStats(int match_id, int  team_id, int tourYear, String tournament_name) throws SQLException {
        Connection con = getCon();
        String sql = "INSERT INTO TeamMatchStats(tournament_id, match_id, team_id) " +
                "VALUES ((SELECT tournament_id FROM Tournaments WHERE tournament_name = ? AND year = ?), ?, ? ) ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tournament_name);
        ps.setInt(2, tourYear);
        ps.setInt(3, match_id);
        ps.setInt(4, team_id);
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
                                        int runs_batsman, int extras_runs, boolean is_wicket,
                                        Integer out_player_id) throws SQLException {

        Connection con = getCon(); // Your method to get DB connection
        String sql = "INSERT INTO ball_by_ball (match_id, innings, over_number, ball_number, striker_id, " +
                "non_striker_id, bowler_id, runs_batsman, extras_runs, is_wicket, out_player_id) " +
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
        ps.setBoolean(10, is_wicket);

        // Set NULL if no wicket (nullable out_player_id)
        if (out_player_id != null) {
            ps.setInt(11, out_player_id);
        } else {
            ps.setNull(11, java.sql.Types.INTEGER);
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
