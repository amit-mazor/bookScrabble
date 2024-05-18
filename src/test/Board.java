package test;

import java.util.ArrayList;
import test.Tile.Bag;

public class Board {
private static final int BOARD_SIZE = 15;

    private Tile[][] gameBoard;
    private int[][] bonusGameBoard;
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

        // Bonus Board Initialization  
        // 1 -normal ,2 - double letter score , 3 - triple letter score, 22 - double word score , 33- triple word score
        this.bonusGameBoard = new int[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                this.bonusGameBoard[i][j] = 1;
            }
        }

        //33
        bonusGameBoard[0][0]= 33 ;  bonusGameBoard[0][7] = 33; bonusGameBoard[0][14]= 33;
        bonusGameBoard[7][0]= 33; bonusGameBoard[7][14]= 33;
        bonusGameBoard[14][0]= 33; bonusGameBoard[14][7]= 33; bonusGameBoard[14][14]= 33;

        //22
        for (int i=1; i<14;i++){
            if (i == 5){
                i = 10;
            }
            bonusGameBoard[i][i] = 22; bonusGameBoard[i][14-i] = 22;
        }

        // 2
        bonusGameBoard[0][3]= 2; bonusGameBoard[0][11]= 2;
        bonusGameBoard[2][6]= 2; bonusGameBoard[2][8]= 2;
        bonusGameBoard[3][0]= 2; bonusGameBoard[3][7]= 2; bonusGameBoard[3][14]= 2;
        bonusGameBoard[6][2]= 2; bonusGameBoard[6][6]= 2; bonusGameBoard[6][8]= 2; bonusGameBoard[6][12]= 2;
        bonusGameBoard[7][3]= 2; bonusGameBoard[7][11]= 2;
        bonusGameBoard[8][2]= 2; bonusGameBoard[8][6]= 2; bonusGameBoard[8][8]= 2; bonusGameBoard[8][12]= 2;
        bonusGameBoard[11][0]= 2; bonusGameBoard[11][7]= 2; bonusGameBoard[11][14]= 2;
        bonusGameBoard[12][6]= 2; bonusGameBoard[12][8]= 2;
        bonusGameBoard[14][3]= 2; bonusGameBoard[14][11]= 2;

        // 3
        bonusGameBoard[1][5]= 3; bonusGameBoard[1][9]= 3;
        bonusGameBoard[5][1]= 3; bonusGameBoard[5][5]= 3; bonusGameBoard[5][9]= 3; bonusGameBoard[5][13]= 3;
        bonusGameBoard[9][1]= 3; bonusGameBoard[9][5]= 3; bonusGameBoard[9][9]= 3; bonusGameBoard[9][13]= 3;
        bonusGameBoard[13][5]= 3; bonusGameBoard[13][9]= 3;
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
        int basic_score = 0;
        int bonus = 0;
        Tile[] tiles_of_word = word.getTiles();
        Bag bag = Bag.getBag();
        Tile[] tiles_scores = bag.getTiles();

        for (int tile_word = 0; tile_word < tiles_of_word.length; tile_word++) { 
            for (int tile_letter = 0; tile_letter < tiles_scores.length; tile_letter++) { 
                if (tiles_of_word[tile_word].equals(tiles_scores[tile_letter])) { 
                    basic_score += tiles_scores[tile_letter].score; 
                }
            }
        }

        int row = word.getRow();
        int col = word.getCol();
        boolean wordDoubleBonus = false;
        boolean wordTripleBonus = false;
        int index = 0;
        
        if (word.isVertical()) {
            for (int i = row; i < row+word.getTiles().length; i++) {
                bonus = bonusGameBoard[i][col]; 
                
                if (bonus == 2) {
                    basic_score = basic_score + word.getTiles()[index].score;
                }

                if (bonus == 3) {
                    basic_score = basic_score + word.getTiles()[index].score * 2;
                }

                if (bonus == 22) {
                    wordDoubleBonus = true;
                }
        
                if (bonus == 33) {
                    wordTripleBonus = true;
                }
                index++;
            }
        }
        else {
            for (int i = col; i < col+word.getTiles().length; i++) {
                bonus = bonusGameBoard[row][i];

                if (bonus == 2) {
                    basic_score = basic_score + word.getTiles()[index].score;
                }

                if (bonus == 3) {
                    basic_score = basic_score + word.getTiles()[index].score * 2;
                }

                if (bonus == 22) {
                    wordDoubleBonus = true;
                }
        
                if (bonus == 33) {
                    wordTripleBonus = true;
                }

                index++;
            }
        }

        if (wordDoubleBonus) {
            basic_score = basic_score * 2;
        }

        if (wordTripleBonus) {
            basic_score = basic_score * 3;
        }

        return basic_score;

    }

    public static void main(String[] args) {
        Board board = Board.getBoard();

        Tile.Bag bag = Tile.Bag.getBag();

        Tile[] tiles = new Tile[]{
            bag.getTile('A'),
            bag.getTile('B'),
            bag.getTile('C'),
            bag.getTile('D')
        };
        Word word = new Word(tiles, 0 , 0 , false); 
        
        int score = board.getScore(word);
        System.out.println("Score for the word : " + score);
    }
}
