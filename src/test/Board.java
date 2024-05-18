package test;

import java.util.ArrayList;
import test.Tile.Bag;

public class Board {
private static final int BOARD_SIZE = 15;

    private Tile[][] gameBoard;
    private boolean isEmpty;

    private static Board board = null;

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized Board getBoard() {
        if (board == null)
            board = new Board();

        return board;
    }

    public Board() {
        this.gameBoard = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.isEmpty = true;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                this.gameBoard[row][col] = null;
            }
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Tile[][] getTiles() {
        Tile[][] newCopy = new Tile[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row ++) {
            System.arraycopy(this.gameBoard[row], 0, newCopy[row], 0, BOARD_SIZE);
        }
        
        return newCopy;
    }

    public boolean boardLegal(Word word) {
        
        // Check if the word is inside the board 
        boolean placement_ok = false;
        if (word.getRow() <= BOARD_SIZE && word.getCol() <= BOARD_SIZE) {
            if (word.isVertical()) {
                if (word.getRow() + word.getTiles().length <= BOARD_SIZE) {
                    placement_ok = true;
                }
            }
            else if (word.getCol() + word.getTiles().length <= BOARD_SIZE) {
                placement_ok = true;
            }
        }

        if (!placement_ok) {
            return false;
        }

        // Check if there is an overlap
        boolean overlap = false;
        boolean isOnStar = false;
        Tile[][] tiles_of_board = getBoard().getTiles();
        Tile[] tiles_of_word = word.getTiles();
        int i = 0;

        if (word.isVertical()) {
            int upperRow = word.getRow() - 1;
            int downerRow = word.getRow() + tiles_of_word.length + 1;
            if (upperRow >= 0 && tiles_of_board[upperRow][word.getCol()] != null) {
                overlap = true;
            } else if (downerRow < tiles_of_board.length && tiles_of_board[downerRow][word.getCol()] != null) {
                overlap = true;
            }

            for (int row = word.getRow(); row < word.getRow() + tiles_of_word.length && !overlap; row++) {
                if (row == (BOARD_SIZE / 2) + 1 && word.getCol() == (BOARD_SIZE / 2) + 1) {
                    isOnStar = true;
                }

                if (tiles_of_board[row][word.getCol()].equals(tiles_of_word[i])) {
                    overlap = true;
                    break;
                } else if (tiles_of_board[row][word.getCol()] != null) {
                    return false;
                }

                int rightCol = word.getCol() + 1;
                if (rightCol < tiles_of_board[0].length && tiles_of_board[row][rightCol] != null) {
                    overlap = true;
                    break;
                }

                int leftCol = word.getCol() - 1;
                if (leftCol >= 0 && tiles_of_board[row][leftCol] != null) {
                    overlap = true;
                    break;
                }

                i++;
            }
        }
        else {
            int rightCol = word.getCol() + tiles_of_word.length + 1;
            int leftCol = word.getCol() - 1;
            if (leftCol >= 0 && tiles_of_board[word.getRow()][leftCol] != null) {
                overlap = true;
            } else if (rightCol < tiles_of_board[0].length && tiles_of_board[word.getRow()][rightCol] != null) {
                overlap = true;
            }

            for (int col = word.getCol(); col < word.getCol() + tiles_of_word.length && !overlap; col++) {
                if (word.getRow() == (BOARD_SIZE / 2) + 1 && col == (BOARD_SIZE / 2) + 1) {
                    isOnStar = true;
                }

                if (tiles_of_board[word.getRow()][col].equals(tiles_of_word[i])) {
                    overlap = true;
                    break;
                } else if (tiles_of_board[word.getRow()][col] != null) {
                    return false;
                }

                int upperRow = word.getRow() - 1;
                if (upperRow >= 0 && tiles_of_board[upperRow][col] != null) {
                    overlap = true;
                    break;
                }

                int downerRow = word.getRow() + word.getTiles().length + 1;
                if (downerRow < tiles_of_board.length && tiles_of_board[downerRow][col] != null) {
                    overlap = true;
                    break;
                }

                i++;
            }
        }

        if (getBoard().isEmpty()) {
            if (!isOnStar) {
                return false;
            } else {
                getBoard().isEmpty = false;
                return true;
            }
        }
        
        return overlap;
    }

    public boolean dictionaryLegal() {
        return true;
    }

