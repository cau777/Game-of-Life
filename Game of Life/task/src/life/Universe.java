package life;

import java.util.Arrays;
import java.util.Random;

public class Universe {
    private final boolean[][] universeMatrix;
    private final int size;
    
    public Universe(int size, Random generator){
        this.size = size;
        
        universeMatrix = new boolean[size][];
        for (int i = 0; i < size; i++) {
            universeMatrix[i] = new boolean[size];
            for (int j = 0; j < size; j++) {
                universeMatrix[i][j] = generator.nextBoolean();
            }
        }
    }
    
    public Universe(boolean[][] universeToCopy){
        this.size = universeToCopy.length;
        universeMatrix = new boolean[size][];
    
        for (int i = 0; i < size; i++) {
            universeMatrix[i] = new boolean[size];
            System.arraycopy(universeToCopy[i], 0, universeMatrix[i], 0, size);
        }
    }
    
    public boolean[][] getUniverseMatrix() {
        return universeMatrix;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
    
        for (boolean[] row : universeMatrix) {
            for (boolean cell : row) {
                builder.append(cell ? 'O' : ' ');
            }
            builder.append("\r\n");
        }
        
        return builder.toString();
    }
    
    public boolean getUniverseCell(int x, int y){
        return universeMatrix[x][y];
    }
    
    public void setUniverseCell(boolean state, int x, int y){
        universeMatrix[x][y] = state;
    }
}
