import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        if (args.length != 1) System.out.println("USAGE: java Driver input/puzzles.txt");
        else {
            String startingSearch = String.format("Searching for solutions to puzzles from file: %s", args[0]);
            System.out.println(startingSearch);

            // Open puzzle file
            try {
                Scanner scanner = new Scanner(new File(args[0]));
                // How many puzzles to solve
                int maxToSolve = 100;

                // Keep track of number of puzzles solved
                int numSolved = 0;

                // Start time for solving
                long startTime = System.currentTimeMillis();

                while (scanner.hasNextLine()) {
                    String puzzle = scanner.nextLine();
                    String searchingMsg = String.format("\nSearching to find solution to following puzzle: %s", puzzle);
                    System.out.println(searchingMsg);

                    Sudoku newPuzzle = new Sudoku();

                    newPuzzle.inputPuzzle(puzzle.stripTrailing());

                    newPuzzle.printPuzzle();

                    Sudoku successPuzzle = new Sudoku.Helper().backtrackingSearch(newPuzzle);

                    if (successPuzzle != null) {
                        System.out.println("\n Successfully solved the puzzle! Here is the solution:\n");

                        successPuzzle.printPuzzle();
                        numSolved++;
                    } else {
                        System.out.println("\n!! Unable to solve the puzzle !!\n");
                        break;
                    }

                    if (numSolved >= maxToSolve) {
                        break;
                    }
                }

                long endTime = System.currentTimeMillis();
                scanner.close();

                double timeElapsed = (endTime - startTime) / 1000.0;
                String output = String.format("\n*****************\nSolved %d puzzles in %.3f seconds.", numSolved,
                        timeElapsed);

                System.out.println(output);

                double avgTime = 0;
                if (numSolved > 0) avgTime = timeElapsed / numSolved;

                String solveTimeStr = String.format("Average Solve Time = %.3f seconds", avgTime);
                System.out.println(solveTimeStr);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
