package com.langtonsant;

public class Solution {

    public static final char WHITE = '.';
    public static final char BLACK = '#';

    public static final int MOVES = 4;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    private static class Ant {

        /**
         * Constructs an Ant with the specified coordinates and direction.
         * @param row The row coordinate for the ant (zero-indexed).
         * @param col The column coordinate for the ant (zero-indexed).
         * @param direction The integer representation of the direction the ant is facing.
         */
        public Ant(int row, int col, int direction) {
            this.row = row;
            this.col = col;
            this.direction = direction;
        }

        private int row;
        private int col;
        private int direction;

        /**
         * @return Return the current row coordinate for the ant.
         */
        public int getRow() {
            return row;
        }

        /**
         * Set the row coordinate for the ant.
         * @param row The new row coordinate.
         */
        public void setRow(int row) {
            this.row = row;
        }

        /**
         * @return Return the current column coordinate for the ant.
         */
        public int getCol() {
            return col;
        }

        /**
         * Set the column coordinate for the ant.
         * @param col The new column coordinate.
         */
        public void setCol(int col) {
            this.col = col;
        }

        /**
         * @return Return the current direction of the ant (in integer format).
         */
        public int getDirection() {
            return direction;
        }

        /**
         * Set the direction for the ant.
         * @param direction The new direction value.
         */
        public void setDirection(int direction) {
            this.direction = direction;
        }
    }

    public static void main(String[] args) {

        char[][] grid = computeLangtonsAntGrid(3, 4, 1 , 1, '<', 7);
        printGrid(grid);
    }

    /**
     * This function will compute the simulate Langton's ant with the requested parameters and will return the final state
     * of the grid after the ant has taken all its requested steps.
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     * @param antRow The initial ant row (zero-indexed).
     * @param antCol The initial ant column (zero-indexed).
     * @param directionChar The initial ant direction represented as a char.
     * @param steps The number of steps the ant will take in the grid.
     *
     * @return The grid containing the final state of the Langton's Ant simulation where '.' represents white and '#'
     * represents black.
     */
    public static char[][] computeLangtonsAntGrid(int rows, int cols, int antRow, int antCol, char directionChar, int steps) {

        int direction = computeDirectionFromChar(directionChar);
        char[][] grid = constructInitialGrid(rows, cols);

        // Initialize a new ant object to keep track of the internal state (i.e. current location and direction)
        // of the ant.
        Ant ant = new Ant(antRow, antCol, direction);

        // Update the grid based on the rules of Langton's Ant.
        for (int i = 0; i < steps; i++) {
            updateLangtonsGridForAntMovement(grid, ant);
        }

        // Place the ant with the correct direction onto the grid for display.
        return drawAntOnGrid(grid, ant);
    }

  /**
   * The character is converted to an integer to make it easy to add or subtract 90 degrees, since
   * each 90 degree change can be represented by incrementing or decrementing the integer by 1 (modulo 4).
   *
   * @param directionChar The direction represented as a char ('^', '{@literal >}', 'v', '{@literal <}').
   * @return The char direction converted to an integer representation (0, 1, 2, 3).
   */
  private static int computeDirectionFromChar(char directionChar) {
        int direction;
        switch (directionChar) {
            case '^': {
                direction = UP;
                break;
            }
            case '>': {
                direction = RIGHT;
                break;
            }
            case 'v': {
                direction = DOWN;
                break;
            }
            case '<': {
                direction = LEFT;
                break;
            }
            default: {
                throw new IllegalArgumentException("The provided ant direction must be one of: '^', '>', 'v', '<'" );
            }
        }

        return direction;
    }

    /**
     * Create a grid of the requested size with all cells initialized to white ('.') and no cells black ('#').
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     * @return The initial grid.
     */
    private static char[][] constructInitialGrid(int rows, int cols) {
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = WHITE;
            }
        }

        return grid;
    }


    /**
     * Update the provided grid after the ant takes a step.
     *
     * @param grid The grid that will be updated.
     * @param ant The object containing the information for the ant (i.e. its location and direction).
     */
    private static void updateLangtonsGridForAntMovement(char[][] grid, Ant ant) {
        int i = ant.getRow();
        int j = ant.getCol();
        int rows = grid.length;
        int cols = grid[0].length;
        int direction = ant.getDirection();
        switch (grid[i][j]) {
            case WHITE: {
                // Turn the ant 90 degrees clockwise, and move it one step in that direction.
                direction = Math.floorMod(direction + 1, MOVES);
                // Change the grid location that the ant left to black.
                grid[i][j] = BLACK;
                break;
            }
            case BLACK: {
                // Turn the ant 90 degrees counter-clockwise, and move it one step in that direction.
                direction = Math.floorMod(direction - 1, MOVES);
                // Change the grid location that the ant left to white.
                grid[i][j] = WHITE;
                break;
            }
            default: {
                throw new IllegalStateException(String.format("Character at point (%d, %d) in grid is neither '%c' " +
                        "nor '%c'.", i, j, WHITE, BLACK));
            }
        }

        ant.setDirection(direction);
        moveAntOneStepInDirection(ant, direction, rows, cols);
    }

    /**
     * Move the ant one step in the provided direction.
     *
     * @param ant The ant which will be moved.
     * @param direction The direction to move the ant.
     * @param rows The total number of rows in the grid.
     * @param cols The total number of cols in the grid.
     */
    private static void moveAntOneStepInDirection(Ant ant, int direction, int rows, int cols) {
        int row = ant.getRow();
        int col = ant.getCol();
        switch (direction) {
            case UP: {
                ant.setRow(--row);
                break;
            }
            case RIGHT: {
                ant.setCol(++col);
                break;
            }
            case DOWN: {
                ant.setRow(++row);
                break;
            }
            case LEFT: {
                ant.setCol(--col);
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("The direction must be one of: %d, %d, %d, %d. It " +
                                "was instead %d.", UP, RIGHT, DOWN, LEFT, direction));
            }
        }

        // Check that the new position is valid.
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new UnsupportedOperationException(String.format("The ant can not be moved to point (%d, %d) as " +
                    "it lies outside of the grid.", row, col));
        }
    }

    private static void printGrid(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Place the ant onto the grid with its current direction for display. A copy of the provided grid is made and
     * returned.
     *
     * @param grid The grid on which to draw the ant location.
     * @param ant The ant that will be drawn onto the grid.
     * @return The copy of the grid with the ant placed on it.
     */
    private static char[][] drawAntOnGrid(char[][] grid, Ant ant) {
        int rows = grid.length;
        int cols = grid[0].length;

        char[][] gridCopy = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridCopy[i][j] = grid[i][j];
            }
        }

        int row = ant.getRow();
        int col = ant.getCol();
        switch (ant.getDirection()) {
            case UP: {
                gridCopy[row][col] = '^';
                break;
            }
            case RIGHT: {
                gridCopy[row][col] = '>';
                break;
            }
            case DOWN: {
                gridCopy[row][col] = 'v';
                break;
            }
            case LEFT: {
                gridCopy[row][col] = '<';
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("The direction must be one of: %d, %d, %d, %d. It " +
                        "was instead %d.", UP, RIGHT, DOWN, LEFT, ant.getDirection()));
            }
        }

        return gridCopy;
    }
}
