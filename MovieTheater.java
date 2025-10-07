import java.util.Arrays;
import java.util.Scanner;

/**
 * Movie Theater Seat Reservation System (2D Arrays in Java)
 * O = available, X = reserved
 */
public class MovieTheater {
    // ---- change theater size here ----
    private static final int ROWS = 8;   
    private static final int COLS = 10;  

    
    private final boolean[][] seats = new boolean[ROWS][COLS];
    private final Scanner in = new Scanner(System.in);

    public MovieTheater() {
        for (int r = 0; r < ROWS; r++) Arrays.fill(seats[r], true);
    }

    public static void main(String[] args) {
        new MovieTheater().run();
    }

    private void run() {
        while (true) {
            printChart();
            System.out.println("""
                    -------------------------
                    1) Reserve a seat
                    2) Cancel a reservation
                    3) Display seating chart
                    0) Exit
                    """);
            int choice = readInt("Choose: ", 0, 3);
            switch (choice) {
                case 1 -> reserveFlow();
                case 2 -> cancelFlow();
                case 3 -> {} 
                case 0 -> { System.out.println("Goodbye!"); return; }
            }
            pause();
        }
    }

    
    private void printChart() {
        System.out.println("\nCurrent Seating Chart");
        System.out.print("     ");
        for (int c = 1; c <= COLS; c++) System.out.printf("%3d", c);
        System.out.println("\n     " + "---".repeat(COLS));
        for (int r = 0; r < ROWS; r++) {
            System.out.printf("%3s |", rowName(r));
            for (int c = 0; c < COLS; c++) {
                System.out.printf("%3s", seats[r][c] ? 'O' : 'X');
            }
            System.out.println();
        }
        System.out.println("Legend: O = available, X = reserved\n");
    }

    
    private void reserveFlow() {
        int[] rc = readSeat("Enter seat to reserve (e.g., B7): ");
        int r = rc[0], c = rc[1];
        if (seats[r][c]) {
            seats[r][c] = false;
            System.out.printf("Reserved %s%d.\n", rowName(r), c + 1);
        } else {
            System.out.printf("Sorry, %s%d is already taken.\n", rowName(r), c + 1);
            int[] sug = suggestNearest(r, c);
            if (sug != null) {
                int sr = sug[0], sc = sug[1];
                System.out.printf("Try %s%d instead? (y/n): ", rowName(sr), sc + 1);
                if (yes()) {
                    seats[sr][sc] = false;
                    System.out.printf("Reserved %s%d.\n", rowName(sr), sc + 1);
                } else System.out.println("No reservation made.");
            } else System.out.println("No alternative seats available.");
        }
        printChart();
    }

    
    private int[] suggestNearest(int row, int col) {
        for (int d = 0; d < COLS; d++) {              // same row
            int L = col - d, R = col + d;
            if (L >= 0 && seats[row][L]) return new int[]{row, L};
            if (R < COLS && seats[row][R]) return new int[]{row, R};
        }
        for (int r = 0; r < ROWS; r++) {              // other rows
            if (r == row) continue;
            for (int d = 0; d < COLS; d++) {
                int L = col - d, R = col + d;
                if (L >= 0 && seats[r][L]) return new int[]{r, L};
                if (R < COLS && seats[r][R]) return new int[]{r, R};
            }
        }
        return null;
    }

    
    private void cancelFlow() {
        int[] rc = readSeat("Enter seat to cancel (e.g., B7): ");
        int r = rc[0], c = rc[1];
        if (!seats[r][c]) {
            seats[r][c] = true;
            System.out.printf("Canceled reservation for %s%d.\n", rowName(r), c + 1);
        } else {
            System.out.printf("%s%d is not currently reserved.\n", rowName(r), c + 1);
        }
        printChart();
    }

    
    private int[] readSeat(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim().toUpperCase().replaceAll("[\\s-]+", "");
            if (s.length() >= 2 && Character.isLetter(s.charAt(0))) {
                int r = s.charAt(0) - 'A';
                try {
                    int c = Integer.parseInt(s.substring(1)) - 1;
                    if (r >= 0 && r < ROWS && c >= 0 && c < COLS) return new int[]{r, c};
                } catch (NumberFormatException ignored) {}
            }
            System.out.printf("Invalid seat. Rows A-%s, columns 1-%d.\n", rowName(ROWS - 1), COLS);
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(in.nextLine().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Enter a number between %d and %d.\n", min, max);
        }
    }

    private boolean yes() { return in.nextLine().trim().toLowerCase().startsWith("y"); }
    private String rowName(int r) { return String.valueOf((char) ('A' + r)); }
    private void pause() { System.out.print("Press ENTER to continue..."); in.nextLine(); }
}
