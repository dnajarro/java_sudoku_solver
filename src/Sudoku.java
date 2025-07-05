import java.util.*;

public class Sudoku {

    static final int NUMBER_OF_ROWS = 9;
    static final int NUMBER_OF_COLUMNS = 9;
    static final int MAX_SUDOKU_NUMBER = 9;
    static final int MIN_SUDOKU_NUMBER = 1;

    public ArrayList<ArrayList<Cell>> cells = new ArrayList<>();

    public Sudoku() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                cells.get(i).add(new Cell());
            }
        }
    }

    public boolean hasValidValues() {
        boolean isValid = true;

        for (List<Cell> row: cells) {
            for (Cell cell : row) {
                if (cell.value == Cell.NO_VALUE) {
                    isValid = false;
                    break;
                }
            }
        }

        return isValid;
    }

    public void copyPuzzle(Sudoku other) {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.cells.get(j).get(i).copyCell(other.cells.get(j).get(i));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < NUMBER_OF_ROWS; r++) {
            for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
                if (this.cells.get(r).get(c).value != Cell.NO_VALUE) {
                    sb.append(this.cells.get(r).get(c).value);
                }
                else sb.append(".");
            }
        }
        return sb.toString();
    }

    public void printPuzzle() {
        for (int r = 0; r < NUMBER_OF_ROWS; r++) {
            if (r % 3 == 0 && r > 0) System.out.println("\n");

            for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
                if (c % 3 == 0 && c > 0) {
                    System.out.print(" ");
                }
                if (this.cells.get(r).get(c).value == Cell.NO_VALUE) {
                    System.out.print(".");
                }
                else System.out.print(this.cells.get(r).get(c).value);
            }
            System.out.println("\n");
        }
    }

    public boolean isSolved() {
        for (int r = 0; r < NUMBER_OF_ROWS; r++) {
            for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
                if (this.cells.get(r).get(c).value == Cell.NO_VALUE) {
                    return false;
                }
            }
        }

        return true;
    }

    public void inputPuzzle(String puzzleStr) {
        int r = 0;
        int c = 0;
        for (char v: puzzleStr.toCharArray()) {
            if (v != '.') {
                if (Character.isDigit(v)) cells.get(r).get(c).assignValue(Character.getNumericValue(v));
            }
            c += 1;
            if (c >= NUMBER_OF_COLUMNS) {
                c = 0;
                r++;
            }
        }

        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
                if (cells.get(row).get(col).value != Cell.NO_VALUE) {
                    boolean success = forwardCheckRemove(row, col, cells.get(row).get(col).value);
                    if (!success) {
                        System.out.println("Contradiction in Puzzle! Invalid input. Exiting.");
                        System.out.println("Puzzle string: " + puzzleStr);
                        System.exit(1);
                    }
                }
            }
        }
    }

    public boolean forwardCheckRemove(int row, int col, int value) {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (i != row) {
                if (!cells.get(i).get(col).removeValue(value)) {
                    return false;
                }
            }
        }

        for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
            if (j != col) {
                if (!cells.get(row).get(j).removeValue(value)) {
                    return false;
                }
            }
        }

        int gridNum = getGrid(row, col);
        // int cell = getCell(row, col);
        List<Position> gridLocations = new ArrayList<>();
        // check variables in the same 3x3 grid
        for (int x = 0; x < NUMBER_OF_COLUMNS; x++) {
            int gridRow = getRow(gridNum, x);
            int gridCol = getCol(gridNum, x);
            gridLocations.add(new Position(gridRow, gridCol));
        }
        for (Position p: gridLocations) {
            if (p.getCellRow() != row && p.getCellCol() != col) {
                if (!cells.get(p.getCellRow()).get(p.getCellCol()).removeValue(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int forwardCheckCount(int row, int col, int value) {
        int count = 0;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (i != row) {
                if (cells.get(i).get(col).domain.contains(value)) {
                    count++;
                }
            }
        }

        for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
            if (j != col) {
                if (cells.get(row).get(j).domain.contains(value)) {
                    count++;
                }
            }
        }

        int gridNum = getGrid(row, col);
        // int cell = getCell(row, col);
        List<Position> gridLocations = new ArrayList<>();
        for (int x = 0; x < NUMBER_OF_COLUMNS; x++) {
            int gridRow = getRow(gridNum, x);
            int gridCol = getCol(gridNum, x);
            gridLocations.add(new Position(gridRow, gridCol));
        }
        for (Position p: gridLocations) {
            if (p.getCellRow() != row && p.getCellCol() != col) {
                if (cells.get(p.getCellRow()).get(p.getCellCol()).domain.contains(Integer.valueOf(value))) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getGrid(int row, int col) {
        int grid = 3 * (row / 3) + (col / 3);
        return grid;
    }

    public int getCell(int row, int col) {
        int grid = 3 * (row / 3) + (col / 3);
        int baseR = 3 * (grid / 3);
        int baseC = 3 * (grid % 3);
        int offsetR = row - baseR;
        int offsetC = col - baseC;
        int cell = 3 * offsetR + offsetC;
        return cell;
    }

    public int getRow(int grid, int cell) {
        int baseR = 3 * (grid / 3);
//        int baseC = 3 * grid % 3;
        int offsetR = cell / 3;
//        int offsetC = cell % 3;
        int row = baseR + offsetR;
        return row;
    }

    public int getCol(int grid, int cell) {
//        int baseR = 3 * grid / 3;
        int baseC = 3 * (grid % 3);
//        int offsetR = cell / 3;
        int offsetC = cell % 3;
        int col = baseC + offsetC;
        return col;
    }

    static class Position {

        private int row;
        private int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getCellRow() {
            return row;
        }

        public int getCellCol() {
            return col;
        }
    }

    public static class Helper {

        public List<Position> mrv(Sudoku puzzle, List<Position> unassigned) {
            Map<Integer, List<Position>> mrvMap = new HashMap<>();
            for (int i = MIN_SUDOKU_NUMBER; i <= MAX_SUDOKU_NUMBER; i++) {
                mrvMap.put(i, new ArrayList<>());
            }

            for (Position p: unassigned) {
                int row = p.getCellRow();
                int col = p.getCellCol();
                List<Integer> ascendingOrderDomains = new ArrayList<>(mrvMap.keySet());
                Collections.sort(ascendingOrderDomains);

                for (int sizeOfDomain: ascendingOrderDomains) {
                    if (puzzle.cells.get(row).get(col).value == Cell.NO_VALUE &&
                            puzzle.cells.get(row).get(col).domain.size() == sizeOfDomain) {
                        mrvMap.get(sizeOfDomain).add(p);
                    }
                }
            }
            for (int i = MIN_SUDOKU_NUMBER; i <= MAX_SUDOKU_NUMBER; i++) {
                if (!mrvMap.get(i).isEmpty()) {
                    return mrvMap.get(i);
                }
            }

            return new ArrayList<>();
        }

        public List<Position> maxDegree(Sudoku puzzle, List<Position> tied) {
            int maxC = 0;
            List<Position> mcVariables = new ArrayList<>();
            for (Position t: tied) {
                int curC = countConstraints(puzzle, t.getCellRow(), t.getCellCol());
                if (curC == maxC) {
                    mcVariables.add(t);
                }
                if (curC > maxC) {
                    maxC = curC;
                    mcVariables = new ArrayList<>(Arrays.asList(t));
                }
            }

            return mcVariables;
        }

        public int countConstraints(Sudoku puzzle, int row, int col) {
            int count = 0;
            for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
                if (puzzle.cells.get(row).get(c).value == Cell.NO_VALUE) {
                    count++;
                }
            }
            for (int r = 0; r < NUMBER_OF_ROWS; r++) {
                if (puzzle.cells.get(r).get(col).value == Cell.NO_VALUE) {
                    count++;
                }
            }
            int gridNum = puzzle.getGrid(row, col);
//            int cell = puzzle.getCell(row, col);
            List<Position> gridLocations = new ArrayList<>();
            for (int x = 0; x < MAX_SUDOKU_NUMBER; x++) {
                int gridRow = puzzle.getRow(gridNum, x);
                int gridCol = puzzle.getCol(gridNum, x);
                gridLocations.add(new Position(gridRow, gridCol));
            }

            for (Position cell: gridLocations) {
                if (cell.getCellRow() != row && cell.getCellCol() != col) {
                    if (puzzle.cells.get(cell.getCellRow()).get(cell.getCellCol()).value == Cell.NO_VALUE) {
                        count++;
                    }
                }
            }

            return count;
        }

        public List<Position> getUnassignedVariables(Sudoku puzzle) {
            List<Position> unassigned = new ArrayList<>();

            for (int r = 0; r < NUMBER_OF_ROWS; r++) {
                for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
                    if (puzzle.cells.get(r).get(c).value == Cell.NO_VALUE) {
                        unassigned.add(new Position(r, c));
                    }
                }
            }

            return unassigned;
        }

        public Position selectVariable(Sudoku puzzle) {

            // 0. Get the initial list of all unassigned variables
            List<Position> unassigned = getUnassignedVariables(puzzle);

            // 1. Use MRV heuristic to get list of variables with the min remaining values
            List<Position> minimumRemainingValues = mrv(puzzle, unassigned);

            // If MRV identifies a unique variable, then return it
            if (minimumRemainingValues.size() == 1) {
                return new Position(minimumRemainingValues.get(0).getCellRow(),
                        minimumRemainingValues.get(0).getCellCol());
            }

            // 2. Refine list to those with maximum degree
            List<Position> mostConstrainingVariables = maxDegree(puzzle, minimumRemainingValues);

            // 3. Return first variable in the list. This will be the only one if there was a unique most constraining
            // variable, or the "alphabetically first" or "arbitrary" one of them if there were ties
            return new Position(mostConstrainingVariables.get(0).getCellRow(),
                    mostConstrainingVariables.get(0).getCellCol());
        }

        public List<Integer> orderValues(Sudoku puzzle, int row, int col) {
            // Get the current domain for this variable
            List<Integer> domain = new ArrayList<>();
            int domainLength = puzzle.cells.get(row).get(col).domain.size();
            for (int i = 0; i < domainLength; i++) {
                domain.add(puzzle.cells.get(row).get(col).domain.get(i));
            }

            Map<Integer, Integer> valueDict = new HashMap<>();

            // Order the domain list by least-constrained value to most-constrained value
            for (int val: domain) {
                valueDict.put(val, puzzle.forwardCheckCount(row, col, val));
            }

            // sort the dictionary by value (i.e., the number of constraints in ascending order)
            List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(valueDict.entrySet());
            sortedEntries.sort(Comparator.comparingInt(Map.Entry::getValue));

            List<Integer> orderedValues = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry: sortedEntries) {
                orderedValues.add(entry.getKey());
            }

            return orderedValues;
        }

        public Sudoku backtrackingSearch(Sudoku puzzle) {
            // Perform a recursive backtracking search on puzzle, ensuring that a copy is made of the puzzle before
            // modifying it and recursing it.

            // If unsuccessful, a Cell.NO_VALUE is returned
            // If successful, this function returns the solved puzzle object

            // 1. Base case, is puzzle solved? If so, return the puzzle
            if (puzzle.isSolved()) return puzzle;

            // 2. Select a variable to assign next using selectVariable() function, returning row and col
            Position nextVariablePosition = selectVariable(puzzle);

            // 3. Select an ordering over the values using orderValues(r, c) where r, c are the position of the selected
            // variable. Returns a list of values
            List<Integer> orderedValues = orderValues(puzzle,
                    nextVariablePosition.getCellRow(), nextVariablePosition.getCellCol());

            // 4. For each value in the ordered list:
            for (int val: orderedValues) {

//                System.out.println(puzzle.toString());
                Sudoku newPuzzle = new Sudoku();

                newPuzzle.copyPuzzle(puzzle);

                newPuzzle.cells.get(nextVariablePosition.getCellRow()).get(nextVariablePosition.getCellCol()).assignValue(val);

                boolean isValid = newPuzzle.forwardCheckRemove(nextVariablePosition.getCellRow(),
                        nextVariablePosition.getCellCol(), val);

                if (!isValid) continue;

                Sudoku result = backtrackingSearch(newPuzzle);

                if (result != null) return result;
            }

            return null;
        }
    }
}
