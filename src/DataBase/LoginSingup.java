package DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;
import static DataBase.SQLquery.*;
import static Simulation.Tournament.*;

public class LoginSingup {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Signup\n2. Login\nEnter choice:");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (choice == 1) {
            System.out.print("Enter Full Name: ");
            String fullName = sc.nextLine();

            System.out.print("Enter Email ID: ");
            String email = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            System.out.print("Enter Role (HOST / AUDIENCE: ");
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

        }
        else if (choice == 2) {
            String role = "";
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
                role= rs.getString("role");
                System.out.println("Role: " + role);

            } else {
                System.out.println("Invalid credentials.Enter valid input");

            }
            if(role.equalsIgnoreCase("host")) {
                System.out.println();
                int choiceForMatch;
                do {
                    System.out.println("Enter 1 for new matches");
                    System.out.println("Enter 2 for saved matches");
                    System.out.println("Enter 3 for exit");
                    choiceForMatch=sc.nextInt();
                    switch(choiceForMatch){
                        case 1:
                            organizeMatch(email);
                            break;

                        case 2:
                            setScheduleFromDB(sc,email);
                            System.out.println("****************************************");
                            startMatch(sc,email);
                            break;
                    }
                }while (choiceForMatch!=3);
            }
            else if(role.equalsIgnoreCase("AUDIENCE")){

            }
            else
                System.out.println("after database");
        }
        else
            System.out.println("Invalid choice");
    }
}
