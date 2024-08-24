import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    // The 3 arrays to keep track of sold and free seats
    static int[] row1 = new int[12];
    static int[] row2 = new int[16];
    static int[] row3 = new int[20];
    static ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public static double calculate_price(int row, int seat) {
        double price = 0;
        if (row == 1) {
            price = 10.0;
        } else if (row == 2) {
            price = 8.0;
        } else if (row == 3) {
            price = 6.0;
        }

        if (seat > 10) {
            price *= 1.2; // increase price by 20% for seats in the last 10 columns
        }

        return price;
    }

    public static ArrayList<Ticket> buy_ticket(Scanner input, ArrayList<Ticket> tickets) {

        // Get user input for row and seat
        System.out.print("Enter row number (1-3): ");
        int row = input.nextInt();
        System.out.print("Enter seat number (1-20): ");
        int seat = input.nextInt();

        // Check that the row and seat numbers are valid
        if (row < 1 || row > 3 || seat < 1 || seat > 20) {
            System.out.println("Invalid row or seat number. Please try again.");
            return tickets;
        }

        // Get the array for the selected row
        int[] selected_row;
        if (row == 1) {
            selected_row = row1;
        } else if (row == 2) {
            selected_row = row2;
        } else {
            selected_row = row3;
        }

        // Check that the selected seat is available
        if (selected_row[seat - 1] == 1) {
            System.out.println("The selected seat is not available. Please try again.");
            return tickets;
        }

        // Record the seat as occupied
        selected_row[seat - 1] = 1;

        // Get the person's information
        System.out.print("Enter name: ");
        String name = input.next();
        System.out.print("Enter surname: ");
        String surname = input.next();
        System.out.print("Enter email: ");
        String email = input.next();

        // Create a new person object
        Person person = new Person(name, surname, email);

        // Create a new ticket object and add it to the list of tickets
        Ticket ticket = new Ticket(row, seat, calculate_price(row, seat), person);
        tickets.add(ticket);

        System.out.println("Seat " + seat + " in row " + row + " has been sold to " + name + " " + surname + " for £"
                + calculate_price(row, seat));
        // Return the updated ticket list
        return tickets;
    }

    public static void print_seating_area() {
        // Print the stage
        System.out.println("***********");
        System.out.println("*  STAGE  *");
        System.out.println("***********");

        // Loop through each row and print the seats
        for (int i = 0; i < 3; i++) {
            int[] row;
            if (i == 0) {
                row = row1;
            } else if (i == 1) {
                row = row2;
            } else {
                row = row3;
            }

            // Print the seats
            for (int j = 0; j < row.length; j++) {
                if (j == 0) {
                    System.out.print(" ");
                }
                if (row[j] == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
                if (j == 5 || j == 11 || j == 15) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static ArrayList<Ticket> cancel_ticket(Scanner scanner, ArrayList<Ticket> tickets) {
        // Ask the user for the row and seat number
        System.out.println("Enter the row number:");
        int row_number = scanner.nextInt();
        System.out.println("Enter the seat number:");
        int seat_number = scanner.nextInt();

        // Check if the row and seat numbers are valid
        if (row_number < 1 || row_number > 3) {
            System.out.println("Invalid row number");
            return tickets;
        }
        int[] row;
        if (row_number == 1) {
            row = row1;
        } else if (row_number == 2) {
            row = row2;
        } else {
            row = row3;
        }
        if (seat_number < 1 || seat_number > row.length) {
            System.out.println("Invalid seat number");
            return tickets;
        }

        // Check if the seat is already available
        if (row[seat_number - 1] == 0) {
            System.out.println("Seat is already available");
            return tickets;
        }

        // Find the ticket in the list and remove it
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == row_number && ticket.getSeat() == seat_number) {
                tickets.remove(ticket);
                break;
            }
        }

        // Cancel the ticket (make the seat available)
        row[seat_number - 1] = 0;
        System.out.println("Ticket cancelled");

        // Return the updated ticket list
        return tickets;
    }

    public static void show_available() {
        for (int i = 1; i <= 3; i++) {
            int[] row;
            if (i == 1) {
                row = row1;
            } else if (i == 2) {
                row = row2;
            } else {
                row = row3;
            }
            System.out.print("Seats available in row " + i + ": ");
            for (int j = 0; j < row.length; j++) {
                if (row[j] == 0) {
                    System.out.print((j + 1) + " ");
                }
            }
            System.out.println();
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter("seating.txt");
            for (int i = 0; i < row1.length; i++) {
                writer.write(row1[i] + ",");
            }
            writer.write("\n");
            for (int i = 0; i < row2.length; i++) {
                writer.write(row2[i] + ",");
            }
            writer.write("\n");
            for (int i = 0; i < row3.length; i++) {
                writer.write(row3[i] + ",");
            }
            writer.write("\n");
            writer.close();
            System.out.println("Seating information saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the seating information to file.");
        }
    }

    public static void load() {
        try {
            // Open the file for reading
            File file = new File("seating.txt");
            Scanner scanner = new Scanner(file);

            // Read each row's data from the file
            String row1Data = scanner.nextLine();
            String row2Data = scanner.nextLine();
            String row3Data = scanner.nextLine();

            // Convert the row's data strings to arrays of integers
            row1 = Arrays.stream(row1Data.split(",")).mapToInt(Integer::parseInt).toArray();
            row2 = Arrays.stream(row2Data.split(",")).mapToInt(Integer::parseInt).toArray();
            row3 = Arrays.stream(row3Data.split(",")).mapToInt(Integer::parseInt).toArray();

            // Close the file
            scanner.close();

            System.out.println("Seating data has been loaded from file.");

        } catch (FileNotFoundException e) {
            System.out.println("Seating data file not found.");
        }
    }

    public static void show_tickets_info() {
        double total_price = 0.0;
        System.out.println("Ticket information:");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            System.out.println("Ticket " + (i + 1) + ":");
            ticket.print();
            total_price += ticket.getPrice();
        }
        System.out.println("Total price: £" + total_price);
    }

    public static ArrayList<Ticket> sort_tickets() {
        // Create a copy of the tickets ArrayList to avoid modifying the original
        ArrayList<Ticket> sorted_tickets = new ArrayList<>(tickets);

        // Sort the tickets by price in ascending order
        Collections.sort(sorted_tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return Double.compare(t1.getPrice(), t2.getPrice());
            }
        });

        // Print the tickets information after sorting
        System.out.println("Sorted Tickets:");
        for (Ticket ticket : sorted_tickets) {
            ticket.print();
            System.out.println("\n---------------------\n");
        }

        // Return the sorted list of tickets
        return sorted_tickets;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the New Theatre");

        // Create scanner to get user input
        Scanner input = new Scanner(System.in);

        // Print the menu options
        System.out.println("-------------------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("1) Buy a ticket");
        System.out.println("2) Print seating area");
        System.out.println("3) Cancel ticket");
        System.out.println("4) List available seats");
        System.out.println("5) Save to file");
        System.out.println("6) Load from file");
        System.out.println("7) Print ticket information and total price");
        System.out.println("8) Sort tickets by price");
        System.out.println("0) Quit");
        System.out.println("-------------------------------------------------");

        // Get user input for menu option
        System.out.print("Enter option: ");
        int option = input.nextInt();

        // Keep looping until user selects 0 to quit
        while (option != 0) {
            switch (option) {
                case 1:
                    tickets = buy_ticket(input, tickets);
                    break;
                case 2:
                    // TODO: Implement the Print seating area option
                    print_seating_area();
                    break;
                case 3:
                    // TODO: Implement the Cancel ticket option
                    tickets = cancel_ticket(input, tickets);

                    break;
                case 4:
                    // TODO: Implement the List available seats option
                    show_available();
                    break;
                case 5:
                    // TODO: Implement the Save to file option
                    save();
                    break;
                case 6:
                    // TODO: Implement the Load from file option
                    load();
                    break;
                case 7:
                    // TODO: Implement the Print ticket information and total price option
                    show_tickets_info();
                    break;
                case 8:
                    // TODO: Implement the Sort tickets by price option
                    sort_tickets();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            // Print the main menu
            System.out.println("-------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load from file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("0) Quit");
            System.out.println("-------------------------------------------------");

            // Get user input for menu option
            System.out.print("Enter option: ");
            option = input.nextInt();
        }
    }
}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    // The 3 arrays to keep track of sold and free seats
    static int[] row1 = new int[12];
    static int[] row2 = new int[16];
    static int[] row3 = new int[20];
    static ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public static double calculate_price(int row, int seat) {
        double price = 0;
        if (row == 1) {
            price = 10.0;
        } else if (row == 2) {
            price = 8.0;
        } else if (row == 3) {
            price = 6.0;
        }

        if (seat > 10) {
            price *= 1.2; // increase price by 20% for seats in the last 10 columns
        }

        return price;
    }

    public static ArrayList<Ticket> buy_ticket(Scanner input, ArrayList<Ticket> tickets) {

        // Get user input for row and seat
        System.out.print("Enter row number (1-3): ");
        int row = input.nextInt();
        System.out.print("Enter seat number (1-20): ");
        int seat = input.nextInt();

        // Check that the row and seat numbers are valid
        if (row < 1 || row > 3 || seat < 1 || seat > 20) {
            System.out.println("Invalid row or seat number. Please try again.");
            return tickets;
        }

        // Get the array for the selected row
        int[] selected_row;
        if (row == 1) {
            selected_row = row1;
        } else if (row == 2) {
            selected_row = row2;
        } else {
            selected_row = row3;
        }

        // Check that the selected seat is available
        if (selected_row[seat - 1] == 1) {
            System.out.println("The selected seat is not available. Please try again.");
            return tickets;
        }

        // Record the seat as occupied
        selected_row[seat - 1] = 1;

        // Get the person's information
        System.out.print("Enter name: ");
        String name = input.next();
        System.out.print("Enter surname: ");
        String surname = input.next();
        System.out.print("Enter email: ");
        String email = input.next();

        // Create a new person object
        Person person = new Person(name, surname, email);

        // Create a new ticket object and add it to the list of tickets
        Ticket ticket = new Ticket(row, seat, calculate_price(row, seat), person);
        tickets.add(ticket);

        System.out.println("Seat " + seat + " in row " + row + " has been sold to " + name + " " + surname + " for £"
                + calculate_price(row, seat));
        // Return the updated ticket list
        return tickets;
    }

    public static void print_seating_area() {
        // Print the stage
        System.out.println("***********");
        System.out.println("*  STAGE  *");
        System.out.println("***********");

        // Loop through each row and print the seats
        for (int i = 0; i < 3; i++) {
            int[] row;
            if (i == 0) {
                row = row1;
            } else if (i == 1) {
                row = row2;
            } else {
                row = row3;
            }

            // Print the seats
            for (int j = 0; j < row.length; j++) {
                if (j == 0) {
                    System.out.print(" ");
                }
                if (row[j] == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
                if (j == 5 || j == 11 || j == 15) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static ArrayList<Ticket> cancel_ticket(Scanner scanner, ArrayList<Ticket> tickets) {
        // Ask the user for the row and seat number
        System.out.println("Enter the row number:");
        int row_number = scanner.nextInt();
        System.out.println("Enter the seat number:");
        int seat_number = scanner.nextInt();

        // Check if the row and seat numbers are valid
        if (row_number < 1 || row_number > 3) {
            System.out.println("Invalid row number");
            return tickets;
        }
        int[] row;
        if (row_number == 1) {
            row = row1;
        } else if (row_number == 2) {
            row = row2;
        } else {
            row = row3;
        }
        if (seat_number < 1 || seat_number > row.length) {
            System.out.println("Invalid seat number");
            return tickets;
        }

        // Check if the seat is already available
        if (row[seat_number - 1] == 0) {
            System.out.println("Seat is already available");
            return tickets;
        }

        // Find the ticket in the list and remove it
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == row_number && ticket.getSeat() == seat_number) {
                tickets.remove(ticket);
                break;
            }
        }

        // Cancel the ticket (make the seat available)
        row[seat_number - 1] = 0;
        System.out.println("Ticket cancelled");

        // Return the updated ticket list
        return tickets;
    }

    public static void show_available() {
        for (int i = 1; i <= 3; i++) {
            int[] row;
            if (i == 1) {
                row = row1;
            } else if (i == 2) {
                row = row2;
            } else {
                row = row3;
            }
            System.out.print("Seats available in row " + i + ": ");
            for (int j = 0; j < row.length; j++) {
                if (row[j] == 0) {
                    System.out.print((j + 1) + " ");
                }
            }
            System.out.println();
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter("seating.txt");
            for (int i = 0; i < row1.length; i++) {
                writer.write(row1[i] + ",");
            }
            writer.write("\n");
            for (int i = 0; i < row2.length; i++) {
                writer.write(row2[i] + ",");
            }
            writer.write("\n");
            for (int i = 0; i < row3.length; i++) {
                writer.write(row3[i] + ",");
            }
            writer.write("\n");
            writer.close();
            System.out.println("Seating information saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the seating information to file.");
        }
    }

    public static void load() {
        try {
            // Open the file for reading
            File file = new File("seating.txt");
            Scanner scanner = new Scanner(file);

            // Read each row's data from the file
            String row1Data = scanner.nextLine();
            String row2Data = scanner.nextLine();
            String row3Data = scanner.nextLine();

            // Convert the row's data strings to arrays of integers
            row1 = Arrays.stream(row1Data.split(",")).mapToInt(Integer::parseInt).toArray();
            row2 = Arrays.stream(row2Data.split(",")).mapToInt(Integer::parseInt).toArray();
            row3 = Arrays.stream(row3Data.split(",")).mapToInt(Integer::parseInt).toArray();

            // Close the file
            scanner.close();

            System.out.println("Seating data has been loaded from file.");

        } catch (FileNotFoundException e) {
            System.out.println("Seating data file not found.");
        }
    }

    public static void show_tickets_info() {
        double total_price = 0.0;
        System.out.println("Ticket information:");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            System.out.println("Ticket " + (i + 1) + ":");
            ticket.print();
            total_price += ticket.getPrice();
        }
        System.out.println("Total price: £" + total_price);
    }

    public static ArrayList<Ticket> sort_tickets() {
        // Create a copy of the tickets ArrayList to avoid modifying the original
        ArrayList<Ticket> sorted_tickets = new ArrayList<>(tickets);

        // Sort the tickets by price in ascending order
        Collections.sort(sorted_tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return Double.compare(t1.getPrice(), t2.getPrice());
            }
        });

        // Print the tickets information after sorting
        System.out.println("Sorted Tickets:");
        for (Ticket ticket : sorted_tickets) {
            ticket.print();
            System.out.println("\n---------------------\n");
        }

        // Return the sorted list of tickets
        return sorted_tickets;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the New Theatre");

        // Create scanner to get user input
        Scanner input = new Scanner(System.in);

        // Print the menu options
        System.out.println("-------------------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("1) Buy a ticket");
        System.out.println("2) Print seating area");
        System.out.println("3) Cancel ticket");
        System.out.println("4) List available seats");
        System.out.println("5) Save to file");
        System.out.println("6) Load from file");
        System.out.println("7) Print ticket information and total price");
        System.out.println("8) Sort tickets by price");
        System.out.println("0) Quit");
        System.out.println("-------------------------------------------------");

        // Get user input for menu option
        System.out.print("Enter option: ");
        int option = input.nextInt();

        // Keep looping until user selects 0 to quit
        while (option != 0) {
            switch (option) {
                case 1:
                    tickets = buy_ticket(input, tickets);
                    break;
                case 2:
                    // TODO: Implement the Print seating area option
                    print_seating_area();
                    break;
                case 3:
                    // TODO: Implement the Cancel ticket option
                    tickets = cancel_ticket(input, tickets);

                    break;
                case 4:
                    // TODO: Implement the List available seats option
                    show_available();
                    break;
                case 5:
                    // TODO: Implement the Save to file option
                    save();
                    break;
                case 6:
                    // TODO: Implement the Load from file option
                    load();
                    break;
                case 7:
                    // TODO: Implement the Print ticket information and total price option
                    show_tickets_info();
                    break;
                case 8:
                    // TODO: Implement the Sort tickets by price option
                    sort_tickets();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            // Print the main menu
            System.out.println("-------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load from file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("0) Quit");
            System.out.println("-------------------------------------------------");

            // Get user input for menu option
            System.out.print("Enter option: ");
            option = input.nextInt();
        }
    }
}