    public ArrayList<Word> getWords(Word word) {
        Tile[][] tiles_of_board = getBoard().getTiles();

        ArrayList<Word> allWords = new ArrayList<>();

        allWords.add(word);
        
        if (!word.isVertical()) { // WORD IS IN A ROW
            for (int col = word.getCol(); col < word.getCol()+word.getTiles().length; col++) {
                int row = word.getRow();

                while((row - 1) >= 0 && tiles_of_board[row - 1][col] != null) {
                    row--;
                }

                int wordRow = row;
                int wordCol = col;
                int tileCount = 0;

                while(row < tiles_of_board.length && tiles_of_board[row][col] != null) {
                    tileCount++;
                    row++;
                }

                if (tileCount < 1) {
                    continue;
                }

                Tile[] wordTiles = new Tile[tileCount];

                row = wordRow;
                int index = 0;
                while(index < tileCount) {
                    wordTiles[index] = tiles_of_board[row][col];
                    row++;
                    index++;
                }

                allWords.add(new Word(wordTiles, wordRow, wordCol, true));
            }

            // TODO: Check if feature is nessecary!!!
            // int col = word.getCol() + word.getTiles().length;
            // int wordRow = word.getRow();
            // int wordCol = col;
            // int tileCount = 1;
            
            // while((col + 1) < tiles_of_board[0].length && tiles_of_board[wordRow][col] != null) {
            //     tileCount++;
            //     col++;
            // }

            // if (tileCount > 1) {
            //     Tile[] wordTiles = new Tile[tileCount];
            //     col = wordCol;
            //     int index = 0;

            //     while(index < tileCount) {
            //         wordTiles[index] = tiles_of_board[wordRow][col];
            //         col++;
            //         index++;
            //     }

            //     allWords.add(new Word(wordTiles, wordRow, wordCol, false));
            // }

        } else { // WORD IS IN A COLOUMN
            for (int row = word.getRow(); row < word.getRow()+word.getTiles().length; row++) {
                int col = word.getCol();

                while((col-1) >= 0 && tiles_of_board[row][col-1] != null) {
                    col--;
                }

                int wordCol = col;
                int wordRow = row;
                int tileCount = 0;
            
                while (col < tiles_of_board.length && tiles_of_board[row][col] != null) {
                    tileCount++;
                    col++;
                }

                if (tileCount < 1) {
                    continue;
                }

                Tile[] wordTiles = new Tile[tileCount];
                col = wordCol;
                int index = 0;
                while (index < tileCount) {
                    wordTiles[index] = tiles_of_board[row][col];
                    col++;
                    index++;
                }
                
                allWords.add(new Word(wordTiles, wordRow, wordCol, true));
            }
        }
        return allWords;
    }

    public int getScore(Word word) {
        int[][] triple_word_score = {
            {0, 0},  // Row 0, Column 0
            {0, 7}, {0, 14},
            {7, 0}, {7, 14},
            {14, 0}, {14, 7}, {14, 14}
        };

        int[][] triple_letter_score = {
            {1, 5}, {1, 9},
            {5, 1}, {5, 5}, {5, 9}, {5, 13},
            {9, 1}, {9, 5}, {9, 9}, {9, 13},
            {13, 5}, {13, 9}
        };

        int[][] double_word_score = {
            {1, 1}, {1, 13}, {1, 12}, {1, 11}, {1, 10},
            {2, 2},
            {3, 3},
            {4, 4},
            {10, 4}, {10, 10},
            {11, 3}, {11, 11},
            {12, 2}, {12, 12},
            {13, 1}, {13, 13}
        };
        
        int[][] double_letter_score = {
            {0, 3}, {0, 11},
            {2, 6}, {2, 8},
            {3, 0}, {3, 7}, {3, 14},
            {6, 2}, {6, 6}, {6, 8}, {6, 12},
            {7, 3}, {7, 11},
            {8, 2}, {8, 6}, {8, 8}, {8, 12},
            {11, 0}, {11, 7}, {11, 14},
            {12, 6}, {12, 8},
            {14, 3}, {14, 11}
        };
        
        int basic_score = 0;
        Tile[] tiles_of_word = word.getTiles();
        Bag bag = Bag.getBag();
        Tile[] tiles_scores = bag.getTiles();

        // basic score
        for (int tile_word = 0; tile_word < tiles_of_word.length; tile_word++) {
            for (int tile_letter = 0; tile_letter < tiles_scores.length; tile_letter++) {
                if (tiles_of_word[tile_word].equals(tiles_scores[tile_letter])) {
                    basic_score += tiles_scores[tile_letter].score;
                }
            }
        }

        // bonus score
        int bonus_score = 0;
        int row = word.getRow();
        int col = word.getCol(); 

        return basic_score;

    }
}

// /Users/amitmaz/Downloads/assignment/Assigment/src