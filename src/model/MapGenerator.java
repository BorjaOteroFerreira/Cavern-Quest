///GENERADO POR IA 

package model;

// Import the necessary libraries
import java.util.Random;

// Define the MapGenerator class
public class MapGenerator {
public static void main(String[] args) {
    hasPath(generateMap( 10, 10),10,10);
}
    // Define the generateMap method that takes in the number of rows and columns as arguments
    
public static int[][] generateMap(int rows, int cols) {
    int[][] map = new int[rows][cols];
    Random rand = new Random();
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                if (i == rows / 2 && j == 0) {
                    map[i][j] = 6; // Marca la entrada con un 6
                } else if (i == rows / 2 && j == cols - 1) {
                    map[i][j] = 9; // Marca la salida con un 9
                } else {
                    map[i][j] = 1; // Establece las celdas del borde en 1 si no son la entrada o la salida
                }
            } else {
                if ((map[i-1][j] == 0 && map[i][j-1] == 0 && map[i+1][j] == 0 && map[i][j+1] == 0) && !(i == rows / 2 && j == 0) && !(i == rows / 2 && j == cols - 1) && map[i][j] != 6 && map[i][j] != 9) {
                    map[i][j] = rand.nextInt(2) ; // Si hay ceros en diagonal, se establece en uno
                } else {
                    map[i][j] = 0;
                }
            }
        }
    }
    for (int i = 0; i < rows; i++) {
        System.out.print("{");
        for (int j = 0; j < cols; j++) {
            System.out.print(map[i][j] + ", ");
        }
        System.out.println("},");
    }
    return map;
}
// Define the canReach method that takes in the generated map and returns true if there is a path from 6 to 9 using only 0s
public static boolean canReach(int[][] map) {
    int rows = map.length;
    int cols = map[0].length;
    boolean[][] visited = new boolean[rows][cols];
    int startX = -1;
    int startY = -1;
    // Find the starting point (6)
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (map[i][j] == 6) {
                startX = i;
                startY = j;
                break;
            }
        }
    }
    // Perform DFS to find the end point (9)
    return dfs(map, visited, startX, startY, rows, cols);
}

// Define the dfs method that performs a depth-first search on the map to find the end point (9)
public static boolean dfs(int[][] map, boolean[][] visited, int x, int y, int rows, int cols) {
    if (map[x][y] == 9) {
        return true;
    }
    visited[x][y] = true;
    if (x > 0 && map[x-1][y] == 0 && !visited[x-1][y] && !(x-1 == rows / 2 && y == 0)) {
        if (dfs(map, visited, x-1, y, rows, cols)) {
            return true;
        }
    }
    if (x < rows-1 && map[x+1][y] == 0 && !visited[x+1][y] && !(x+1 == rows / 2 && y == 0)) {
        if (dfs(map, visited, x+1, y, rows, cols)) {
            return true;
        }
    }
    if (y > 0 && map[x][y-1] == 0 && !visited[x][y-1] && !(x == rows / 2 && y-1 == 0)) {
        if (dfs(map, visited, x, y-1, rows, cols)) {
            return true;
        }
    }
    if (y < cols-1 && map[x][y+1] == 0 && !visited[x][y+1] && !(x == rows / 2 && y+1 == 0)) {
        if (dfs(map, visited, x, y+1, rows, cols)) {
            return true;
        }
    }
    // Check if the number 9 is surrounded by ones
    if (map[x][y] == 9) {
        if ((x > 0 && map[x-1][y] == 1) || (x < rows-1 && map[x+1][y] == 1) || (y > 0 && map[x][y-1] == 1) || (y < cols-1 && map[x][y+1] == 1)) {
            return true;
        }
        return false;
    }
    
    return false;
}
public static boolean hasPath(int[][] map, int rows, int cols) {
    boolean[][] visited = new boolean[rows][cols];
    int startX = -1;
    int startY = -1;
    int endX = -1;
    int endY = -1;
    // Find the starting and ending points
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (map[i][j] == 6) {
                startX = i;
                startY = j;
            } else if (map[i][j] == 9) {
                endX = i;
                endY = j;
            }
        }
    }
    // If either the starting or ending point is not found, return false
    if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
        return false;
    }
    // Run dfs to check if there is a path of zeros between the starting and ending points
    return dfs(map, visited, startX, startY, rows, cols);
}

}
    

