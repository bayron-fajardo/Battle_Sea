package Clases;

public class Ship {
    private int size;
    private boolean vertical;

    public Ship(int size, boolean vertical) {
        this.size = size;
        this.vertical = vertical;
    }

    public int getSize() {
        return size;
    }

    public boolean isVertical() {
        return vertical;
    }
}
