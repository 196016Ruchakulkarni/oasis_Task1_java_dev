import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class reserv_online {
    public static final int min = 1000;
    private static final int max = 9999;

    public static class user {
        private String username;
        private String password;

        Scanner sc = new Scanner(System.in);

        public user() {

        }

        public String getUsername() {
            System.out.println("Enter Username:");
            username = sc.nextLine();
            return username;
        }
        

        public String getPassword() {
            System.out.println("Enter Password:");
            password = sc.nextLine();
            return password;
        }
    }

    public static class PNrecord {
        private int pn_num;
        private String passangernm;
        private String train_num;
        private String classType;
        private String journey_date;
        private String from;
        private String to;

        Scanner sc = new Scanner(System.in);

        public int getpn_number() {
            Random random = new Random();
            pn_num = random.nextInt(max) + min;
            return pn_num;
        }

        public String getPassengerName() {
            System.out.println("Enter the passenger name:");
            passangernm = sc.nextLine();
            return passangernm;
        }

        public String getTrainNumber() {
            System.out.println("Enter the Train No");
            train_num = sc.nextLine();
            return train_num;
        }

        public String getclassType() {
            System.out.println("Enter the class Type: ");
            classType = sc.nextLine();
            return classType;
        }

        public String getDate() {
            System.out.println("Enter the journey date as 'YYYY-MM-DD' format");
            sc.nextLine();
            return journey_date;
        }

        public String getfrom() {
            System.out.println("Enter Starting place:");
            from = sc.nextLine();
            return from;
        }

        public String getto() {
            System.out.println("Enter Destination place:");
            to = sc.nextLine();
            return to;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        user u1 = new user();
        String username = u1.getUsername();
        String password = u1.getPassword();

        String url = "jdbc:mysql://localhost:3306/rucha_intern";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("User Connection Granted..\n");

                while (true) {
                    String InsertQuery = "insert into reservations values (?,?,?,?,?,?,?)";
                    String DeleteQuery = "delete FROM reservations WHERE pn_num=?";
                    String ShowQuery = "Select * from reservations";

                    System.out.println("Enter Choice:");
                    System.out.println("1.Insert Record:\n");
                    System.out.println("2.Delete Record\n");
                    System.out.println("3.Show All Records\n");
                    System.out.println("4.Exit\n");
                    int choice = sc.nextInt();

                    if (choice == 1) {
                        PNrecord p1 = new PNrecord();
                        int pn_num = p1.getpn_number();
                        String passangernm = p1.getPassengerName();
                        String train_num = p1.getTrainNumber();
                        String classType = p1.getclassType();
                        String journey_date = p1.getDate();
                        String getfrom = p1.getfrom();
                        String getto = p1.getto();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(InsertQuery)) {
                            preparedStatement.setInt(1, pn_num);
                            preparedStatement.setString(2, passangernm);
                            preparedStatement.setString(3, train_num);
                            preparedStatement.setString(4, classType);
                            preparedStatement.setString(5, journey_date);
                            preparedStatement.setString(6, getfrom);
                            preparedStatement.setString(7, getto);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record Added Successfully..!");
                            }

                            else {
                                System.out.println("No Records were Added..!");
                            }

                        } catch (SQLException e) {
                            System.out.println("SQLException:" + e.getMessage());
                        }
                    }

                    else if (choice == 2) {
                        System.out.println("Enter the PNR number to delete Record:");
                        int pn_num = sc.nextInt();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(DeleteQuery)) {
                            preparedStatement.setInt(1, pn_num);
                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Record Deleted Successfully..!!");
                            }

                            else {
                                System.out.println("No records were deleted..!!");
                            }
                        }

                        catch (SQLException e) {
                            System.out.println("SQLException: " + e.getMessage());
                        }
                    }

                    else if (choice == 3) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(ShowQuery);
                                ResultSet resultSet = preparedStatement.executeQuery()) {
                            System.out.println("\n All Records Printing..");

                            while (resultSet.next()) {
                                String pn_num = resultSet.getString("pn_num");
                                String passangernm = resultSet.getString("passangernm");
                                String train_num = resultSet.getString("train_num");
                                String classType = resultSet.getString("classType");
                                String journey_date = resultSet.getString("journey_date");
                                String fromLocation = resultSet.getString("getfrom");
                                String toLocation = resultSet.getString("getto");

                                System.out.println("PNR Number:" + pn_num);
                                System.out.println("Passanger Name:" + passangernm);
                                System.out.println("Train Number:" + train_num);
                                System.out.println("Class Type:" + classType);
                                System.out.println("Journey Date:" + journey_date);
                                System.out.println("from Location:" + fromLocation);
                                System.out.println("To Location:" + toLocation);
                                System.out.println("----------");
                            }
                        }

                        catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    }

                    else if (choice == 4) {
                        System.out.println("Existing the Program..");
                        break;
                    }

                    else {
                        System.out.println("Invalid Choice Entered..\n");
                    }
                }
            }

            catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
        }

        catch (ClassNotFoundException e) {
            System.out.println("Error Loading JDBC driver: " + e.getMessage());
        }
        sc.close();

    }
}
