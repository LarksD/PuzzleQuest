import java.util.Random;
import java.io.*;

public class Board implements Serializable{
    private Tile[][] tiles;
    private Random random;

    public Board() {
        tiles = new Tile[8][8];
        random = new Random();
    }

    public void initialize() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(random.nextInt(7));
            }
        }
        shuffleBoard();
    }

    public void calculos(int length, Tile current) { // quebrar e procurar matches dnv
        if (current.getType() == 0) {
            System.out.println("Skull");
        } else if (current.getType() == 1) {
            System.out.println("Red");
        } else if (current.getType() == 2) {
            System.out.println("Blue");
        } else if (current.getType() == 3) {
            System.out.println("Green");
        } else if (current.getType() == 4) {
            System.out.println("Yellow");
        } else if (current.getType() == 5) {
            System.out.println("Gold");
        } else if (current.getType() == 6) {
            System.out.println("Experience");
        }

    }

    public int lookForMatches(boolean calc) {
    // Procurar combos horizontais
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 6; col++) {
            Tile current = tiles[row][col];
            int matchLength = 1;
            while (col + matchLength < 8 && current.equals(tiles[row][col + matchLength])) {
                matchLength++;
            }
            if (matchLength > 3) {
                if (calc) {
                    calculos(matchLength, current);
                }
                //System.out.println("Match of length " + matchLength + " found starting at: (" + row + "," + col + ")" + " of the type: " + current.toString());
                return 2;
            }
            if (matchLength == 3) {
                if (calc) {
                    calculos(matchLength, current);
                }
                //System.out.println("Match of length " + matchLength + " found starting at: (" + row + "," + col + ")" + " of the type: " + current.toString());
                return 1;
            }
        }
    }

    // Procurar combos Verticais
    for (int col = 0; col < 8; col++) {
        for (int row = 0; row < 6; row++) {
            Tile current = tiles[row][col];
            int matchLength = 1;
            while (row + matchLength < 8 && current.equals(tiles[row + matchLength][col])) {
                matchLength++;
            }
            if (matchLength > 3) {
                if (calc) {
                    calculos(matchLength, current);
                }
                //System.out.println("Match of length " + matchLength + " found starting at: (" + row + "," + col + ")" + " of the type: " + current.toString());
                return 2;
            }
            if (matchLength == 3) {
                if (calc) {
                    calculos(matchLength, current);
                }
                //System.out.println("Match of length " + matchLength + " found starting at: (" + row + "," + col + ")" + " of the type: " + current.toString());
                return 1;
            }
        }
    }

    return 0;
}



    private void shuffleBoard() {
        // dar shuffle até que não haja trios
        while (lookForMatches(false) != 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int x = random.nextInt(8);
                    int y = random.nextInt(8);
                    swap(i, j, x, y);
                }
            }
        }
    }

    public void swap(int x1, int y1, int x2, int y2) {
        Tile temp = tiles[x1][y1];
        tiles[x1][y1] = tiles[x2][y2];
        tiles[x2][y2] = temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Coluna com emojis
        String[] numberEmojis = {"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣"};
        sb.append("  ");
        for (int col = 0; col < 8; col++) {
            sb.append(numberEmojis[col]).append(" ");
        }
        sb.append("\n");

        // Rows with labels and emojis
        String[] letterEmojis = {"🅰", "🅱", "🅲️", "🅳️", "🅴️", "🅵️", "🅶️", "🅷️"};
        for (int row = 0; row < 8; row++) {
            sb.append(letterEmojis[row]).append(" ");
            for (int col = 0; col < 8; col++) {
                sb.append(tiles[row][col]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
