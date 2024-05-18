package test;

import java.util.Arrays;

public class Word {
    private final Tile[] tiles;
    private final int row;
    private final int col;
    private final boolean vertical; // is the word written from up to down ? it not, then its from left to right

    @Override
    public String toString() {
        return "Word [tiles=" + Arrays.toString(tiles) + ", row=" + row + ", col=" + col + ", vertical=" + vertical
                + "]";
    }

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.col = col;
        this.row = row;
        this.tiles = tiles;
        this.vertical = vertical;
    }

    // Getters
    public Tile[] getTiles() {
        return tiles;
    }

    public boolean isVertical() {
        return vertical;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Arrays.deepHashCode(this.tiles);
        hash = 59 * hash + this.row;
        hash = 59 * hash + this.col;
        hash = 59 * hash + (this.vertical ? 1 : 0);
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
        final Word other = (Word) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.col != other.col) {
            return false;
        }
        if (this.vertical != other.vertical) {
            return false;
        }
        return Arrays.deepEquals(this.tiles, other.tiles);
    }
}

