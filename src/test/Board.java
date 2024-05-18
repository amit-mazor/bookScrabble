package test;

import java.util.ArrayList;
import javax.swing.border.SoftBevelBorder;
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

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void print() {
        for (int i=0; i < BOARD_SIZE + 1; i++) {
            for (int j=0; j < BOARD_SIZE + 1; j++) {
                if (i == 0) {
                    if (j > 10) {
                        System.out.print(" " + (j - 1) + " ");
                    } else {
                        System.out.print(" " + (j - 1) + " ");
                    }
                } else if (j == 0) {
                    if (i > 10) {
                        System.out.print(" " + (i - 1) + " ");
                    } else {
                        System.out.print(" " + (i - 1) + "  ");   
                    }
                } else if (getBoard().gameBoard[i - 1][j - 1] == null) {
                    System.out.print(" _ ");
                } else {
                    System.out.print(" " + getBoard().gameBoard[i - 1][j - 1].getLetter() + " ");
                }
                
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
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

    // isInsideBoard 
    // gets a word and checks whether its fully inside the board or not.
    private boolean isInsideBoard(Word word) {
        boolean placement_ok = false;
        if (0 <= word.getRow() && word.getRow() <= BOARD_SIZE && 0 <= word.getCol() && word.getCol() <= BOARD_SIZE) {
            if (word.isVertical()) {
                if (word.getRow() + word.getTiles().length <= BOARD_SIZE) {
                    return true;
                }
            }
            else if (word.getCol() + word.getTiles().length <= BOARD_SIZE) {
                return true;
            }
        }

        return false;
    }

    // isOnCenter
    // gets a location on the board and checks if it's the center of the board.
    private static boolean isOnCenter(int row, int col) {
        if (row == (BOARD_SIZE / 2) && col == (BOARD_SIZE / 2)) {
            return true;
        }
        return false;
    }

    public boolean boardLegal(Word word) {
        // Check if the word is inside the board 
        if (!getBoard().isInsideBoard(word)) {
            return false;
        }

        // Check for overlap of a tile
        // Check if it's on the star (for the first word only)
        Tile[][] tiles_of_board = getBoard().getTiles();
        Tile[] tiles_of_word = word.getTiles();

        boolean isOverlap = false;
        boolean isOnStar = false;
        int i = 0;

        if (word.isVertical()) {
            int upperRow = word.getRow() - 1;
            int downerRow = word.getRow() + tiles_of_word.length + 1;
            if (upperRow >= 0 && tiles_of_board[upperRow][word.getCol()] != null) {
                isOverlap = true;
            } else if (downerRow < tiles_of_board.length && tiles_of_board[downerRow][word.getCol()] != null) {
                isOverlap = true;
            }

            for (int row = word.getRow(); row < word.getRow() + tiles_of_word.length; row++) {
                if (isOnCenter(row, word.getCol())) {
                    isOnStar = true;
                }

                if (tiles_of_board[row][word.getCol()] != null) {
                    if (tiles_of_word[i] == null) {
                        isOverlap = true;
                    } else {
                        return false;
                    }
                }

                int rightCol = word.getCol() + 1;
                if (rightCol < tiles_of_board[0].length && tiles_of_board[row][rightCol] != null) {
                    isOverlap = true;
                }

                int leftCol = word.getCol() - 1;
                if (leftCol >= 0 && tiles_of_board[row][leftCol] != null) {
                    isOverlap = true;
                }

                i++;
            }
        }
        else {
            int rightCol = word.getCol() + tiles_of_word.length + 1;
            int leftCol = word.getCol() - 1;
            if (leftCol >= 0 && tiles_of_board[word.getRow()][leftCol] != null) {
                isOverlap = true;
            } else if (rightCol < tiles_of_board[0].length && tiles_of_board[word.getRow()][rightCol] != null) {
                isOverlap = true;
            }

            for (int col = word.getCol(); col < word.getCol() + tiles_of_word.length; col++) {
                if (isOnCenter(word.getRow(), col)) {
                    isOnStar = true;
                }

                if (tiles_of_board[word.getRow()][col] != null) {
                    if (tiles_of_word[i] == null) {
                        isOverlap = true;
                    } else {
                        System.out.println("BAD: Tile:" + tiles_of_board[word.getRow()][col].toString() + ", WORD: " + word.toString());
                        return false;
                    }
                }

                int upperRow = word.getRow() - 1;
                if (upperRow >= 0 && tiles_of_board[upperRow][col] != null) {
                    isOverlap = true;
                }

                int downerRow = word.getRow() + word.getTiles().length + 1;
                if (downerRow < tiles_of_board.length && tiles_of_board[downerRow][col] != null) {
                    isOverlap = true;
                }

                i++;
            }
        }

        if (getBoard().isEmpty()) {
            if (!isOnStar) {
                return false;
            } else {
                return true;
            }
        }
        
        return isOverlap;
    }

    public boolean dictionaryLegal(Word word) {
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
                if (tiles_of_word[tile_word] != null && tiles_of_word[tile_word].equals(tiles_scores[tile_letter])) { 
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
                
                if (i == (BOARD_SIZE / 2) && col == (BOARD_SIZE / 2)) {
                    wordDoubleBonus = true;
                }

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

                if (row == (BOARD_SIZE / 2) && i == (BOARD_SIZE / 2)) {
                    wordDoubleBonus = true;
                }

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

    public int tryPlaceWord(Word word) {
        if (!(boardLegal(word))) {
            System.out.println("ILLEGAL WORD: " + word.toString());
            return 0;
        }
        
        if (!(dictionaryLegal(word))) {
            System.out.println("ILLEGAL DICT: " + word.toString());
            return 0;
        }
    
        ArrayList<Word> allWords = getWords(word);
        for (Word w : allWords) {
            if (!(dictionaryLegal(w))) {
                System.out.println("ILLEGAL OTHER W: " + w.toString());
                return 0;
            }
        }

        for (int row = word.getRow(), col = word.getCol(), index = 0;
        (index < word.getTiles().length); index++) {

            if (getBoard().gameBoard[row][col] == null) {
                getBoard().gameBoard[row][col] = word.getTiles()[index];
            }

            if (word.isVertical()) {
                row++;
            } else {
                col++;
            }
        }

        int score_total = 0;
        for (Word w : allWords) {
            score_total =+ getScore(w);
        }

        getBoard().setEmpty(false);
        return score_total;
    }



    public static void main(String[] args) {
        Board b = Board.getBoard();
		if(b!=Board.getBoard())
			System.out.println("board should be a Singleton (-5)");
		
		
		Bag bag = Bag.getBag();
		Tile[] ts=new Tile[10];
		for(int i=0;i<ts.length;i++) 
			ts[i]=bag.getRand();
		
		// Word w0=new Word(ts,0,6,true);
		// Word w1=new Word(ts,7,6,false);
		// Word w2=new Word(ts,6,7,true);
		// Word w3=new Word(ts,-1,7,true);
		// Word w4=new Word(ts,7,-1,false);
		// Word w5=new Word(ts,0,7,true);
		// Word w6=new Word(ts,7,0,false);
		
        // if(b.boardLegal(w0) || b.boardLegal(w1) || b.boardLegal(w2) || b.boardLegal(w3) || b.boardLegal(w4) || !b.boardLegal(w5) || !b.boardLegal(w6))
        //     System.out.println("your boardLegal function is wrong (-10)");

        for(Tile t : ts)
			bag.put(t);

        Word horn = new Word(get("HORN"), 7, 5, false);
        if(b.tryPlaceWord(horn)!=14)
            System.out.println("problem in placeWord for 1st word (-10)");

        getBoard().print();
        
        Word farm=new Word(get("FA_M"), 5, 7, true);
        if(b.tryPlaceWord(farm)!=9)
            System.out.println("problem in placeWord for 2ed word (-10): ");

        getBoard().print();

        Word paste=new Word(get("PASTE"), 9, 5, false);
        int score = b.tryPlaceWord(paste);
		if(score!=25)
			System.out.println("problem in placeWord for 3ed word (-10): " + score);
    }

    private static Tile[] get(String s) {
        Tile[] ts=new Tile[s.length()];
        int i=0;
        for(char c: s.toCharArray()) {
            ts[i]=Bag.getBag().getTile(c);
            i++;
        }
        return ts;
    }
}
