package test;
import java.util.Arrays;
import java.util.Random;

public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.letter;
        hash = 97 * hash + this.score;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.letter != other.letter) {
            return false;
        }
        return this.score == other.score;
    }

    @Override
    public String toString() {
        return "Tile [letter=" + letter + ", score=" + score + "]";
    }

    public static class Bag {
        private static final int NUM_LETTERS = 26;
        private static final int[] LETTERS_COUNT = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};

        private static Bag bag = null;

        private int[] letterCounts; // array of how many times each letter exists
        private Tile[] tiles; // array of tiles with the value of each letter

        // Static method
        // Static method to create instance of Singleton class
        public static synchronized Bag getBag() {
            if (bag == null)
                bag = new Bag();
    
            return bag;
        }

        private Bag() {
            this.letterCounts = new int[NUM_LETTERS];
            this.tiles = new Tile[NUM_LETTERS];

            System.arraycopy(LETTERS_COUNT, 0, this.letterCounts, 0, NUM_LETTERS);

            int score;
            for (int i = 0; i < 26; i++) {
                char letter = (char) ('A' + i);
                score = switch (i) {
                    case 10 -> 5;
                    case 1, 2, 12, 15 -> 3;
                    case 3, 6 -> 2;
                    case 5, 7, 21, 22, 24 -> 4;
                    case 9, 23 -> 8;
                    case 25 -> 10;
                    default -> 1;
                };
                tiles[i] = new Tile(letter, score);
            }
        }
        
        public Tile[] getTiles() {
            return tiles;
        }

        public Tile getRand() {
            Random random = new Random();
            int randomIndex = random.nextInt(this.tiles.length-1);
            boolean all_empty = true;

            for (int i = 0; i < this.letterCounts.length; i++) {
                if (this.letterCounts[i] != 0){
                    all_empty = false;
                    break;
                }
            }
            if (all_empty) {
                return null;
            }
            
            while (this.letterCounts[randomIndex] == 0) {
                randomIndex = random.nextInt(this.tiles.length-1);
            }

            this.letterCounts[randomIndex] -= 1;
            return this.tiles[randomIndex];
        }

        public Tile getTile(char letterToGrab) {
            for (int i = 0; i < this.tiles.length; i++) {
                if (this.tiles[i].getLetter() == letterToGrab) {
                    if (this.letterCounts[i] > 0) {
                        this.letterCounts[i] -= 1;
                        return this.tiles[i];
                    }
                    break;
                }
            }
            return null;
        }

        public void put(Tile tileToInsert) {
            char theLetter = tileToInsert.letter;
            int index = theLetter - 'A'; // Converts to num which is the index in the array

            if (this.letterCounts[index] < LETTERS_COUNT[index]) {
                this.letterCounts[index] += 1;
            }
        }

        @Override
        public String toString() {
            return "Bag [letterCounts=" + Arrays.toString(letterCounts) + ", tiles=" + Arrays.toString(tiles) + "]";
        }

        public int size() {
            int sumTiles = 0;
            for (int i = 0; i < this.letterCounts.length; i++) {
                sumTiles += this.letterCounts[i];
            }
            return sumTiles;
        }

        public int[] getQuantities() {
            int[] newCopy = new int[NUM_LETTERS];
            System.arraycopy(this.letterCounts, 0, newCopy, 0, NUM_LETTERS);
            return newCopy;
        }
    }
}