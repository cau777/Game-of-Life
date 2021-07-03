package life;

import java.util.Arrays;
import java.util.Random;

public class UniverseGenerator {
    private Universe universe;
    private Random random;
    private final int size;
    
    public UniverseGenerator(int size) {
        this.size = size;
        random = new Random();
        universe = new Universe(size, random);
    }
    
    public void advanceGenerations(int n) {
        for (int i = 0; i < n; i++) {
            advanceGeneration();
        }
    }
    
    public void advanceGeneration() {
        Universe newUniverse = new Universe(universe.getUniverseMatrix());
        
        /*
        Rules
        A live cell survives if it has two or three live neighbors; otherwise, it dies of boredom (<2) or overpopulation (>3).
        A dead cell is reborn if it has exactly three live neighbors.
         */
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                boolean cellState = universe.getUniverseCell(x, y);
                int total = 0;
                
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;
                        
                        boolean neighborCellState = universe.getUniverseCell((x + dx + size) % size, (y + dy + size) % size);
                        if (neighborCellState) {
                            total++;
                        }
                    }
                }
                
                if (cellState) {
                    if (total < 2 || total > 3) {
                        newUniverse.setUniverseCell(false, x, y);
                    }
                } else {
                    if (total == 3) {
                        newUniverse.setUniverseCell(true, x, y);
                    }
                }
            }
        }
        
        universe = newUniverse;
    }
    
    public void printUniverseState() {
        System.out.println(universe);
    }
    
    public int countAlive() {
        int alive = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (universe.getUniverseCell(x, y)) {
                    alive++;
                }
            }
        }
        return alive;
    }
    
    public boolean getUniverseCell(int x, int y) {
        return universe.getUniverseCell(x, y);
    }
}
