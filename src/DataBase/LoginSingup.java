package DataBase;
import java.util.Scanner;
import static DataBase.SQLquery.*;
import static Simulation.Tournament.organizeMatch;

public class LoginSingup {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Signup\n2. Login\nEnter choice:");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (choice == 1) {
            signup();

        }
        else if (choice == 2) {
            if(login().equalsIgnoreCase("host")) {
                System.out.println();
                organizeMatch();
            }
            else
                System.out.println("after database");
        }
        else
            System.out.println("Invalid choice");
    }
}
